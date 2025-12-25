package com.ncst.hospitaloutpatient.mapper.medicalitem;

import com.ncst.hospitaloutpatient.model.dto.medicalitem.CreateMedicalItemRequest;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.UpdateMedicalItemRequest;
import com.ncst.hospitaloutpatient.model.dto.reference.MedicalItemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MedicalItemMapper {

    /**
     * 查询医疗项目列表
     * @param keyword 关键字（名称/编码）
     * @param status 状态过滤（null查所有）
     * @return 列表
     */
    List<MedicalItemResponse> selectItems(@Param("keyword") String keyword,
                                          @Param("status") Integer status,
                                          @Param("departmentId") Integer departmentId);

    /**
     * 分页查询医疗项目列表
     */
    List<MedicalItemResponse> selectItemsPaged(
            @Param("keyword") String keyword,
            @Param("status") Integer status,
            @Param("departmentId") Integer departmentId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 统计医疗项目数量（用于分页）
     */
    long countItems(
            @Param("keyword") String keyword,
            @Param("status") Integer status,
            @Param("departmentId") Integer departmentId
    );

    /**
     * 新增项目
     */
    int insertItem(CreateMedicalItemRequest request);

    /**
     * 更新项目
     */
    int updateItem(@Param("itemId") Integer itemId,
                   @Param("req") UpdateMedicalItemRequest request);

    /**
     * 切换状态 (1->0, 0->1)
     */
    int toggleStatus(@Param("itemId") Integer itemId);
}