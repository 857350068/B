package com.hr.model.dto;

import lombok.Data;
import java.util.List;

/**
 * 图表数据响应 VO
 * 用于ECharts图表数据接口返回
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Data
public class ChartDataVO {
    
    /**
     * 图表标题
     */
    private String title;
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * 图表类型（bar/line/pie）
     */
    private String chartType;
    
    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
    
    /**
     * X轴数据（类别轴）
     */
    private List<String> xAxisData;
    
    public void setXAxisData(List<String> xAxisData) {
        this.xAxisData = xAxisData;
    }
    
    /**
     * Y轴数据（数值轴）
     */
    private List<ChartSeries> series;
    
    public void setSeries(List<ChartSeries> series) {
        this.series = series;
    }
    
    /**
     * 图例数据
     */
    private List<String> legendData;
    
    public void setLegendData(List<String> legendData) {
        this.legendData = legendData;
    }
    
    /**
     * 数据分类ID
     */
    private Long categoryId;
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * 统计维度
     */
    private String dimension;
    
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
    
    /**
     * 统计周期
     */
    private String period;
    
    public void setPeriod(String period) {
        this.period = period;
    }
    
    /**
     * 图表配置选项
     */
    private ChartOptions options;
    
    public void setOptions(ChartOptions options) {
        this.options = options;
    }
    
    /**
     * 图表系列数据类
     */
    @Data
    public static class ChartSeries {
        /**
         * 系列名称
         */
        private String name;
        
        public void setName(String name) {
            this.name = name;
        }
        
        /**
         * 系列类型
         */
        private String type;
        
        public void setType(String type) {
            this.type = type;
        }
        
        /**
         * 数据值
         */
        private List<Object> data;
        
        public void setData(List<Object> data) {
            this.data = data;
        }
        
        /**
         * 同比数据
         */
        private List<Object> yearOnYearData;
        
        /**
         * 环比数据
         */
        private List<Object> monthOnMonthData;
    }
    
    /**
     * 图表配置选项
     */
    @Data
    public static class ChartOptions {
        /**
         * 是否显示图例
         */
        private Boolean showLegend = true;
        
        public void setShowLegend(Boolean showLegend) {
            this.showLegend = showLegend;
        }
        
        /**
         * 是否显示工具提示
         */
        private Boolean showTooltip = true;
        
        public void setShowTooltip(Boolean showTooltip) {
            this.showTooltip = showTooltip;
        }
        
        /**
         * 是否显示数据标签
         */
        private Boolean showLabel = false;
        
        public void setShowLabel(Boolean showLabel) {
            this.showLabel = showLabel;
        }
        
        /**
         * 图表高度
         */
        private String height = "400px";
        
        public void setHeight(String height) {
            this.height = height;
        }
        
        /**
         * 颜色主题
         */
        private List<String> colors;
        
        public void setColors(List<String> colors) {
            this.colors = colors;
        }
    }
}