package com.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hr.common.ErrorCode;
import com.hr.exception.BaseException;
import com.hr.mapper.ReportTemplateMapper;
import com.hr.model.dto.PageResult;
import com.hr.model.entity.ReportTemplate;
import com.hr.service.ReportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 报表模板服务实现类
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Service
public class ReportTemplateServiceImpl extends ServiceImpl<ReportTemplateMapper, ReportTemplate> implements ReportTemplateService {
    
    @Autowired
    private ReportTemplateMapper reportTemplateMapper;
    
    @Override
    public PageResult<ReportTemplate> getTemplatePage(Long pageNum, Long pageSize, String keyword, String category, String status) {
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1L;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10L;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<ReportTemplate> queryWrapper = new LambdaQueryWrapper<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like(ReportTemplate::getName, keyword)
                    .or()
                    .like(ReportTemplate::getDescription, keyword));
        }
        
        if (category != null && !category.trim().isEmpty()) {
            queryWrapper.eq(ReportTemplate::getCategory, category);
        }
        
        if (status != null && !status.trim().isEmpty()) {
            queryWrapper.eq(ReportTemplate::getEnabled, "1".equals(status) ? 1 : 0);
        }
        
        // 查询数据库
        Page<ReportTemplate> page = new Page<>(pageNum, pageSize);
        Page<ReportTemplate> result = this.page(page, queryWrapper);
        
        return PageResult.of(pageNum, pageSize, result.getTotal(), result.getRecords());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReportTemplate saveTemplate(ReportTemplate template) {
        // 设置默认值
        template.setCreateTime(LocalDateTime.now());
        template.setUpdateTime(LocalDateTime.now());
        if (template.getEnabled() == null) {
            template.setEnabled(1); // 默认启用
        }
        
        boolean result = this.save(template);
        if (!result) {
            throw new BaseException(ErrorCode.OPERATION_ERROR, "保存报表模板失败");
        }
        
        return template;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReportTemplate updateTemplate(ReportTemplate template) {
        // 获取原模板信息
        ReportTemplate existingTemplate = this.getById(template.getId());
        if (existingTemplate == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "报表模板不存在");
        }
        
        // 设置更新时间
        template.setUpdateTime(LocalDateTime.now());
        
        boolean result = this.updateById(template);
        if (!result) {
            throw new BaseException(ErrorCode.OPERATION_ERROR, "更新报表模板失败");
        }
        
        return template;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "模板ID不能为空");
        }
        
        ReportTemplate template = this.getById(id);
        if (template == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "报表模板不存在");
        }
        
        return this.removeById(id);
    }
    
    @Override
    public String generateReport(Long templateId, Map<String, Object> params) {
        if (templateId == null || templateId <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "模板ID不能为空");
        }
        
        ReportTemplate template = this.getById(templateId);
        if (template == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "报表模板不存在");
        }
        
        if (template.getEnabled() != 1) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "报表模板已被禁用");
        }
        
        // 模拟报表生成过程
        // 在实际应用中，这里会执行数据库查询、数据处理、报表生成等操作
        
        // 生成报表文件名
        String reportFileName = "report_" + templateId + "_" + System.currentTimeMillis() + ".xlsx";
        
        // 返回报表访问URL（模拟）
        return "/reports/" + reportFileName;
    }
    
    @Override
    public List<String> generateBatchReports(Long templateId, List<Map<String, Object>> batchParams) {
        if (templateId == null || templateId <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "模板ID不能为空");
        }
        
        if (batchParams == null || batchParams.isEmpty()) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "批量参数不能为空");
        }
        
        ReportTemplate template = this.getById(templateId);
        if (template == null) {
            throw new BaseException(ErrorCode.NOT_FOUND, "报表模板不存在");
        }
        
        if (template.getEnabled() != 1) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "报表模板已被禁用");
        }
        
        // 模拟批量报表生成过程
        java.util.ArrayList<String> reportUrls = new java.util.ArrayList<>();
        
        for (Map<String, Object> params : batchParams) {
            // 生成报表文件名
            String reportFileName = "report_" + templateId + "_" + UUID.randomUUID().toString() + ".xlsx";
            reportUrls.add("/reports/" + reportFileName);
        }
        
        return reportUrls;
    }
}