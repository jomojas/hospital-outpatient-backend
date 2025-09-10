package com.ncst.hospitaloutpatient.mapper.medicalitem;

import com.ncst.hospitaloutpatient.model.dto.medicalitem.DepartmentSimpleDTO;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.ExamApplyDTO;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.ItemApplyInfoForLog;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.StaffSimpleDTO;
import com.ncst.hospitaloutpatient.model.entity.medicalItem.MedicalItemOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DisposalMapper {
    List<ExamApplyDTO> selectDisposalApplies(@Param("keyword") String keyword,
                                        @Param("limit") Integer limit,
                                        @Param("offset") Integer offset,
                                        @Param("sortBy") String sortBy,
                                        @Param("order") String order);

    long countDisposalApplies(@Param("keyword") String keyword);

    /**
     * 根据applyId查registrationId
     */
    Integer getRegistrationIdByApplyId(@Param("applyId") Integer applyId);

    /**
     * 取消项目申请（将status设为CANCELLED）
     */
    int cancelDisposalApply(@Param("applyId") Integer applyId);

    String getCurrentStatus(@Param("registrationId") Integer registrationId);

    int updateCurrentStatus(@Param("registrationId") Integer registrationId, @Param("status") String status);

    ItemApplyInfoForLog getApplyInfoForLog(@Param("applyId") Integer applyId);

    int insert(MedicalItemOperationLog log);

    String getApplyStatusById(@Param("applyId") Integer applyId);

    int updateResultAndStatus(@Param("applyId") Integer applyId, @Param("result") String result);

    int insertOperationLog(MedicalItemOperationLog log);

    int countUnfinishedApplies(@Param("registrationId") Integer registrationId);

    int updatePatientVisitStatus(@Param("registrationId") Integer registrationId, @Param("status") String status);

    List<DepartmentSimpleDTO> listDepartmentsByType(@Param("type") String type);

    List<StaffSimpleDTO> listStaffsByDepartmentId(Integer departmentId);
}
