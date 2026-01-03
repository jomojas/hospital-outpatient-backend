package com.ncst.hospitaloutpatient.service.medicalitem;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.CreateMedicalItemRequest;
import com.ncst.hospitaloutpatient.model.dto.medicalitem.UpdateMedicalItemRequest;
import com.ncst.hospitaloutpatient.model.dto.reference.MedicalItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MedicalItemService {

    @Autowired
    private com.ncst.hospitaloutpatient.mapper.medicalitem.MedicalItemMapper medicalItemMapper;

    /**
     * 获取项目列表
     * @param keyword 搜索关键字
     * @param status 状态过滤 (null查所有，用于管理端；1查启用，用于医生端)
     */
    public List<MedicalItemResponse> listItems(String keyword, Integer status, Integer departmentId, Integer page, Integer pageSize) {
        int safePage = (page == null || page < 1) ? 1 : page;
        int safePageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        int offset = (safePage - 1) * safePageSize;
        return medicalItemMapper.selectItemsPaged(keyword, status, departmentId, offset, safePageSize);
    }

    public long countItems(String keyword, Integer status, Integer departmentId) {
        return medicalItemMapper.countItems(keyword, status, departmentId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createItem(CreateMedicalItemRequest request) {
        // 校验逻辑 (虽然Controller层有@Valid，这里可以做业务校验)
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(400, "价格不能为负数");
        }

        // 执行插入
        int rows = medicalItemMapper.insertItem(request);
        if (rows == 0) {
            throw new BusinessException(500, "新增项目失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateItem(Integer itemId, UpdateMedicalItemRequest request) {
        if (request.getPrice() != null && request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(400, "价格不能为负数");
        }

        int rows = medicalItemMapper.updateItem(itemId, request);
        if (rows == 0) {
            throw new BusinessException(500, "更新项目失败，项目可能不存在");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Integer itemId) {
        int rows = medicalItemMapper.toggleStatus(itemId);
        if (rows == 0) {
            throw new BusinessException(500, "切换状态失败，项目可能不存在");
        }
    }

    public String generateMedicalItemCode(String type) {
        if (type == null || type.isBlank()) {
            throw new BusinessException(400, "type不能为空");
        }

        String normalized = type.trim().toUpperCase();
        String prefix;
        switch (normalized) {
            case "EXAM" -> prefix = "EX";
            case "LAB" -> prefix = "LAB";
            case "DISPOSAL" -> prefix = "DISP";
            default -> throw new BusinessException(400, "不支持的项目类型: " + type);
        }

        Integer maxSeq = medicalItemMapper.getMaxMedicalItemSeq(normalized, prefix);
        int next = (maxSeq == null ? 1 : maxSeq + 1);
        if (next > 999) {
            throw new BusinessException(500, "编码序号已超过999，prefix=" + prefix);
        }
        return prefix + String.format("%03d", next);
    }
}