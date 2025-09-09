package com.ncst.hospitaloutpatient.mapper.reference;

import com.ncst.hospitaloutpatient.model.dto.reference.DrugDetailResponse;
import com.ncst.hospitaloutpatient.model.dto.reference.MedicalItemResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CatalogMapper {
    List<MedicalItemResponse> selectExamItems(@Param("size") int size, @Param("offset") int offset, @Param("keyword") String keyword);

    int countExamItems(@Param("keyword") String keyword);

    List<MedicalItemResponse> selectLabItems(@Param("size") int size, @Param("offset") int offset, @Param("keyword") String keyword);

    int countLabItems(@Param("keyword") String keyword);

    List<MedicalItemResponse> selectDisposalItems(@Param("size") int size, @Param("offset") int offset, @Param("keyword") String keyword);

    int countDisposalItems(@Param("keyword") String keyword);

    List<DrugDetailResponse> selectDrugCatalog(@Param("size") int size,
                                               @Param("offset") int offset,
                                               @Param("keyword") String keyword,
                                               @Param("categoryId") Integer categoryId);

    int countDrugCatalog(@Param("keyword") String keyword,
                         @Param("categoryId") Integer categoryId);
}
