package com.hr.controller.admin;

import com.hr.common.Response;
import com.hr.model.dto.PageResult;
import com.hr.model.entity.WarningRule;
import com.hr.service.WarningRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 规则管理控制器
 * 提供预警规则配置和管理的API接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/admin/rules")
public class RuleAdminController extends AdminBaseController {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RuleAdminController.class);
    
    @Autowired
    private WarningRuleService warningRuleService;
    
    /**
     * 获取预警规则分页列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param keyword 关键词搜索
     * @param ruleType 规则类型筛选
     * @param status 状态筛选
     * @return 预警规则分页列表
     */
    @GetMapping
    public Response<PageResult<WarningRule>> getRuleList(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String ruleType,
            @RequestParam(required = false) String status) {
        try {
            PageResult<WarningRule> result = warningRuleService.getRulePage(pageNum, pageSize, keyword, ruleType, status);
            return success(result);
        } catch (Exception e) {
            log.error("获取预警规则列表失败: ", e);
            return error(500, "获取规则列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取预警规则详情
     * 
     * @param id 规则ID
     * @return 预警规则详情
     */
    @GetMapping("/{id}")
    public Response<WarningRule> getRuleById(@PathVariable Long id) {
        try {
            WarningRule rule = warningRuleService.getById(id);
            if (rule != null) {
                return success(rule);
            } else {
                return error(404, "规则不存在");
            }
        } catch (Exception e) {
            log.error("获取预警规则详情失败: id={}", id, e);
            return error(500, "获取规则详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建预警规则
     * 
     * @param rule 预警规则信息
     * @return 创建结果
     */
    @PostMapping
    public Response<WarningRule> createRule(@RequestBody WarningRule rule) {
        try {
            // 设置默认值
            rule.setEffective(1); // 默认生效
            rule.setCreateTime(java.time.LocalDateTime.now());
            rule.setUpdateTime(java.time.LocalDateTime.now());
            rule.setVersion(1); // 初始版本
            
            WarningRule savedRule = warningRuleService.saveRule(rule);
            return success(savedRule);
        } catch (Exception e) {
            log.error("创建预警规则失败: ", e);
            return error(500, "创建规则失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新预警规则
     * 
     * @param id 规则ID
     * @param rule 预警规则信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Response<WarningRule> updateRule(@PathVariable Long id, @RequestBody WarningRule rule) {
        try {
            // 验证规则是否存在
            WarningRule existingRule = warningRuleService.getById(id);
            if (existingRule == null) {
                return error(404, "规则不存在");
            }
            
            // 设置更新信息
            rule.setId(id);
            rule.setUpdateTime(java.time.LocalDateTime.now());
            // 版本递增
            rule.setVersion(existingRule.getVersion() + 1);
            
            WarningRule updatedRule = warningRuleService.updateRule(rule);
            return success(updatedRule);
        } catch (Exception e) {
            log.error("更新预警规则失败: id={}", id, e);
            return error(500, "更新规则失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除预警规则
     * 
     * @param id 规则ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Response<Void> deleteRule(@PathVariable Long id) {
        try {
            WarningRule rule = warningRuleService.getById(id);
            if (rule == null) {
                return error(404, "规则不存在");
            }
            
            boolean result = warningRuleService.deleteById(id);
            if (result) {
                return success();
            } else {
                return error(500, "删除规则失败");
            }
        } catch (Exception e) {
            log.error("删除预警规则失败: id={}", id, e);
            return error(500, "删除规则失败: " + e.getMessage());
        }
    }
    
    /**
     * 启用预警规则
     * 
     * @param id 规则ID
     * @return 启用结果
     */
    @PutMapping("/{id}/enable")
    public Response<Void> enableRule(@PathVariable Long id) {
        try {
            WarningRule rule = warningRuleService.getById(id);
            if (rule == null) {
                return error(404, "规则不存在");
            }
            
            rule.setEffective(1);
            rule.setUpdateTime(java.time.LocalDateTime.now());
            warningRuleService.updateById(rule);
            
            return success();
        } catch (Exception e) {
            log.error("启用预警规则失败: id={}", id, e);
            return error(500, "启用规则失败: " + e.getMessage());
        }
    }
    
    /**
     * 禁用预警规则
     * 
     * @param id 规则ID
     * @return 禁用结果
     */
    @PutMapping("/{id}/disable")
    public Response<Void> disableRule(@PathVariable Long id) {
        try {
            WarningRule rule = warningRuleService.getById(id);
            if (rule == null) {
                return error(404, "规则不存在");
            }
            
            rule.setEffective(0);
            rule.setUpdateTime(java.time.LocalDateTime.now());
            warningRuleService.updateById(rule);
            
            return success();
        } catch (Exception e) {
            log.error("禁用预警规则失败: id={}", id, e);
            return error(500, "禁用规则失败: " + e.getMessage());
        }
    }
}