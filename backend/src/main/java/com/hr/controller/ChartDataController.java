package com.hr.controller;

import com.hr.common.Response;
import com.hr.model.dto.ChartDataVO;
import com.hr.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 图表数据控制器
 * 提供图表数据API接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/chart")
public class ChartDataController {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ChartDataController.class);
    
    @Autowired
    private ChartService chartService;
    
    /**
     * 获取图表数据
     * 
     * @param categoryId 数据分类ID
     * @param chartType 图表类型 (bar/line/pie)
     * @param dimension 统计维度 (dept/job/time)
     * @param period 统计周期 (day/week/month/quarter/year)
     * @param deptId 部门ID（可选）
     * @return 图表数据
     */
    @GetMapping("/data")
    public Response<ChartDataVO> getChartData(
            @RequestParam @NotNull Long categoryId,
            @RequestParam(defaultValue = "bar") String chartType,
            @RequestParam(defaultValue = "dept") String dimension,
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(required = false) Long deptId) {
        
        try {
            ChartDataVO chartData = chartService.generateChartData(categoryId, chartType, dimension, period, deptId);
            return Response.success(chartData);
        } catch (Exception e) {
            log.error("获取图表数据失败: categoryId={}, chartType={}, dimension={}, period={}", 
                     categoryId, chartType, dimension, period, e);
            return Response.error(500, "获取图表数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取分时数据（用于同环比分析）
     * 
     * @param categoryId 数据分类ID
     * @param dimension 统计维度
     * @param timeRange 时间范围 (recent7days/recent30days/recent12months)
     * @param deptId 部门ID（可选）
     * @return 分时数据
     */
    @GetMapping("/timeseries")
    public Response<List<ChartDataVO>> getTimeSeriesData(
            @RequestParam @NotNull Long categoryId,
            @RequestParam(defaultValue = "time") String dimension,
            @RequestParam(defaultValue = "recent30days") String timeRange,
            @RequestParam(required = false) Long deptId) {
        
        try {
            List<ChartDataVO> timeSeriesData = chartService.generateTimeSeriesData(categoryId, dimension, timeRange, deptId);
            return Response.success(timeSeriesData);
        } catch (Exception e) {
            log.error("获取分时数据失败: categoryId={}, dimension={}, timeRange={}", 
                     categoryId, dimension, timeRange, e);
            return Response.error(500, "获取分时数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取多维度对比数据
     * 
     * @param categoryId 数据分类ID
     * @param dimensions 多个统计维度（逗号分隔）
     * @param period 统计周期
     * @param deptId 部门ID（可选）
     * @return 对比数据
     */
    @GetMapping("/compare")
    public Response<List<ChartDataVO>> getCompareData(
            @RequestParam @NotNull Long categoryId,
            @RequestParam String dimensions,
            @RequestParam(defaultValue = "month") String period,
            @RequestParam(required = false) Long deptId) {
        
        try {
            String[] dimensionArray = dimensions.split(",");
            List<ChartDataVO> compareData = chartService.generateCompareData(categoryId, dimensionArray, period, deptId);
            return Response.success(compareData);
        } catch (Exception e) {
            log.error("获取对比数据失败: categoryId={}, dimensions={}, period={}", 
                     categoryId, dimensions, period, e);
            return Response.error(500, "获取对比数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取图表配置选项
     * 
     * @param chartType 图表类型
     * @return 配置选项
     */
    @GetMapping("/options/{chartType}")
    public Response<ChartDataVO.ChartOptions> getChartOptions(
            @PathVariable String chartType) {
        
        try {
            ChartDataVO.ChartOptions options = chartService.getChartOptions(chartType);
            return Response.success(options);
        } catch (Exception e) {
            log.error("获取图表配置失败: chartType={}", chartType, e);
            return Response.error(500, "获取图表配置失败: " + e.getMessage());
        }
    }
}