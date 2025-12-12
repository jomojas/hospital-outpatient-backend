package com.ncst.hospitaloutpatient.mapper.medicalitem;

import com.ncst.hospitaloutpatient.model.dto.medicalitem.*;
import com.ncst.hospitaloutpatient.model.entity.medicalItem.MedicalItemOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LabMapper {
    Integer getDepartmentIdByStaffId(@Param("staffId") Integer staffId);

    List<ExamApplyDTO> selectLabApplies(@Param("keyword") String keyword,
                                        @Param("pageSize") Integer pageSize,
                                        @Param("offset") Integer offset,
                                        @Param("sortBy") String sortBy,
                                        @Param("order") String order,
                                        @Param("departmentId") Integer departmentId,
                                        @Param("today") LocalDate today);

    long countLabApplies(@Param("keyword") String keyword,
                         @Param("departmentId") Integer departmentId,
                         @Param("today") LocalDate today);

    /**
     * 根据applyId查registrationId
     */
    Integer getRegistrationIdByApplyId(@Param("applyId") Integer applyId);

    /**
     * 取消项目申请（将status设为CANCELLED）
     */
    int cancelLabApply(@Param("applyId") Integer applyId);

    String getCurrentStatus(@Param("registrationId") Integer registrationId);

    int updateCurrentStatus(@Param("registrationId") Integer registrationId, @Param("status") String status);

    ItemApplyInfoForLog getApplyInfoForLog(@Param("applyId") Integer applyId);

    int insert(MedicalItemOperationLog log);

    String getApplyStatusById(@Param("applyId") Integer applyId);

    int updatePerformer(@Param("applyId") Integer applyId,
                        @Param("performerId") Integer performerId,
                        @Param("performTime") LocalDateTime performTime);

    int updateResultStatusAndRecorder(@Param("applyId") Integer applyId,
                                      @Param("result") String result,
                                      @Param("status") String status,
                                      @Param("resultRecorderId") Integer resultRecorderId);

    int insertOperationLog(MedicalItemOperationLog log);

    int countUnfinishedApplies(@Param("registrationId") Integer registrationId);

    int updatePatientVisitStatus(@Param("registrationId") Integer registrationId, @Param("status") String status);

    List<DepartmentSimpleDTO> listDepartmentsByType(@Param("type") String type);

    List<StaffSimpleDTO> listStaffsByDepartmentId(Integer departmentId);

    List<MedicalItemOperateLogDTO> listOperateLogs(
            @Param("keyword") String keyword,
            @Param("operateType") String operateType,
            @Param("operatorId") Integer operatorId,
            @Param("sortBy") String sortBy,
            @Param("order") String order,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    long countOperateLogs(
            @Param("keyword") String keyword,
            @Param("operateType") String operateType,
            @Param("operatorId") Integer operatorId
    );
}
