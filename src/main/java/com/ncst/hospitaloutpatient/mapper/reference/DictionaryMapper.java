package com.ncst.hospitaloutpatient.mapper.reference;

import com.ncst.hospitaloutpatient.model.dto.reference.DrugCategoryResponse;
import com.ncst.hospitaloutpatient.model.dto.reference.NumberTypeResponse;
import com.ncst.hospitaloutpatient.model.dto.reference.PaymentMethodResponse;
import com.ncst.hospitaloutpatient.model.dto.reference.SettlementCategoryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictionaryMapper {

    List<SettlementCategoryResponse> selectSettlementCategories();

    List<NumberTypeResponse> selectNumberTypes();

    List<PaymentMethodResponse> selectPaymentMethods();

    List<DrugCategoryResponse> selectAllDrugCategories();
}
