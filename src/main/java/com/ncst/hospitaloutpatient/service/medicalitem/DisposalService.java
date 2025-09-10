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
        int offset = (page - 1) * pageSize;
        // sortBy 校验，只允许 patientName/applyTime，order 只允许 asc/desc，防SQL注入
        if (!"patientName".equals(sortBy) && !"applyTime".equals(sortBy)) {
            sortBy = "applyTime";
        }
        if (!"asc".equalsIgnoreCase(order)) {
            order = "desc";
        }
        return disposalMapper.selectDisposalApplies(keyword, pageSize, offset, sortBy, order);
    }

    public long countDisposalApplies(String keyword) {
        return disposalMapper.countDisposalApplies(keyword);
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
        // 0. 检查申请记录的状态是否为 UNFINISHED
        String applyStatus = disposalMapper.getApplyStatusById(applyId);
        if (!"UNFINISHED".equals(applyStatus)) {
            throw new BusinessException(40002, "该申请记录当前状态不可执行");
        }
        // 1. 获取registrationId
        Integer registrationId = disposalMapper.getRegistrationIdByApplyId(applyId);
        if (registrationId == null) throw new BusinessException(40001, "申请不存在");

        // 2. 判断patient_visit.current_status，如果不是CHECKING则改为CHECKING
        String status = disposalMapper.getCurrentStatus(registrationId);
        if (!"CHECKING".equals(status)) {
            int result = disposalMapper.updateCurrentStatus(registrationId, "CHECKING");
            if(result == 0) {
                throw new BusinessException(500, "更新就诊状态失败");
            }
        }

        // 3. 查询申请的详细信息填充日志
        ItemApplyInfoForLog applyInfo = disposalMapper.getApplyInfoForLog(applyId);

        // 4. 插入操作日志
        MedicalItemOperationLog log = new MedicalItemOperationLog();
        Integer operatorId = getCurrentStaffId();
        log.setApplyId(applyId);
        log.setOperatorId(operatorId);
        log.setOperateType("EXECUTE");
        log.setItemId(applyInfo.getItemId());
        log.setItemName(applyInfo.getItemName());
        log.setItemType(applyInfo.getItemType());
        log.setPatientNo(applyInfo.getPatientNo());
        log.setPatientName(applyInfo.getPatientName());
        log.setRemark("执行检查项目");

        int result = disposalMapper.insert(log);
        if(result == 0) {
            throw new BusinessException(500, "插入操作日志失败");
        }
    }

    @Transactional
    public void submitDisposalResult(Integer applyId, ItemApplyResultDTO resultDTO) {
        // 1. 校验申请状态
        String applyStatus = disposalMapper.getApplyStatusById(applyId);
        if (!"UNFINISHED".equals(applyStatus)) {
            throw new BusinessException(400, "该申请非未完成状态，不能提交结果");
        }
        // 2. 更新 medical_item_apply 的 result 和 status
        int update = disposalMapper.updateResultAndStatus(applyId, resultDTO.getResult());
        if (update == 0) {
            throw new BusinessException(500, "结果更新失败");
        }

        // 3. 查询用于日志的明细
        ItemApplyInfoForLog applyInfo = disposalMapper.getApplyInfoForLog(applyId);

        // 4. 写入操作日志
        MedicalItemOperationLog log = new MedicalItemOperationLog();
        log.setApplyId(applyId);
        log.setOperatorId(getCurrentStaffId());
        log.setOperateType("INPUT_RESULT");
        log.setItemId(applyInfo.getItemId());
        log.setItemName(applyInfo.getItemName());
        log.setItemType(applyInfo.getItemType());
        log.setPatientNo(applyInfo.getPatientNo());
        log.setPatientName(applyInfo.getPatientName());
        log.setRemark(resultDTO.getRemark() != null ? resultDTO.getRemark() : "录入检查结果");
        int resultA = disposalMapper.insertOperationLog(log);
        if (resultA == 0) {
            throw new BusinessException(500, "操作日志插入失败");
        }

        // 5. 检查该 registration 下所有项目是否全部 FINISHED
        Integer registrationId = disposalMapper.getRegistrationIdByApplyId(applyId);
        int unfinishedCount = disposalMapper.countUnfinishedApplies(registrationId);
        if (unfinishedCount == 0) {
            // 6. 全部完成，更新 patient_visit.current_status
            int resultB = disposalMapper.updatePatientVisitStatus(registrationId, "WAITING_FOR_REVISIT");
            if(resultB == 0) {
                throw new BusinessException(500, "更新就诊状态失败");
            }
        }
    }

    public List<DepartmentSimpleDTO> listDisposalDepartments() {
        return disposalMapper.listDepartmentsByType("DISPOSAL");
    }

    public List<StaffSimpleDTO> listStaffsByDepartmentId(Integer departmentId) {
        return disposalMapper.listStaffsByDepartmentId(departmentId);
    }
}
