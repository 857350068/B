package com.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.model.dto.PageResult;
import com.hr.model.entity.ReportTemplate;

import java.util.List;
import java.util.Map;

/**
 * 报表模板服务接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
public interface ReportTemplateService extends IService<ReportTemplate> {
    
    /**
     * 获取报表模板分页列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param keyword 关键词搜索
     * @param category 分类筛选
     * @param status 状态筛选
     * @return 报表模板分页结果
     */
    PageResult<ReportTemplate> getTemplatePage(Long pageNum, Long pageSize, String keyword, String category, String status);
    
    /**
     * 保存报表模板
     * 
     * @param template 报表模板
     * @return 保存后的报表模板
     */
    ReportTemplate saveTemplate(ReportTemplate template);
    
    /**
     * 更新报表模板
     * 
     * @param template 报表模板
     * @return 更新后的报表模板
     */
    ReportTemplate updateTemplate(ReportTemplate template);
    
    /**
     * 根据ID删除报表模板
     * 
     * @param id 模板ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);
    
    /**
     * 生成报表
     * 
     * @param templateId 模板ID
     * @param params 报表参数
     * @return 生成的报表URL
     */
    String generateReport(Long templateId, Map<String, Object> params);
    
    /**
     * 批量生成报表
     * 
     * @param templateId 模板ID
     * @param batchParams 批量参数列表
     * @return 生成的报表URL列表
     */
    List<String> generateBatchReports(Long templateId, List<Map<String, Object>> batchParams);
}