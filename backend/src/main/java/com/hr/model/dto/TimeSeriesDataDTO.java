package com.hr.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分时数据分析DTO
 * 用于同/环比数据输出
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Data
public class TimeSeriesDataDTO {
    
    /**
     * 时间点
     */
    private LocalDateTime timePoint;
    
    /**
     * 当前值
     */
    private BigDecimal currentValue;
    
    /**
     * 同期值（去年同期）
     */
    private BigDecimal yearOnYearValue;
    
    /**
     * 环比值（上期）
     */
    private BigDecimal monthOnMonthValue;
    
    /**
     * 同比增长率 (%)
     */
    private BigDecimal yearOnYearGrowth;
    
    /**
     * 环比增长率 (%)
     */
    private BigDecimal monthOnMonthGrowth;
    
    /**
     * 统计维度
     */
    private String dimension;
    
    /**
     * 维度值
     */
    private String dimensionValue;
    
    /**
     * 数据分类ID
     */
    private Long categoryId;
    
    /**
     * 部门ID
     */
    private Long deptId;
    
    /**
     * 统计周期
     */
    private String period;
    
    /**
     * 数据来源表
     */
    private String sourceTable;
    
    /**
     * 计算时间
     */
    private LocalDateTime calculateTime;
}