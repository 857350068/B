package com.hr.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据分析创建DTO
 * 用于创建数据分析任务的请求参数
 * 
 * @author HR Datacenter
 * @since 1.0.0
 */
public class DataAnalysisCreateDTO {
    
    /**
     * 数据分类ID
     */
    @NotNull(message = "数据分类ID不能为空")
    private Long categoryId;
    
    /**
     * 分析名称
     */
    @NotBlank(message = "分析名称不能为空")
    private String analysisName;
    
    /**
     * 分析描述
     */
    private String description;
    
    /**
     * 分析类型
     */
    @NotBlank(message = "分析类型不能为空")
    private String analysisType;
    
    /**
     * 查询语句
     */
    @NotBlank(message = "查询语句不能为空")
    private String queryStatement;
    
    /**
     * 参数配置 (JSON格式)
     */
    private String parameters;
    
    /**
     * 结果字段映射 (JSON格式)
     */
    private String resultMapping;
    
    /**
     * 图表配置 (JSON格式)
     */
    private String chartConfig;
    
    /**
     * 数据源类型
     */
    @NotBlank(message = "数据源类型不能为空")
    private String dataSource;
    
    /**
     * 执行频率
     */
    private String executionFrequency;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public String getQueryStatement() {
        return queryStatement;
    }

    public void setQueryStatement(String queryStatement) {
        this.queryStatement = queryStatement;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResultMapping() {
        return resultMapping;
    }

    public void setResultMapping(String resultMapping) {
        this.resultMapping = resultMapping;
    }

    public String getChartConfig() {
        return chartConfig;
    }

    public void setChartConfig(String chartConfig) {
        this.chartConfig = chartConfig;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getExecutionFrequency() {
        return executionFrequency;
    }

    public void setExecutionFrequency(String executionFrequency) {
        this.executionFrequency = executionFrequency;
    }
}