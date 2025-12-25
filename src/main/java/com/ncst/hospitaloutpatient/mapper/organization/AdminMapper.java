package com.ncst.hospitaloutpatient.mapper.organization;

import com.ncst.hospitaloutpatient.model.dto.organization.CreateRegistrationLevelRequest;
import com.ncst.hospitaloutpatient.model.dto.organization.RegistrationLevelResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface AdminMapper {

    /**
     * 查询所有号别配置
     */
    List<RegistrationLevelResponse> selectRegistrationLevels();

    /**
     * 更新价格
     * 注意：根据表结构，主键是 String 类型的 numberType
     */
    int updatePrice(@Param("code") String code, @Param("price") BigDecimal price);

    // ... 原有代码 ...

    /**
     * 检查Code是否存在
     */
    int checkCodeExists(@Param("code") String code);

    /**
     * 插入新号别
     */
    int insertRegistrationLevel(CreateRegistrationLevelRequest request);

    /**
     * 更新状态
     */
    int updateLevelStatus(@Param("code") String code, @Param("status") Integer status);
}