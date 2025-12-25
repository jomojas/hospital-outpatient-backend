package com.ncst.hospitaloutpatient.service.inventory;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.inventory.DrugMapper;
import com.ncst.hospitaloutpatient.model.dto.inventory.CreateDrugRequest;
import com.ncst.hospitaloutpatient.model.dto.inventory.UpdateDrugRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DrugService {
    @Autowired
    private DrugMapper drugMapper;

    @Transactional
    public void createDrug(CreateDrugRequest request) {
        // 1. 校验药品分类是否存在
        boolean categoryExists = drugMapper.existsDrugCategory(request.getCategoryId());
        if (!categoryExists) {
            throw new BusinessException(400, "药品分类不存在，categoryId=" + request.getCategoryId());
        }
        // 2. 插入药品
        int row = drugMapper.insertDrug(request);
        if (row == 0) {
            throw new BusinessException(500, "新增药品失败");
        }
    }

    @Transactional
    public void updateDrug(Integer drugId, UpdateDrugRequest request) {
        boolean categoryExists = drugMapper.existsDrugCategory(request.getCategoryId());
        if (!categoryExists) {
            throw new BusinessException(400, "药品分类不存在，categoryId=" + request.getCategoryId());
        }
        int row = drugMapper.updateDrug(drugId, request);
        if (row == 0) {
            throw new BusinessException(500, "药品信息更新失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Integer drugId) {
        int rows = drugMapper.toggleStatus(drugId);
        if (rows == 0) {
            throw new BusinessException(500, "切换药品状态失败，药品可能不存在");
        }
    }
}
