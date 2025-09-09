package com.ncst.hospitaloutpatient.mapper.hr;

import com.ncst.hospitaloutpatient.model.dto.hr.StaffDetailResponse;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    List<StaffDetailResponse> selectStaffDetailByFilter(
            @Param("keyword") String keyword,
            @Param("departmentId") Integer departmentId,
            @Param("roleId") Integer roleId,
            @Param("sortBy") String sortBy,
            @Param("order") String order,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    long countStaffDetailByFilter(
            @Param("keyword") String keyword,
            @Param("departmentId") Integer departmentId,
            @Param("roleId") Integer roleId
    );

    // 插入staff，staffId回填到Staff对象
    int insertStaff(StaffEntity staff);

    // 插入staff_account
    int insertStaffAccount(@Param("staffId") Integer staffId,
                           @Param("accountName") String accountName,
                           @Param("password") String password);

    // 插入staff_doctor_ext
    int insertStaffDoctorExt(@Param("staffId") Integer staffId,
                             @Param("isExpert") Integer isExpert);

    // 更新 staff 表
    int updateStaff(@Param("id") Integer id,
                    @Param("departmentId") Integer departmentId,
                    @Param("roleId") Integer roleId,
                    @Param("name") String name,
                    @Param("phone") String phone,
                    @Param("idCard") String idCard);

    // 更新 staff_doctor_ext 表（只更新 is_expert 字段，不存在则插入）
    int updateDoctorExt(@Param("id") Integer id, @Param("isExpert") Boolean isExpert);

    // 逻辑删除员工（将staff_account.status设为1）
    int deleteEmployeeByStaffId(@Param("staffId") Integer staffId);

    // 恢复员工（将staff_account.status设为0）
    int restoreEmployeeByStaffId(@Param("staffId") Integer staffId);

    // 根据staffId查询员工详情
    StaffDetailResponse selectStaffDetailById(@Param("staffId") Integer staffId);

    // 查询部门类型
    String getDepartmentType(@Param("departmentId") Integer departmentId);

    // 获取科室前缀
    String getDepartmentPrefix(@Param("departmentId") Integer departmentId);

    // 获取某科室当前年份的最大自增号码
    Integer getMaxSerialNumber(@Param("prefix") String prefix, @Param("year") String year);
}
