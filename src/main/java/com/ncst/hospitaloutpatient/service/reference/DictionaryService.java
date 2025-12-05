package com.ncst.hospitaloutpatient.service.reference;

import com.ncst.hospitaloutpatient.common.enums.DrugUnit;
import com.ncst.hospitaloutpatient.common.enums.Period;
import com.ncst.hospitaloutpatient.common.enums.MedicalItemType;
import com.ncst.hospitaloutpatient.mapper.reference.DictionaryMapper;
import com.ncst.hospitaloutpatient.model.dto.reference.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    public List<SettlementCategoryResponse> listSettlementCategories() {
        return dictionaryMapper.selectSettlementCategories();
    }

    public List<NoonSessionResponse> listNoonSessions() {
        return Arrays.stream(Period.values())
                .map(period -> new NoonSessionResponse(period.name(), period.getLabel()))
                .collect(Collectors.toList());
    }

    public List<NumberTypeResponse> listNumberTypes() {
        return dictionaryMapper.selectNumberTypes();
    }

    public List<PaymentMethodResponse> listPaymentMethods() {
        return dictionaryMapper.selectPaymentMethods();
    }

    public List<DrugCategoryResponse> listDrugCategories() {
        return dictionaryMapper.selectAllDrugCategories();
    }

    public List<ProjectTypeResponse> listItemTypes() {
        return Arrays.stream(MedicalItemType.values())
                .map(type -> new ProjectTypeResponse(type.name(), type.getLabel()))
                .collect(Collectors.toList());
    }

    public List<DrugUnit> listDrugUnits() {
        return Arrays.asList(DrugUnit.values());
    }
}