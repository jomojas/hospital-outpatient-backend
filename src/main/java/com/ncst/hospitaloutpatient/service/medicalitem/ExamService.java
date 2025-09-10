package com.ncst.hospitaloutpatient.service.medicalitem;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.medicalitem.ExamMapper;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.*;
import com.ncst.hospitaloutpatient.model.entity.medicalItem.MedicalItemOperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamService {
    @Autowired
    private ExamMapper examMapper;

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

    public List<ExamApplyDTO> listExamApplies(String keyword, Integer page, Integer pageSize, String sortBy, String order) {
        int offset = (page - 1) * pageSize;
        // sortBy 校验，只允许 patientName/applyTime，order 只允许 asc/desc，防SQL注入
        if (!"patientName".equals(sortBy) && !"applyTime".equals(sortBy)) {
            sortBy = "applyTime";
        }
        if (!"asc".equalsIgnoreCase(order)) {
            order = "desc";
        }
        return examMapper.selectExamApplies(keyword, pageSize, offset, sortBy, order);
    }

    public long countExamApplies(String keyword) {
        return examMapper.countExamApplies(keyword);
    }

    @Transactional
    public void cancelExamApply(Integer applyId) {
        // 1. 查询该申请对应的registrationId
        Integer registrationId = examMapper.getRegistrationIdByApplyId(applyId);
        if (registrationId == null) {
            throw new BusinessException(404, "申请记录不存在");
        }

        // 2. 检查patient_visit的current_status是否为WAITING_FOR_CHECKUP
        String status = examMapper.getCurrentStatus(registrationId);
        if (!"WAITING_FOR_CHECKUP".equals(status)) {
            throw new BusinessException(400, "当前状态不可取消项目");
        }

        // 3. 取消该申请（将status设为CANCELLED）
        int result = examMapper.cancelExamApply(applyId);
        if (result == 0) {
            throw new BusinessException(500, "项目取消失败");
        }
    }

    @Transactional
    public void executeExam(Integer applyId) {
        // 0. 检查申请记录的状态是否为 UNFINISHED
        String applyStatus = examMapper.getApplyStatusById(applyId);
        if (!"UNFINISHED".equals(applyStatus)) {
            throw new BusinessException(40002, "该申请记录当前状态不可执行");
        }
        // 1. 获取registrationId
        Integer registrationId = examMapper.getRegistrationIdByApplyId(applyId);
        if (registrationId == null) throw new BusinessException(40001, "申请不存在");

        // 2. 判断patient_visit.current_status，如果不是CHECKING则改为CHECKING
        String status = examMapper.getCurrentStatus(registrationId);
        if (!"CHECKING".equals(status)) {
            int result = examMapper.updateCurrentStatus(registrationId, "CHECKING");
            if(result == 0) {
                throw new BusinessException(500, "更新就诊状态失败");
            }
        }

        // 3. 查询申请的详细信息填充日志
        ItemApplyInfoForLog applyInfo = examMapper.getApplyInfoForLog(applyId);

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

        int result = examMapper.insert(log);
        if(result == 0) {
            throw new BusinessException(500, "插入操作日志失败");
        }
    }

    @Transactional
    public void submitExamResult(Integer applyId, ItemApplyResultDTO resultDTO) {
        // 1. 校验申请状态
        String applyStatus = examMapper.getApplyStatusById(applyId);
        if (!"UNFINISHED".equals(applyStatus)) {
            throw new BusinessException(400, "该申请非未完成状态，不能提交结果");
        }
        // 2. 更新 medical_item_apply 的 result 和 status
        int update = examMapper.updateResultAndStatus(applyId, resultDTO.getResult());
        if (update == 0) {
            throw new BusinessException(500, "结果更新失败");
        }

        // 3. 查询用于日志的明细
        ItemApplyInfoForLog applyInfo = examMapper.getApplyInfoForLog(applyId);

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
        int resultA = examMapper.insertOperationLog(log);
        if (resultA == 0) {
            throw new BusinessException(500, "操作日志插入失败");
        }

        // 5. 检查该 registration 下所有项目是否全部 FINISHED
        Integer registrationId = examMapper.getRegistrationIdByApplyId(applyId);
        int unfinishedCount = examMapper.countUnfinishedApplies(registrationId);
        if (unfinishedCount == 0) {
            // 6. 全部完成，更新 patient_visit.current_status
            int resultB = examMapper.updatePatientVisitStatus(registrationId, "WAITING_FOR_REVISIT");
            if(resultB == 0) {
                throw new BusinessException(500, "更新就诊状态失败");
            }
        }
    }

    public List<DepartmentSimpleDTO> listExamDepartments() {
        return examMapper.listDepartmentsByType("EXAM");
    }

    public List<StaffSimpleDTO> listStaffsByDepartmentId(Integer departmentId) {
        return examMapper.listStaffsByDepartmentId(departmentId);
    }

    public List<MedicalItemOperateLogDTO> listOperateLogs(String keyword, String operateType, Integer page, Integer pageSize, String sortBy, String order) {
        int offset = (page - 1) * pageSize;
        return examMapper.listOperateLogs(keyword, operateType, getCurrentStaffId(), sortBy, order, offset, pageSize);
    }

    public long countOperateLogs(String keyword, String operateType) {
        return examMapper.countOperateLogs(keyword, operateType, getCurrentStaffId());
    }
}
