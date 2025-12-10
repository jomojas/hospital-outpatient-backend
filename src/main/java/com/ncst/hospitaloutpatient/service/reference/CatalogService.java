package com.ncst.hospitaloutpatient.service.reference;

import com.ncst.hospitaloutpatient.common.enums.DrugUnit;
import com.ncst.hospitaloutpatient.common.enums.MedicalItemType;
import com.ncst.hospitaloutpatient.mapper.reference.CatalogMapper;
import com.ncst.hospitaloutpatient.model.dto.reference.DrugDetailResponse;
import com.ncst.hospitaloutpatient.model.dto.reference.MedicalItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {
    @Autowired
    private CatalogMapper catalogMapper;

    public List<MedicalItemResponse> listExamItems(Integer page, Integer size, String keyword) {
        int offset = (page - 1) * size;
        List<MedicalItemResponse> items = catalogMapper.selectExamItems(size, offset, keyword);
        for (MedicalItemResponse item : items) {
            item.setItemTypeLabel(
                    MedicalItemType.valueOf(item.getItemType()).getLabel()
            );
        }
        return items;
    }

    public int countExamItems(String keyword) {
        return catalogMapper.countExamItems(keyword);
    }

    public List<MedicalItemResponse> listLabItems(int page, int pageSize, String keyword) {
        int offset = (page - 1) * pageSize;
        List<MedicalItemResponse> items = catalogMapper.selectLabItems(pageSize, offset, keyword);
        // 转中文label
        for (MedicalItemResponse item : items) {
            item.setItemTypeLabel(MedicalItemType.valueOf(item.getItemType()).getLabel());
        }
        return items;
    }

    public int countLabItems(String keyword) {
        return catalogMapper.countLabItems(keyword);
    }

    public List<MedicalItemResponse> listDisposalItems(int page, int pageSize, String keyword) {
        int offset = (page - 1) * pageSize;
        List<MedicalItemResponse> items = catalogMapper.selectDisposalItems(pageSize, offset, keyword);
        // 设置中文 label
        for (MedicalItemResponse item : items) {
            item.setItemTypeLabel(MedicalItemType.valueOf(item.getItemType()).getLabel());
        }
        return items;
    }

    public int countDisposalItems(String keyword) {
        return catalogMapper.countDisposalItems(keyword);
    }

    public List<DrugDetailResponse> listDrugCatalog(int page, int pageSize, String keyword, Integer categoryId) {
        int offset = (page - 1) * pageSize;
        List<DrugDetailResponse> list = catalogMapper.selectDrugCatalog(pageSize, offset, keyword, categoryId);
        for (DrugDetailResponse d : list) {
            d.setUnit(DrugUnit.toLabel(d.getUnit()));
        }
        return list;
    }

    public int countDrugCatalog(String keyword, Integer categoryId) {
        return catalogMapper.countDrugCatalog(keyword, categoryId);
    }
}
