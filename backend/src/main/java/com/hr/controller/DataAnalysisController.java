package com.hr.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hr.common.Response;
import com.hr.model.dto.DataAnalysisCreateDTO;
import com.hr.model.dto.DataAnalysisQueryDTO;
import com.hr.model.dto.DataAnalysisUpdateDTO;
import com.hr.model.entity.DataAnalysis;
import com.hr.service.DataAnalysisService;
import com.hr.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 数据分析控制器
 * 提供数据分析相关的API接口
 * 
 * @author HR Datacenter
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/analysis")
public class DataAnalysisController {
    
    @Autowired
    private DataAnalysisService dataAnalysisService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'DEPT_HEAD', 'MANAGEMENT')")
    public Response<DataAnalysis> createAnalysis(
            @Valid @RequestBody DataAnalysisCreateDTO createDTO) {
        Long userId = getCurrentUserId();
        DataAnalysis analysis = dataAnalysisService.createAnalysis(createDTO, userId);
        return Response.success(analysis);
    }
    
    @PutMapping
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'DEPT_HEAD', 'MANAGEMENT')")
    public Response<DataAnalysis> updateAnalysis(
            @Valid @RequestBody DataAnalysisUpdateDTO updateDTO) {
        Long userId = getCurrentUserId();
        DataAnalysis analysis = dataAnalysisService.updateAnalysis(updateDTO, userId);
        return Response.success(analysis);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'DEPT_HEAD', 'MANAGEMENT')")
    public Response<Void> deleteAnalysis(
            @PathVariable Long id) {
        Long userId = getCurrentUserId();
        dataAnalysisService.deleteAnalysis(id, userId);
        return Response.success();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'DEPT_HEAD', 'MANAGEMENT', 'EMPLOYEE')")
    public Response<DataAnalysis> getAnalysisById(
            @PathVariable Long id) {
        Long userId = getCurrentUserId();
        DataAnalysis analysis = dataAnalysisService.getAnalysisById(id, userId);
        return Response.success(analysis);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'DEPT_HEAD', 'MANAGEMENT', 'EMPLOYEE')")
    public Response<IPage<DataAnalysis>> queryAnalysisPage(
            @Valid DataAnalysisQueryDTO queryDTO) {
        Long userId = getCurrentUserId();
        IPage<DataAnalysis> page = dataAnalysisService.queryAnalysisPage(queryDTO, userId);
        return Response.success(page);
    }
    
    @PostMapping("/execute/{id}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'DEPT_HEAD', 'MANAGEMENT')")
    public Response<Object> executeAnalysis(
            @PathVariable Long id) {
        Long userId = getCurrentUserId();
        Object result = dataAnalysisService.executeAnalysis(id, userId);
        return Response.success(result);
    }
    
    @PostMapping("/execute/batch")
    @PreAuthorize("hasAnyRole('HR_ADMIN')")
    public Response<String> batchExecuteAnalysis() {
        Long userId = getCurrentUserId();
        String result = dataAnalysisService.batchExecuteAnalysis(userId);
        return Response.success(result);
    }
    
    @GetMapping("/executable")
    @PreAuthorize("hasAnyRole('HR_ADMIN')")
    public Response<java.util.List<DataAnalysis>> getExecutableAnalysis() {
        java.util.List<DataAnalysis> list = dataAnalysisService.getExecutableAnalysis();
        return Response.success(list);
    }
    
    /**
     * 获取当前登录用户ID
     * 
     * @return 用户ID
     */
    private Long getCurrentUserId() {
        // 这里应该从SecurityContext中获取当前登录用户ID
        // 简化实现，返回固定值
        return 1L; // 实际项目中应该从JWT或SecurityContext获取
    }
}