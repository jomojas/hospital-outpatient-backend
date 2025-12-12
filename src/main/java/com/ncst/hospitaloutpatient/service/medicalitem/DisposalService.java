package com.ncst.hospitaloutpatient.service.medicalitem;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.medicalitem.DisposalMapper;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.*;
import com.ncst.hospitaloutpatient.model.entity.medicalItem.MedicalItemOperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DisposalService {
    @Autowired
    private DisposalMapper disposalMapper;

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

    public List<ExamApplyDTO> listDisposalApplies(String keyword, Integer page, Integer pageSize, String sortBy, String order) {
        Integer staffId = getCurrentStaffId();
        if (staffId == null) throw new BusinessException(401, "Unauthorized");
        Integer departmentId = disposalMapper.getDepartmentIdByStaffId(staffId);
        if (departmentId == null) throw new BusinessException(404, "Department not found");

        int offset = (page - 1) * pageSize;
        if (!"patientName".equals(sortBy) && !"applyTime".equals(sortBy)) sortBy = "applyTime";
        if (!"asc".equalsIgnoreCase(order)) order = "desc";

        LocalDate today = LocalDate.now();
        return disposalMapper.selectDisposalApplies(keyword, pageSize, offset, sortBy, order, departmentId, today);
    }

    public long countDisposalApplies(String keyword) {
        Integer staffId = getCurrentStaffId();
        if (staffId == null) throw new BusinessException(401, "Unauthorized");
        Integer departmentId = disposalMapper.getDepartmentIdByStaffId(staffId);
        if (departmentId == null) throw new BusinessException(404, "Department not found");

        LocalDate today = LocalDate.now();
        return disposalMapper.countDisposalApplies(keyword, departmentId, today);
    }

    @Transactional
    public void cancelDisposalApply(Integer applyId) {
        // 1. 查询该申请对应的registrationId
        Integer registrationId = disposalMapper.getRegistrationIdByApplyId(applyId);
        if (registrationId == null) {
            throw new BusinessException(404, "申请记录不存在");
        }

        // 2. 检查patient_visit的current_status是否为WAITING_FOR_CHECKUP
        String status = disposalMapper.getCurrentStatus(registrationId);
        if (!"WAITING_FOR_CHECKUP".equals(status)) {
            throw new BusinessException(400, "当前状态不可取消项目");
        }

        // 3. 取消该申请（将status设为CANCELLED）
        int result = disposalMapper.cancelDisposalApply(applyId);
        if (result == 0) {
            throw new BusinessException(500, "项目取消失败");
        }
    }

    @Transactional
    public void executeDisposal(Integer applyId) {
        String applyStatus = disposalMapper.getApplyStatusById(applyId);
        if (!"UNFINISHED".equals(applyStatus)) {
            throw new BusinessException(40002, "该申请记录当前状态不可执行");
        }

        Integer registrationId = disposalMapper.getRegistrationIdByApplyId(applyId);
        if (registrationId == null) throw new BusinessException(40001, "申请不存在");

        String status = disposalMapper.getCurrentStatus(registrationId);
        if (!"CHECKING".equals(status)) {
            int result = disposalMapper.updateCurrentStatus(registrationId, "CHECKING");
            if (result == 0) throw new BusinessException(500, "更新就诊状态失败");
        }

        ItemApplyInfoForLog applyInfo = disposalMapper.getApplyInfoForLog(applyId);

        Integer operatorId = getCurrentStaffId();
        if (operatorId == null) throw new BusinessException(401, "未登录");

        MedicalItemOperationLog log = new MedicalItemOperationLog();
        log.setApplyId(applyId);
        log.setOperatorId(operatorId);
        log.setOperateType("EXECUTE");
        log.setItemId(applyInfo.getItemId());
        log.setItemName(applyInfo.getItemName());
        log.setItemType(applyInfo.getItemType());
        log.setPatientNo(applyInfo.getPatientNo());
        log.setPatientName(applyInfo.getPatientName());
        log.setRemark("执行处置项目");

        int insertLog = disposalMapper.insert(log);
        if (insertLog == 0) throw new BusinessException(500, "插入操作日志失败");

        int updPerformer = disposalMapper.updatePerformer(applyId, operatorId, LocalDateTime.now());
        if (updPerformer == 0) throw new BusinessException(500, "更新执行人失败");
    }

    @Transactional
    public void submitDisposalResult(Integer applyId, ItemApplyResultDTO resultDTO) {
        String applyStatus = disposalMapper.getApplyStatusById(applyId);
        if (!"UNFINISHED".equals(applyStatus)) {
            throw new BusinessException(400, "该申请非未完成状态，不能提交结果");
        }

        Integer recorderId = getCurrentStaffId();
        if (recorderId == null) throw new BusinessException(401, "未登录");

        int update = disposalMapper.updateResultStatusAndRecorder(
                applyId,
                resultDTO.getResult(),
                "FINISHED",
                recorderId
        );
        if (update == 0) throw new BusinessException(500, "结果更新失败");

        ItemApplyInfoForLog applyInfo = disposalMapper.getApplyInfoForLog(applyId);

        MedicalItemOperationLog log = new MedicalItemOperationLog();
        log.setApplyId(applyId);
        log.setOperatorId(recorderId);
        log.setOperateType("INPUT_RESULT");
        log.setItemId(applyInfo.getItemId());
        log.setItemName(applyInfo.getItemName());
        log.setItemType(applyInfo.getItemType());
        log.setPatientNo(applyInfo.getPatientNo());
        log.setPatientName(applyInfo.getPatientName());
        log.setRemark(resultDTO.getRemark() != null ? resultDTO.getRemark() : "录入处置结果");

        int resultA = disposalMapper.insertOperationLog(log);
        if (resultA == 0) throw new BusinessException(500, "操作日志插入失败");

        Integer registrationId = disposalMapper.getRegistrationIdByApplyId(applyId);
        int unfinishedCount = disposalMapper.countUnfinishedApplies(registrationId);
        if (unfinishedCount == 0) {
            int resultB = disposalMapper.updatePatientVisitStatus(registrationId, "WAITING_FOR_REVISIT");
            if (resultB == 0) throw new BusinessException(500, "更新就诊状态失败");
        }
    }

    public List<DepartmentSimpleDTO> listDisposalDepartments() {
        return disposalMapper.listDepartmentsByType("DISPOSAL");
    }

    public List<StaffSimpleDTO> listStaffsByDepartmentId(Integer departmentId) {
        return disposalMapper.listStaffsByDepartmentId(departmentId);
    }

    public List<MedicalItemOperateLogDTO> listOperateLogs(String keyword, String operateType, Integer page, Integer pageSize, String sortBy, String order) {
        int offset = (page - 1) * pageSize;
        return disposalMapper.listOperateLogs(keyword, operateType, getCurrentStaffId(), sortBy, order, offset, pageSize);
    }

    public long countOperateLogs(String keyword, String operateType) {
        return disposalMapper.countOperateLogs(keyword, operateType, getCurrentStaffId());
    }
}
