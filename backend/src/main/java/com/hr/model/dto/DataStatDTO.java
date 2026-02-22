package com.hr.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据统计响应 DTO
 * 用于图表数据接口返回
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Data
public class DataStatDTO {
    
    /**
     * 统计维度值（如部门名称、岗位名称、时间等）
     */
    private String dimensionValue;
    
    /**
     * 统计数值
     */
    private BigDecimal value;
    
    /**
     * 统计时间
     */
    private LocalDateTime statTime;
    
    /**
     * 统计类型（COUNT/SUM/AVG/MAX/MIN）
     */
    private String statType;
    
    /**
     * 数据分类ID
     */
    private Long categoryId;
    
    /**
     * 部门ID
     */
    private Long deptId;
    
    /**
     * 岗位
     */
    private String job;
    
    /**
     * 统计周期（DAY/WEEK/MONTH/QUARTER/YEAR）
     */
    private String period;
    
    /**
     * 同比数据
     */
    private BigDecimal yearOnYear;
    
    /**
     * 环比数据
     */
    private BigDecimal monthOnMonth;
    
    /**
     * 占比（用于饼图）
     */
    private BigDecimal ratio;
}