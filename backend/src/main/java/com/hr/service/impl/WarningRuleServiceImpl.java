package com.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hr.common.ErrorCode;
import com.hr.exception.BaseException;
import com.hr.mapper.WarningRuleMapper;
import com.hr.model.dto.PageResult;
import com.hr.model.entity.WarningRule;
import com.hr.service.WarningRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 预警规则服务实现类
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Service
public class WarningRuleServiceImpl extends ServiceImpl<WarningRuleMapper, WarningRule> implements WarningRuleService {
    
    @Autowired
    private WarningRuleMapper warningRuleMapper;
    
    @Override
    public PageResult<WarningRule> getRulePage(Long pageNum, Long pageSize, String keyword, String ruleType, String status) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1L;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10L;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<WarningRule> queryWrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like(WarningRule::getRuleType, keyword)
                    .or()
                    .like(WarningRule::getDescription, keyword));
        }
        
        if (ruleType != null && !ruleType.trim().isEmpty()) {
            queryWrapper.eq(WarningRule::getRuleType, ruleType);
        }
        
        if (status != null && !status.trim().isEmpty()) {
            queryWrapper.eq(WarningRule::getEffective, "1".equals(status) ? 1 : 0);
        }
        
        // 查询数据库
        Page<WarningRule> page = new Page<>(pageNum, pageSize);
        Page<WarningRule> result = this.page(page, queryWrapper);
        
        return PageResult.of(pageNum, pageSize, result.getTotal(), result.getRecords());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarningRule saveRule(WarningRule rule) {
        // 设置默认值
        rule.setCreateTime(LocalDateTime.now());
        rule.setUpdateTime(LocalDateTime.now());
        if (rule.getVersion() == null) {
            rule.setVersion(1); // 初始版本
        }
        if (rule.getEffective() == null) {
            rule.setEffective(1); // 默认生效
        }
        
        boolean result = this.save(rule);
        if (!result) {
            throw new BaseException(ErrorCode.OPERATION_ERROR, "保存规则失败");
        }
        
        return rule;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WarningRule updateRule(WarningRule rule) {
        // 获取原规则信息
        WarningRule existingRule = this.getById(rule.getId());
        if (existingRule == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "规则不存在");
        }
        
        // 设置更新时间
        rule.setUpdateTime(LocalDateTime.now());
        // 版本递增
        rule.setVersion((existingRule.getVersion() != null ? existingRule.getVersion() : 0) + 1);
        
        boolean result = this.updateById(rule);
        if (!result) {
            throw new BaseException(ErrorCode.OPERATION_ERROR, "更新规则失败");
        }
        
        return rule;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "规则ID不能为空");
        }
        
        WarningRule rule = this.getById(id);
        if (rule == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "规则不存在");
        }
        
        return this.removeById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long id, Integer effective) {
        if (id == null || id <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "规则ID不能为空");
        }
        
        if (effective == null || (effective != 0 && effective != 1)) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "状态值不合法");
        }
        
        WarningRule rule = this.getById(id);
        if (rule == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "规则不存在");
        }
        
        rule.setEffective(effective);
        rule.setUpdateTime(LocalDateTime.now());
        
        return this.updateById(rule);
    }
}