package com.ncst.hospitaloutpatient.mapper.inventory;

import com.ncst.hospitaloutpatient.model.dto.inventory.CreateDrugRequest;
import com.ncst.hospitaloutpatient.model.dto.inventory.UpdateDrugRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DrugMapper {
    // 检查药品分类是否存在
    boolean existsDrugCategory(@Param("categoryId") Integer categoryId);

    // 插入药品
    int insertDrug(CreateDrugRequest request);

    int updateDrug(@Param("drugId") Integer drugId, @Param("req") UpdateDrugRequest req);
}
