package com.hr.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据分析更新DTO
 * 用于更新数据分析任务的请求参数
 * 
 * @author HR Datacenter
 * @since 1.0.0
 */
public class DataAnalysisUpdateDTO {
    
    /**
     * 主键ID
     */
    @NotNull(message = "分析ID不能为空")
    private Long id;
    
    /**
     * 数据分类ID
     */
    private Long categoryId;
    
    /**
     * 分析名称
     */
    private String analysisName;
    
    /**
     * 分析描述
     */
    private String description;
    
    /**
     * 分析类型
     */
    private String analysisType;
    
    /**
     * 查询语句
     */
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
    private String dataSource;
    
    /**
     * 执行频率
     */
    private String executionFrequency;
    
    /**
     * 状态
     */
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}