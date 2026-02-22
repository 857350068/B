package com.hr.model.dto;

/**
 * 数据分析查询DTO
 * 用于查询数据分析任务的请求参数
 * 
 * @author HR Datacenter
 * @since 1.0.0
 */
public class DataAnalysisQueryDTO {
    
    /**
     * 数据分类ID
     */
    private Long categoryId;
    
    /**
     * 分析名称（模糊查询）
     */
    private String analysisName;
    
    /**
     * 分析类型
     */
    private String analysisType;
    
    /**
     * 数据源类型
     */
    private String dataSource;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 创建人ID
     */
    private Long createdBy;
    
    /**
     * 当前页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 10;

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

    public String getAnalysisType() {
        return analysisType;
    }

    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}