package com.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.model.dto.PageResult;
import com.hr.model.entity.WarningRule;

/**
 * 预警规则服务接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
public interface WarningRuleService extends IService<WarningRule> {
    
    /**
     * 获取预警规则分页列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param keyword 关键词搜索
     * @param ruleType 规则类型筛选
     * @param status 状态筛选
     * @return 预警规则分页结果
     */
    PageResult<WarningRule> getRulePage(Long pageNum, Long pageSize, String keyword, String ruleType, String status);
    
    /**
     * 保存预警规则
     * 
     * @param rule 预警规则
     * @return 保存后的预警规则
     */
    WarningRule saveRule(WarningRule rule);
    
    /**
     * 更新预警规则
     * 
     * @param rule 预警规则
     * @return 更新后的预警规则
     */
    WarningRule updateRule(WarningRule rule);
    
    /**
     * 根据ID删除预警规则
     * 
     * @param id 规则ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);
    
    /**
     * 更新规则状态
     * 
     * @param id 规则ID
     * @param effective 状态（0-禁用，1-启用）
     * @return 是否更新成功
     */
    boolean updateStatus(Long id, Integer effective);
}