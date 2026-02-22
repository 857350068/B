package com.hr.controller.admin;

import com.hr.common.Response;
import com.hr.model.dto.PageResult;
import com.hr.model.entity.ReportTemplate;
import com.hr.service.ReportTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报表管理控制器
 * 提供报表模板管理和报表生成的API接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/admin/reports")
public class ReportAdminController extends AdminBaseController {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReportAdminController.class);
    
    @Autowired
    private ReportTemplateService reportTemplateService;
    
    /**
     * 获取报表模板分页列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param keyword 关键词搜索
     * @param category 分类筛选
     * @param status 状态筛选
     * @return 报表模板分页列表
     */
    @GetMapping("/templates")
    public Response<PageResult<ReportTemplate>> getTemplateList(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        try {
            PageResult<ReportTemplate> result = reportTemplateService.getTemplatePage(pageNum, pageSize, keyword, category, status);
            return success(result);
        } catch (Exception e) {
            log.error("获取报表模板列表失败: ", e);
            return error(500, "获取报表模板列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取报表模板详情
     * 
     * @param id 模板ID
     * @return 报表模板详情
     */
    @GetMapping("/templates/{id}")
    public Response<ReportTemplate> getTemplateById(@PathVariable Long id) {
        try {
            ReportTemplate template = reportTemplateService.getById(id);
            if (template != null) {
                return success(template);
            } else {
                return error(404, "报表模板不存在");
            }
        } catch (Exception e) {
            log.error("获取报表模板详情失败: id={}", id, e);
            return error(500, "获取报表模板详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建报表模板
     * 
     * @param template 报表模板信息
     * @return 创建结果
     */
    @PostMapping("/templates")
    public Response<ReportTemplate> createTemplate(@RequestBody ReportTemplate template) {
        try {
            ReportTemplate savedTemplate = reportTemplateService.saveTemplate(template);
            return success(savedTemplate);
        } catch (Exception e) {
            log.error("创建报表模板失败: ", e);
            return error(500, "创建报表模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新报表模板
     * 
     * @param id 模板ID
     * @param template 报表模板信息
     * @return 更新结果
     */
    @PutMapping("/templates/{id}")
    public Response<ReportTemplate> updateTemplate(@PathVariable Long id, @RequestBody ReportTemplate template) {
        try {
            template.setId(id);
            ReportTemplate updatedTemplate = reportTemplateService.updateTemplate(template);
            return success(updatedTemplate);
        } catch (Exception e) {
            log.error("更新报表模板失败: id={}", id, e);
            return error(500, "更新报表模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除报表模板
     * 
     * @param id 模板ID
     * @return 删除结果
     */
    @DeleteMapping("/templates/{id}")
    public Response<Void> deleteTemplate(@PathVariable Long id) {
        try {
            boolean result = reportTemplateService.deleteById(id);
            if (result) {
                return success();
            } else {
                return error(500, "删除报表模板失败");
            }
        } catch (Exception e) {
            log.error("删除报表模板失败: id={}", id, e);
            return error(500, "删除报表模板失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成报表
     * 
     * @param templateId 模板ID
     * @param params 报表参数
     * @return 生成结果
     */
    @PostMapping("/generate")
    public Response<String> generateReport(@RequestParam Long templateId, @RequestBody(required = false) java.util.Map<String, Object> params) {
        try {
            String reportUrl = reportTemplateService.generateReport(templateId, params);
            return success(reportUrl);
        } catch (Exception e) {
            log.error("生成报表失败: templateId={}", templateId, e);
            return error(500, "生成报表失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量生成报表
     * 
     * @param templateId 模板ID
     * @param batchParams 批量参数列表
     * @return 生成结果
     */
    @PostMapping("/generate-batch")
    public Response<java.util.List<String>> generateBatchReports(@RequestParam Long templateId, @RequestBody java.util.List<java.util.Map<String, Object>> batchParams) {
        try {
            java.util.List<String> reportUrls = reportTemplateService.generateBatchReports(templateId, batchParams);
            return success(reportUrls);
        } catch (Exception e) {
            log.error("批量生成报表失败: templateId={}", templateId, e);
            return error(500, "批量生成报表失败: " + e.getMessage());
        }
    }
}