<template>
  <div class="charts-container">
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span>数据可视化图表</span>
          <div class="chart-controls">
            <el-select 
              v-model="queryParams.categoryId" 
              placeholder="选择数据分类" 
              style="width: 150px"
              @change="loadChartData"
            >
              <el-option label="组织效能" :value="1" />
              <el-option label="人才梯队" :value="2" />
              <el-option label="薪酬福利" :value="3" />
              <el-option label="绩效管理" :value="4" />
              <el-option label="员工流失" :value="5" />
              <el-option label="培训效果" :value="6" />
              <el-option label="人力成本" :value="7" />
              <el-option label="人才发展" :value="8" />
            </el-select>
            
            <el-select 
              v-model="queryParams.chartType" 
              placeholder="图表类型" 
              style="width: 120px"
              @change="loadChartData"
            >
              <el-option label="柱状图" value="bar" />
              <el-option label="折线图" value="line" />
              <el-option label="饼图" value="pie" />
            </el-select>
            
            <el-select 
              v-model="queryParams.dimension" 
              placeholder="统计维度" 
              style="width: 120px"
              @change="loadChartData"
            >
              <el-option label="部门" value="dept" />
              <el-option label="岗位" value="job" />
              <el-option label="时间" value="time" />
            </el-select>
            
            <el-select 
              v-model="queryParams.period" 
              placeholder="统计周期" 
              style="width: 120px"
              @change="loadChartData"
            >
              <el-option label="日" value="day" />
              <el-option label="周" value="week" />
              <el-option label="月" value="month" />
              <el-option label="季度" value="quarter" />
              <el-option label="年" value="year" />
            </el-select>
            
            <el-button type="primary" @click="loadChartData">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </template>
      
      <div v-loading="loading" class="chart-content">
        <BaseChart 
          v-if="chartOption" 
          :option="chartOption" 
          :height="chartHeight"
          @chart-click="handleChartClick"
          @chart-hover="handleChartHover"
        />
        
        <div v-else class="no-data">
          <el-empty description="暂无数据" />
        </div>
      </div>
    </el-card>
    
    <!-- 同环比分析 -->
    <el-card class="timeseries-card">
      <template #header>
        <span>同环比分析</span>
        <el-button 
          type="primary" 
          size="small" 
          @click="loadTimeSeriesData"
        >
          加载同环比数据
        </el-button>
      </template>
      
      <div v-if="timeSeriesOptions.length > 0" class="timeseries-content">
        <div 
          v-for="(option, index) in timeSeriesOptions" 
          :key="index" 
          class="timeseries-chart"
        >
          <BaseChart 
            :option="option" 
            height="300px"
          />
        </div>
      </div>
      <div v-else class="no-data">
        <el-empty description="暂无同环比数据" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import BaseChart from '@/components/BaseChart.vue'
import { getChartData, getTimeSeriesData } from '@/api/chart'
import type { ChartDataVO } from '@/api/chart'

// 响应式数据
const loading = ref(false)
const chartOption = ref<any>(null)
const timeSeriesOptions = ref<any[]>([])
const chartHeight = ref('500px')

// 查询参数
const queryParams = reactive({
  categoryId: 1,
  chartType: 'bar',
  dimension: 'dept',
  period: 'month'
})

// 生成ECharts配置
const generateChartOption = (chartData: ChartDataVO) => {
  const option: any = {
    title: {
      text: chartData.title,
      left: 'center',
      textStyle: {
        fontSize: 18,
        color: '#333'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => {
        if (chartData.chartType === 'pie') {
          return `${params.seriesName}<br/>${params.name}: ${params.value}% (${params.percent}%)`
        } else {
          return `${params.seriesName}<br/>${params.name}: ${params.value}`
        }
      }
    },
    legend: {
      show: chartData.options.showLegend,
      data: chartData.legendData,
      bottom: 10
    }
  }
  
  // 根据图表类型设置不同配置
  if (chartData.chartType === 'bar' || chartData.chartType === 'line') {
    option.xAxis = {
      type: 'category',
      data: chartData.xAxisData,
      axisLabel: {
        interval: 0,
        rotate: 45
      }
    }
    option.yAxis = {
      type: 'value'
    }
    option.series = chartData.series.map((s) => ({
      name: s.name,
      type: chartData.chartType,
      data: s.data,
      itemStyle: {
        color: '#1890ff'
      }
    }))
    option.grid = {
      left: '10%',
      right: '10%',
      bottom: '15%',
      top: '20%'
    }
  } else if (chartData.chartType === 'pie') {
    option.series = [{
      name: '数据分布',
      type: 'pie',
      radius: '50%',
      center: ['50%', '50%'],
      data: chartData.xAxisData.map((name, index) => ({
        name,
        value: chartData.series[0]?.data[index] || 0
      })),
      itemStyle: {
        borderColor: '#fff',
        borderWidth: 2
      }
    }]
  }
  
  return option
}

// 生成时间序列图表配置
const generateTimeSeriesOptions = (chartDataList: ChartDataVO[]) => {
  return chartDataList.map(chartData => ({
    title: {
      text: chartData.title,
      left: 'center',
      textStyle: { fontSize: 16 }
    },
    xAxis: {
      type: 'category',
      data: chartData.xAxisData
    },
    yAxis: {
      type: 'value'
    },
    tooltip: {
      trigger: 'axis'
    },
    series: [{
      name: chartData.series[0]?.name || '',
      type: 'line',
      data: chartData.series[0]?.data || [],
      smooth: true,
      itemStyle: { color: '#1890ff' }
    }]
  }))
}

// 加载图表数据
const loadChartData = async () => {
  loading.value = true
  try {
    const data = await getChartData({
      categoryId: queryParams.categoryId,
      chartType: queryParams.chartType,
      dimension: queryParams.dimension,
      period: queryParams.period
    }) as unknown as ChartDataVO
    
    chartOption.value = generateChartOption(data)
    ElMessage.success('数据加载成功')
  } catch (error: any) {
    ElMessage.error(error.message || '数据加载失败')
  } finally {
    loading.value = false
  }
}

// 加载时间序列数据
const loadTimeSeriesData = async () => {
  try {
    const dataList = await getTimeSeriesData({
      categoryId: queryParams.categoryId,
      dimension: 'time',
      timeRange: 'recent30days'
    }) as unknown as ChartDataVO[]
    
    timeSeriesOptions.value = generateTimeSeriesOptions(dataList)
    ElMessage.success('同环比数据加载成功')
  } catch (error: any) {
    ElMessage.error(error.message || '同环比数据加载失败')
  }
}

// 图表点击事件
const handleChartClick = (params: any) => {
  console.log('图表点击:', params)
  ElMessage.info(`点击了: ${params.name} - ${params.value}`)
}

// 图表悬停事件
const handleChartHover = (params: any) => {
  console.log('图表悬停:', params)
}

// 组件挂载时加载数据
onMounted(() => {
  loadChartData()
})
</script>

<style scoped>
.charts-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.chart-card, .timeseries-card {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-controls {
  display: flex;
  gap: 10px;
  align-items: center;
}

.chart-content {
  height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.timeseries-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.timeseries-chart {
  height: 300px;
}

.no-data {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

@media (max-width: 768px) {
  .chart-controls {
    flex-wrap: wrap;
  }
  
  .timeseries-content {
    grid-template-columns: 1fr;
  }
}
</style>