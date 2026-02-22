package com.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hr.common.ErrorCode;
import com.hr.exception.BaseException;
import com.hr.mapper.DataAnalysisMapper;
import com.hr.model.dto.ChartDataVO;
import com.hr.model.entity.DataAnalysis;
import com.hr.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 图表服务实现类
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Slf4j
@Service
public class ChartServiceImpl implements ChartService {
    
    @Autowired
    private DataAnalysisMapper dataAnalysisMapper;
    
    @Override
    public ChartDataVO generateChartData(Long categoryId, String chartType, String dimension, String period, Long deptId) {
        // 验证参数
        validateChartParams(categoryId, chartType, dimension, period);
        
        ChartDataVO chartData = new ChartDataVO();
        chartData.setTitle(getChartTitle(categoryId, dimension, period));
        chartData.setChartType(chartType);
        chartData.setCategoryId(categoryId);
        chartData.setDimension(dimension);
        chartData.setPeriod(period);
        chartData.setOptions(getDefaultChartOptions(chartType));
        
        // 生成模拟数据（实际项目中应从Hive查询）
        generateMockChartData(chartData, chartType, dimension);
        
        return chartData;
    }
    
    @Override
    public List<ChartDataVO> generateTimeSeriesData(Long categoryId, String dimension, String timeRange, Long deptId) {
        List<ChartDataVO> result = new ArrayList<>();
        
        // 生成同比数据图表
        ChartDataVO yearOnYearChart = new ChartDataVO();
        yearOnYearChart.setTitle("同比分析");
        yearOnYearChart.setChartType("line");
        yearOnYearChart.setCategoryId(categoryId);
        yearOnYearChart.setDimension(dimension);
        yearOnYearChart.setPeriod(timeRange);
        yearOnYearChart.setOptions(getDefaultChartOptions("line"));
        generateTimeSeriesMockData(yearOnYearChart, "同比");
        result.add(yearOnYearChart);
        
        // 生成环比数据图表
        ChartDataVO monthOnMonthChart = new ChartDataVO();
        monthOnMonthChart.setTitle("环比分析");
        monthOnMonthChart.setChartType("line");
        monthOnMonthChart.setCategoryId(categoryId);
        monthOnMonthChart.setDimension(dimension);
        monthOnMonthChart.setPeriod(timeRange);
        monthOnMonthChart.setOptions(getDefaultChartOptions("line"));
        generateTimeSeriesMockData(monthOnMonthChart, "环比");
        result.add(monthOnMonthChart);
        
        return result;
    }
    
    @Override
    public List<ChartDataVO> generateCompareData(Long categoryId, String[] dimensions, String period, Long deptId) {
        List<ChartDataVO> result = new ArrayList<>();
        
        for (String dimension : dimensions) {
            ChartDataVO chartData = new ChartDataVO();
            chartData.setTitle(dimension + "对比分析");
            chartData.setChartType("bar");
            chartData.setCategoryId(categoryId);
            chartData.setDimension(dimension);
            chartData.setPeriod(period);
            chartData.setOptions(getDefaultChartOptions("bar"));
            generateMockChartData(chartData, "bar", dimension);
            result.add(chartData);
        }
        
        return result;
    }
    
    @Override
    public ChartDataVO.ChartOptions getChartOptions(String chartType) {
        return getDefaultChartOptions(chartType);
    }
    
    // 私有辅助方法
    
    private void validateChartParams(Long categoryId, String chartType, String dimension, String period) {
        if (categoryId == null || categoryId <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "数据分类ID不能为空");
        }
        
        List<String> validChartTypes = Arrays.asList("bar", "line", "pie");
        if (!validChartTypes.contains(chartType)) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "不支持的图表类型: " + chartType);
        }
        
        List<String> validDimensions = Arrays.asList("dept", "job", "time");
        if (!validDimensions.contains(dimension)) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "不支持的统计维度: " + dimension);
        }
        
        List<String> validPeriods = Arrays.asList("day", "week", "month", "quarter", "year");
        if (!validPeriods.contains(period)) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "不支持的统计周期: " + period);
        }
    }
    
    private String getChartTitle(Long categoryId, String dimension, String period) {
        // 根据分类ID获取分类名称（简化处理）
        String categoryName = getCategoryName(categoryId);
        String dimensionName = getDimensionName(dimension);
        String periodName = getPeriodName(period);
        
        return categoryName + dimensionName + periodName + "分析";
    }
    
    private String getCategoryName(Long categoryId) {
        // 实际项目中应查询数据库获取分类名称
        switch (categoryId.intValue()) {
            case 1: return "组织效能";
            case 2: return "人才梯队";
            case 3: return "薪酬福利";
            case 4: return "绩效管理";
            case 5: return "员工流失";
            case 6: return "培训效果";
            case 7: return "人力成本";
            case 8: return "人才发展";
            default: return "数据分析";
        }
    }
    
    private String getDimensionName(String dimension) {
        switch (dimension) {
            case "dept": return "部门";
            case "job": return "岗位";
            case "time": return "时间";
            default: return "";
        }
    }
    
    private String getPeriodName(String period) {
        switch (period) {
            case "day": return "日";
            case "week": return "周";
            case "month": return "月";
            case "quarter": return "季度";
            case "year": return "年";
            default: return "";
        }
    }
    
    private ChartDataVO.ChartOptions getDefaultChartOptions(String chartType) {
        ChartDataVO.ChartOptions options = new ChartDataVO.ChartOptions();
        options.setShowLegend(true);
        options.setShowTooltip(true);
        options.setShowLabel(false);
        options.setHeight("400px");
        
        // 设置默认颜色主题
        List<String> colors = Arrays.asList(
            "#1890ff", "#52c41a", "#faad14", "#f5222d", 
            "#722ed1", "#13c2c2", "#eb2f96", "#fa8c16"
        );
        options.setColors(colors);
        
        return options;
    }
    
    private void generateMockChartData(ChartDataVO chartData, String chartType, String dimension) {
        List<String> xAxisData = new ArrayList<>();
        List<ChartDataVO.ChartSeries> seriesList = new ArrayList<>();
            
        // 生成X轴数据
        if ("dept".equals(dimension)) {
            xAxisData.addAll(Arrays.asList("销售部", "研发部", "人事部", "财务部", "市场部"));
        } else if ("job".equals(dimension)) {
            xAxisData.addAll(Arrays.asList("经理", "主管", "专员", "助理", "实习生"));
        } else if ("time".equals(dimension)) {
            xAxisData.addAll(Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月"));
        }
            
        // 生成系列数据
        ChartDataVO.ChartSeries series = new ChartDataVO.ChartSeries();
        series.setName("数据值");
        series.setType(chartType);
            
        List<Object> data = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < xAxisData.size(); i++) {
            // 生成模拟数据
            BigDecimal value = BigDecimal.valueOf(random.nextDouble() * 100).setScale(2, BigDecimal.ROUND_HALF_UP);
            data.add(value);
        }
        series.setData(data);
        seriesList.add(series);
            
        // 为饼图添加占比数据
        if ("pie".equals(chartType)) {
            BigDecimal total = data.stream()
                .map(obj -> (BigDecimal) obj)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
            List<Object> ratioData = new ArrayList<>();
            for (Object obj : data) {
                BigDecimal value = (BigDecimal) obj;
                BigDecimal ratio = total.compareTo(BigDecimal.ZERO) > 0 ? 
                    value.divide(total, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)) : 
                    BigDecimal.ZERO;
                ratioData.add(ratio.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
                
            ChartDataVO.ChartSeries ratioSeries = new ChartDataVO.ChartSeries();
            ratioSeries.setName("占比");
            ratioSeries.setType("pie");
            ratioSeries.setData(ratioData);
            seriesList.add(ratioSeries);
        }
            
        chartData.setXAxisData(xAxisData);
        chartData.setSeries(seriesList);
        chartData.setLegendData(Arrays.asList("数据值", "占比"));
    }
    
    private void generateTimeSeriesMockData(ChartDataVO chartData, String dataType) {
        List<String> xAxisData = Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月");
        List<ChartDataVO.ChartSeries> seriesList = new ArrayList<>();
        
        ChartDataVO.ChartSeries series = new ChartDataVO.ChartSeries();
        series.setName(dataType);
        series.setType("line");
        
        List<Object> data = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < xAxisData.size(); i++) {
            BigDecimal value;
            if ("同比".equals(dataType)) {
                // 同比数据：正负都有
                value = BigDecimal.valueOf((random.nextDouble() - 0.5) * 50).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                // 环比数据：相对平缓
                value = BigDecimal.valueOf((random.nextDouble() - 0.3) * 30).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            data.add(value);
        }
        series.setData(data);
        seriesList.add(series);
        
        chartData.setXAxisData(xAxisData);
        chartData.setSeries(seriesList);
        chartData.setLegendData(Arrays.asList(dataType));
    }
}