package com.hr.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 数据分析实体类
 * 对应数据库表: data_analysis
 * 
 * @author HR Datacenter
 * @since 1.0.0
 */
@TableName("data_analysis")
public class DataAnalysis {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
     * 分析类型 (DESCRIPTIVE:描述性分析, PREDICTIVE:预测性分析, PRESCRIPTIVE:规范性分析)
     */
    private String analysisType;
    
    /**
     * 查询语句 (SQL/HQL)
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
     * 数据源类型 (MYSQL, HIVE)
     */
    private String dataSource;
    
    /**
     * 执行频率 (ONCE:一次性, DAILY:每日, WEEKLY:每周, MONTHLY:每月)
     */
    private String executionFrequency;
    
    /**
     * 最后执行时间
     */
    private LocalDateTime lastExecutionTime;
    
    /**
     * 下次执行时间
     */
    private LocalDateTime nextExecutionTime;
    
    /**
     * 状态 (ACTIVE:激活, INACTIVE:停用, RUNNING:运行中)
     */
    private String status;
    
    /**
     * 创建人ID
     */
    private Long createdBy;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;
    
    /**
     * 逻辑删除标识 (0:未删除, 1:已删除)
     */
    @TableLogic
    private Integer deleted;

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

    public LocalDateTime getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setLastExecutionTime(LocalDateTime lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public LocalDateTime getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(LocalDateTime nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}