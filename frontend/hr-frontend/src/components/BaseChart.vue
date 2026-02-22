<template>
  <div ref="chartRef" :style="{ height: chartHeight }"></div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts/core'
import { BarChart, LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'

// 注册组件
echarts.use([
  BarChart,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CanvasRenderer
])

// 定义props
interface Props {
  option: any
  height?: string
}

const props = withDefaults(defineProps<Props>(), {
  height: '400px'
})

// 定义emits
const emit = defineEmits<{
  (e: 'chartClick', params: any): void
  (e: 'chartHover', params: any): void
}>()

// 响应式数据
const chartRef = ref<HTMLElement | null>(null)
const chartInstance = ref<echarts.ECharts | null>(null)
const chartHeight = ref(props.height)

// 初始化图表
const initChart = () => {
  if (chartRef.value && !chartInstance.value) {
    chartInstance.value = echarts.init(chartRef.value)
    
    // 绑定事件
    chartInstance.value.on('click', (params) => {
      emit('chartClick', params)
    })
    
    chartInstance.value.on('mouseover', (params) => {
      emit('chartHover', params)
    })
    
    // 设置初始配置
    if (props.option) {
      chartInstance.value.setOption(props.option)
    }
  }
}

// 更新图表
const updateChart = () => {
  if (chartInstance.value && props.option) {
    chartInstance.value.setOption(props.option, true)
  }
}

// 重置图表大小
const resizeChart = () => {
  if (chartInstance.value) {
    chartInstance.value.resize()
  }
}

// 监听option变化
watch(() => props.option, () => {
  nextTick(() => {
    updateChart()
  })
}, { deep: true })

// 监听高度变化
watch(() => props.height, (newHeight) => {
  chartHeight.value = newHeight
  nextTick(() => {
    resizeChart()
  })
})

// 组件挂载
onMounted(() => {
  nextTick(() => {
    initChart()
    window.addEventListener('resize', resizeChart)
  })
})

// 组件卸载
onUnmounted(() => {
  if (chartInstance.value) {
    chartInstance.value.dispose()
    chartInstance.value = null
  }
  window.removeEventListener('resize', resizeChart)
})

// 暴露方法给父组件
defineExpose({
  resize: resizeChart,
  getInstance: () => chartInstance.value
})
</script>

<style scoped>
</style>