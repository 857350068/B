package com.hr.service;

import com.hr.model.dto.ChartDataVO;
import java.util.List;

/**
 * 图表服务接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
public interface ChartService {
    
    /**
     * 生成图表数据
     * 
     * @param categoryId 数据分类ID
     * @param chartType 图表类型 (bar/line/pie)
     * @param dimension 统计维度 (dept/job/time)
     * @param period 统计周期 (day/week/month/quarter/year)
     * @param deptId 部门ID（可选）
     * @return 图表数据
     */
    ChartDataVO generateChartData(Long categoryId, String chartType, String dimension, String period, Long deptId);
    
    /**
     * 生成分时数据（用于同环比分析）
     * 
     * @param categoryId 数据分类ID
     * @param dimension 统计维度
     * @param timeRange 时间范围
     * @param deptId 部门ID（可选）
     * @return 分时数据列表
     */
    List<ChartDataVO> generateTimeSeriesData(Long categoryId, String dimension, String timeRange, Long deptId);
    
    /**
     * 生成多维度对比数据
     * 
     * @param categoryId 数据分类ID
     * @param dimensions 统计维度数组
     * @param period 统计周期
     * @param deptId 部门ID（可选）
     * @return 对比数据列表
     */
    List<ChartDataVO> generateCompareData(Long categoryId, String[] dimensions, String period, Long deptId);
    
    /**
     * 获取图表配置选项
     * 
     * @param chartType 图表类型
     * @return 配置选项
     */
    ChartDataVO.ChartOptions getChartOptions(String chartType);
}