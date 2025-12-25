package com.ncst.hospitaloutpatient.mapper.hr;

import com.ncst.hospitaloutpatient.model.dto.hr.StaffDetailResponse;
import com.ncst.hospitaloutpatient.model.dto.hr.StaffRoleResponse;
import com.ncst.hospitaloutpatient.model.entity.auth.StaffEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    // 根据 roleId 获取角色名
    String getRoleNameById(@Param("roleId") Integer roleId);

    // 获取某前缀账号的最大后三位数字（如 DOC009 -> 9）
    Integer getMaxAccountSeqByPrefix(@Param("prefix") String prefix);

    /** 检查 staff 是否存在 */
    boolean existsStaffById(@Param("staffId") Integer staffId);

    /** 按 staffId 更新登录密码（存加密后的密码） */
    int updatePasswordByStaffId(@Param("staffId") Integer staffId, @Param("password") String password);

    /** 查询所有角色 */
    List<StaffRoleResponse> selectAllRoles();
}
