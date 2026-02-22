<template>
  <div class="data-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据管理</span>
        </div>
      </template>
      
      <div class="management-actions">
        <el-button type="primary" @click="syncData">
          <el-icon><Refresh /></el-icon>
          同步数据到Hive
        </el-button>
        <el-button type="success" @click="handleImport">
          <el-icon><Upload /></el-icon>
          导入数据
        </el-button>
        <el-button type="info" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
        <el-button @click="showSyncLog">
          <el-icon><Document /></el-icon>
          同步日志
        </el-button>
      </div>
      
      <el-table :data="tableData" style="width: 100%; margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="startTime" label="开始时间" width="200" />
        <el-table-column prop="endTime" label="结束时间" width="200" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recordsProcessed" label="处理记录数" width="120" />
        <el-table-column prop="details" label="详情" />
      </el-table>
      
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; justify-content: center;"
      />
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Upload, Download, Document } from '@element-plus/icons-vue'

// 表格数据
const tableData = ref<any[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取同步日志
const getSyncLog = async () => {
  try {
    // 模拟API调用
    const mockData = [
      { id: 1, startTime: '2026-02-21 10:00:00', endTime: '2026-02-21 10:05:00', status: 'SUCCESS', recordsProcessed: 1250, details: '同步了1250条记录' },
      { id: 2, startTime: '2026-02-21 09:00:00', endTime: '2026-02-21 09:03:00', status: 'FAILED', recordsProcessed: 800, details: '同步了800条记录，部分失败' },
      { id: 3, startTime: '2026-02-21 08:00:00', endTime: '2026-02-21 08:04:00', status: 'SUCCESS', recordsProcessed: 1100, details: '同步了1100条记录' },
    ]
    tableData.value = mockData
    total.value = mockData.length
  } catch (error) {
    console.error('获取同步日志失败:', error)
    ElMessage.error('获取同步日志失败')
  }
}

// 同步数据到Hive
const syncData = async () => {
  try {
    ElMessageBox.confirm(
      '确定要同步数据到Hive吗？此操作可能需要较长时间。',
      '确认同步',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    ).then(async () => {
      // 模拟API调用
      ElMessage.success('数据同步任务已启动')
      // 刷新日志
      await getSyncLog()
    })
  } catch (error) {
    console.error('同步数据失败:', error)
    ElMessage.error('同步数据失败')
  }
}

// 处理导入
const handleImport = () => {
  ElMessage.info('导入功能待实现')
}

// 处理导出
const handleExport = () => {
  ElMessage.info('导出功能待实现')
}

// 显示同步日志
const showSyncLog = () => {
  getSyncLog()
}

// 获取状态类型
const getStatusType = (status: string) => {
  switch (status) {
    case 'SUCCESS':
      return 'success'
    case 'FAILED':
      return 'danger'
    default:
      return 'info'
  }
}

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  getSyncLog()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  getSyncLog()
}

onMounted(() => {
  getSyncLog()
})
</script>

<style scoped>
.data-management-container {
  padding: 20px;
}

.management-actions {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>