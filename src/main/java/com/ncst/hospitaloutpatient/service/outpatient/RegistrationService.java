package com.ncst.hospitaloutpatient.service.outpatient;

import com.ncst.hospitaloutpatient.common.enums.*;
import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.outpatient.RegistrationMapper;
import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationRequest;
import com.ncst.hospitaloutpatient.model.dto.outpatient.CreateRegistrationResponse;
import com.ncst.hospitaloutpatient.model.dto.outpatient.RegistrationDetailResponse;
import com.ncst.hospitaloutpatient.model.entity.outpatient.PatientVisit;
import com.ncst.hospitaloutpatient.model.entity.outpatient.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationMapper registrationMapper;

    public Integer getCurrentStaffId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            // 你在 filter 里 set 的是字符串 staffId
            String staffIdStr = authentication.getPrincipal().toString();
            try {
                return Integer.parseInt(staffIdStr);
            } catch (NumberFormatException e) {
                // 可以做异常处理
            }
        }
        return null;
    }

    @Transactional
    public CreateRegistrationResponse createRegistration(CreateRegistrationRequest request) {
        // 1. 插入 registration 表
        int regInserted = registrationMapper.insertRegistration(request);
        Integer registrationId = request.getGeneratedRegistrationId();
        if (regInserted != 1 || registrationId == null || registrationId <= 0) {
            throw new BusinessException(500, "挂号记录插入失败！");
        }

        // 2. 构造 PatientVisit 对象并插入
        LocalDateTime now = LocalDateTime.now();
        PatientVisit patientVisit = new PatientVisit();
        patientVisit.setPatientId(request.getPatientId());
        patientVisit.setRegistrationId(registrationId);
        patientVisit.setDepartmentId(request.getDepartmentId());
        patientVisit.setDoctorId(request.getDoctorId());
        patientVisit.setVisitDate(request.getVisitDate());
        patientVisit.setCurrentStatus(VisitStatus.WAITING_FOR_CONSULTATION.name());
        patientVisit.setStatusChangedAt(now);
        patientVisit.setCreatedAt(now);
        patientVisit.setUpdatedAt(now);

        int visitInserted = registrationMapper.insertPatientVisit(patientVisit);
        Integer visitId = patientVisit.getVisitId();
        if (visitInserted != 1 || visitId == null || visitId <= 0) {
            throw new BusinessException(500, "就诊主流程记录插入失败！");
        }

        // 3. 更新 doctor_quota.used + 1
        int quotaUpdated = registrationMapper.incrementDoctorQuotaUsed(
                request.getDoctorId(),
                request.getVisitDate()
                // 可根据你的表结构补充 period/numberType 条件
        );
        if (quotaUpdated != 1) {
            throw new BusinessException(500, "更新医生号额失败！");
        }

        // 4. 插入 transaction 消费记录
        Integer cashierId = getCurrentStaffId();
        Transaction transaction = new Transaction();
        transaction.setBusinessType(BusinessType.REGISTRATION.name());
        transaction.setRegistrationId(registrationId);
        transaction.setPatientId(request.getPatientId());
        transaction.setAmount(request.getPayableAmount());
        transaction.setTransactionType(TransactionType.PAY.name());
        transaction.setPaymentMethodId(request.getPaymentMethodId());
        transaction.setTransactionTime(now);
        transaction.setCashierId(cashierId); // 可根据登录用户获取
        transaction.setRemark("挂号费");
        transaction.setSettlementTypeId(request.getSettlementTypeId());

        int transInserted = registrationMapper.insertTransaction(transaction);
        Integer transactionId = transaction.getTransactionId();
        if (transInserted != 1) {
            throw new BusinessException(500, "消费记录插入失败！");
        }

        // 4. 返回响应
        CreateRegistrationResponse response = new CreateRegistrationResponse();
        response.setRegistrationId(registrationId);
        response.setVisitId(visitId);
        response.setTransactionId(transactionId);
        return response;
    }

    public RegistrationDetailResponse getRegistration(Integer id) {
        return registrationMapper.selectRegistrationById(id);
    }

    public List<RegistrationDetailResponse> listRegistrations(
            int offset, int pageSize,
            LocalDate date, Integer deptId,
            Integer doctorId, String status, String keyword,
            String sortBy, String order
    ) {
        List<RegistrationDetailResponse> rawList = registrationMapper.selectRegistrations(
                offset, pageSize, date, deptId, doctorId, status, keyword, sortBy, order
        );
        // 直接覆盖原字段为中文
        return rawList.stream().peek(item -> {
            // period
            if (item.getPeriod() != null) {
                try {
                    item.setPeriod(Period.valueOf(item.getPeriod()).getLabel());
                } catch (Exception e) { /* ignore */ }
            }
            // numberType
            if (item.getNumberType() != null) {
                item.setNumberType(NumberType.getLabelByValue(item.getNumberType()));
            }
            // currentStatus
            if (item.getCurrentStatus() != null) {
                item.setCurrentStatus(VisitStatus.getLabelByValue(item.getCurrentStatus()));
            }
        }).collect(Collectors.toList());
    }


    public long countRegistrations(
            LocalDate date, Integer deptId,
            Integer doctorId, String status, String keyword
    ) {
        return registrationMapper.countRegistrations(date, deptId, doctorId, status, keyword);
    }

    @Transactional
    public void cancelRegistration(Integer registrationId) {
        // 1. 更新 patient_visit 的 current_status
        int updateCount = registrationMapper.updatePatientVisitStatusToFinished(registrationId);
        if (updateCount == 0) {
            throw new BusinessException(500, "更新挂号状态失败（挂号ID不存在或已完成）");
        }

        // 2. 查询挂号详情
        RegistrationDetailResponse reg = registrationMapper.selectRegistrationById(registrationId);
        if (reg == null) {
            throw new BusinessException(500, "挂号记录不存在");
        }

        // 3. 插入退费交易记录
        Transaction refund = new Transaction();
        Integer cashierId = getCurrentStaffId();
        refund.setBusinessType("REGISTRATION");
        refund.setRegistrationId(registrationId);
        refund.setPatientId(reg.getPatientId());
        refund.setAmount(reg.getPayableAmount());
        refund.setTransactionType("REFUND");
        refund.setPaymentMethodId(reg.getPaymentMethodId());
        refund.setTransactionTime(LocalDateTime.now());
        refund.setCashierId(cashierId); // 实际应取当前登录操作员
        refund.setRemark("退挂号费");
        refund.setSettlementTypeId(reg.getSettlementTypeId());
        int insertCount = registrationMapper.insertTransaction(refund);
        if (insertCount == 0) {
            throw new BusinessException(500, "生成退费记录失败");
        }
    }

}
