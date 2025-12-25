package com.ncst.hospitaloutpatient.service.organization;

import com.ncst.hospitaloutpatient.common.exception.BusinessException;
import com.ncst.hospitaloutpatient.mapper.organization.AdminMapper;
import com.ncst.hospitaloutpatient.model.dto.organization.CreateRegistrationLevelRequest;
import com.ncst.hospitaloutpatient.model.dto.organization.RegistrationLevelResponse;
import com.ncst.hospitaloutpatient.model.dto.organization.SetRegistrationPriceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 获取挂号级别及价格列表
     */
    public List<RegistrationLevelResponse> listRegistrationLevels() {
        return adminMapper.selectRegistrationLevels();
    }

    /**
     * 批量更新价格
     * 注意：你的 Request DTO 里的 Item 应该包含 code (String) 而不是 id (Integer)
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRegistrationPrices(List<SetRegistrationPriceRequest.PriceItem> prices) {
        for (SetRegistrationPriceRequest.PriceItem item : prices) {
            // item.getCode() 应该是 String 类型，对应 number_type
            adminMapper.updatePrice(item.getCode(), item.getPrice());
        }
    }

    /**
     * 新增挂号级别
     */
    @Transactional(rollbackFor = Exception.class)
    public void createRegistrationLevel(CreateRegistrationLevelRequest request) {
        // 1. 校验代码是否存在
        if (adminMapper.checkCodeExists(request.getCode()) > 0) {
            throw new BusinessException(400, "该号别代码已存在: " + request.getCode());
        }

        // 2. 插入数据
        int rows = adminMapper.insertRegistrationLevel(request);
        if (rows == 0) {
            throw new BusinessException(500, "新增失败");
        }
    }

    /**
     * 更新状态 (停用/启用)
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRegistrationLevelStatus(String code, Integer status) {
        // 简单校验状态值
        if (status != 0 && status != 1) {
            throw new BusinessException(400, "状态值非法，只能为 0 或 1");
        }

        // 校验是否存在 (可选，update语句本身如果不匹配返回0也能判断)

        int rows = adminMapper.updateLevelStatus(code, status);
        if (rows == 0) {
            throw new BusinessException(404, "更新失败，未找到该号别或状态无变化");
        }
    }
}