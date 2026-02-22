import request from './request'

// 图表数据接口
export interface ChartDataVO {
  title: string
  chartType: string
  xAxisData: string[]
  series: ChartSeries[]
  legendData: string[]
  categoryId: number
  dimension: string
  period: string
  options: ChartOptions
}

export interface ChartSeries {
  name: string
  type: string
  data: any[]
  yearOnYearData?: any[]
  monthOnMonthData?: any[]
}

export interface ChartOptions {
  showLegend: boolean
  showTooltip: boolean
  showLabel: boolean
  height: string
  colors: string[]
}

// 获取图表数据
export function getChartData(params: {
  categoryId: number
  chartType: string
  dimension: string
  period: string
  deptId?: number
}) {
  return request.get<ChartDataVO>('/chart/data', { params })
}

// 获取分时数据（同环比分析）
export function getTimeSeriesData(params: {
  categoryId: number
  dimension: string
  timeRange: string
  deptId?: number
}) {
  return request.get<ChartDataVO[]>('/chart/timeseries', { params })
}

// 获取对比数据
export function getCompareData(params: {
  categoryId: number
  dimensions: string
  period: string
  deptId?: number
}) {
  return request.get<ChartDataVO[]>('/chart/compare', { params })
}

// 获取图表配置选项
export function getChartOptions(chartType: string) {
  return request.get<ChartOptions>(`/chart/options/${chartType}`)
}