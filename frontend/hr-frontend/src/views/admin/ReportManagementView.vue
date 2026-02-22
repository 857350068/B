<template>
  <div class="report-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>报表管理</span>
          <el-button type="primary" @click="openAddDialog">
            <el-icon><Plus /></el-icon>
            新增模板
          </el-button>
        </div>
      </template>
      
      <div class="filter-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input 
              v-model="queryParams.keyword" 
              placeholder="模板名称/描述" 
              :prefix-icon="Search"
              @keyup.enter="loadTemplates"
              clearable
            />
          </el-col>
          <el-col :span="6">
            <el-select 
              v-model="queryParams.category" 
              placeholder="模板分类" 
              clearable
              @change="loadTemplates"
            >
              <el-option label="人员报表" value="PERSONNEL" />
              <el-option label="绩效报表" value="PERFORMANCE" />
              <el-option label="薪酬报表" value="COMPENSATION" />
              <el-option label="培训报表" value="TRAINING" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select 
              v-model="queryParams.status" 
              placeholder="状态" 
              clearable
              @change="loadTemplates"
            >
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="loadTemplates">搜索</el-button>
            <el-button @click="resetFilters">重置</el-button>
          </el-col>
        </el-row>
      </div>
      
      <el-table :data="tableData" style="width: 100%; margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="name" label="模板名称" width="200" />
        <el-table-column prop="category" label="分类" width="120">
          <template #default="{ row }">
            {{ getCategoryLabel(row.category) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="enabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled === 1 ? 'success' : 'info'">
              {{ row.enabled === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="300">
          <template #default="{ row }">
            <el-button size="small" @click="openEditDialog(row)">编辑</el-button>
            <el-button size="small" type="primary" @click="generateReport(row)">生成报表</el-button>
            <el-button 
              size="small" 
              :type="row.enabled === 1 ? 'info' : 'success'"
              @click="toggleStatus(row)"
            >
              {{ row.enabled === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button size="small" type="danger" @click="deleteTemplate(row)">删除</el-button>
          </template>
        </el-table-column>
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
    
    <!-- 模板编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="800px"
      :before-close="closeDialog"
    >
      <el-form 
        :model="templateForm" 
        :rules="formRules" 
        ref="templateFormRef" 
        label-width="100px"
      >
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="templateForm.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板分类" prop="category">
          <el-select v-model="templateForm.category" placeholder="请选择模板分类">
            <el-option label="人员报表" value="PERSONNEL" />
            <el-option label="绩效报表" value="PERFORMANCE" />
            <el-option label="薪酬报表" value="COMPENSATION" />
            <el-option label="培训报表" value="TRAINING" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input 
            v-model="templateForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入模板描述"
          />
        </el-form-item>
        <el-form-item label="查询SQL" prop="querySql">
          <el-input 
            v-model="templateForm.querySql" 
            type="textarea" 
            :rows="4"
            placeholder="请输入查询SQL语句"
          />
        </el-form-item>
        <el-form-item label="是否启用" prop="enabled">
          <el-switch 
            v-model="templateForm.enabled" 
            :active-value="1" 
            :inactive-value="0"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDialog">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 生成报表对话框 -->
    <el-dialog 
      v-model="generateDialogVisible" 
      title="生成报表" 
      width="600px"
      :before-close="closeGenerateDialog"
    >
      <el-form :model="reportParams" label-width="120px">
        <el-form-item label="开始日期">
          <el-date-picker
            v-model="reportParams.startDate"
            type="date"
            placeholder="选择开始日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker
            v-model="reportParams.endDate"
            type="date"
            placeholder="选择结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="reportParams.department" placeholder="选择部门">
            <el-option label="全部部门" value="" />
            <el-option label="人事部" value="HR" />
            <el-option label="财务部" value="FINANCE" />
            <el-option label="技术部" value="TECH" />
            <el-option label="销售部" value="SALES" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeGenerateDialog">取消</el-button>
          <el-button type="primary" @click="confirmGenerateReport">生成</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'

// 表格数据
const tableData = ref<any[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 查询参数
const queryParams = reactive({
  keyword: '',
  category: '',
  status: ''
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)

// 生成报表对话框
const generateDialogVisible = ref(false)
const currentTemplate = ref<any>(null)

// 报表参数
const reportParams = reactive({
  startDate: '',
  endDate: '',
  department: ''
})

// 表单数据
const templateForm = reactive({
  id: null as number | null,
  name: '',
  category: '',
  description: '',
  querySql: '',
  enabled: 1
})

// 表单验证规则
const formRules: FormRules = {
  name: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择模板分类', trigger: 'change' }],
  querySql: [{ required: true, message: '请输入查询SQL', trigger: 'blur' }]
}

// 表单引用
const templateFormRef = ref<FormInstance>()

// 获取模板列表
const loadTemplates = async () => {
  try {
    // 模拟API调用
    const mockData = [
      { id: 1, name: '月度人员报表', category: 'PERSONNEL', description: '月度人员变动情况统计', querySql: 'SELECT * FROM hr_personnel_data WHERE month = ?', enabled: 1, createTime: '2026-02-21 10:00:00' },
      { id: 2, name: '季度绩效报表', category: 'PERFORMANCE', description: '季度员工绩效评估汇总', querySql: 'SELECT * FROM hr_performance_data WHERE quarter = ?', enabled: 1, createTime: '2026-02-21 09:30:00' },
      { id: 3, name: '年度薪酬报表', category: 'COMPENSATION', description: '年度薪酬福利支出统计', querySql: 'SELECT * FROM hr_compensation_data WHERE year = ?', enabled: 0, createTime: '2026-02-20 15:20:00' },
    ]
    tableData.value = mockData
    total.value = mockData.length
  } catch (error) {
    console.error('获取模板列表失败:', error)
    ElMessage.error('获取模板列表失败')
  }
}

// 获取分类标签
const getCategoryLabel = (category: string) => {
  const labels: Record<string, string> = {
    PERSONNEL: '人员报表',
    PERFORMANCE: '绩效报表',
    COMPENSATION: '薪酬报表',
    TRAINING: '培训报表'
  }
  return labels[category] || category
}

// 打开新增对话框
const openAddDialog = () => {
  isEdit.value = false
  dialogTitle.value = '新增模板'
  resetForm()
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row: any) => {
  isEdit.value = true
  dialogTitle.value = '编辑模板'
  // 复制数据到表单
  Object.assign(templateForm, { ...row })
  templateForm.enabled = row.enabled // 确保状态值正确
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  Object.assign(templateForm, {
    id: null,
    name: '',
    category: '',
    description: '',
    querySql: '',
    enabled: 1
  })
  if (templateFormRef.value) {
    templateFormRef.value.clearValidate()
  }
}

// 关闭对话框
const closeDialog = () => {
  dialogVisible.value = false
  resetForm()
}

// 关闭生成报表对话框
const closeGenerateDialog = () => {
  generateDialogVisible.value = false
  resetReportParams()
}

// 重置报表参数
const resetReportParams = () => {
  reportParams.startDate = ''
  reportParams.endDate = ''
  reportParams.department = ''
  currentTemplate.value = null
}

// 提交表单
const submitForm = async () => {
  if (!templateFormRef.value) return
  
  await templateFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          // 编辑模板
          ElMessage.success('模板更新成功')
        } else {
          // 新增模板
          ElMessage.success('模板创建成功')
        }
        closeDialog()
        loadTemplates() // 刷新列表
      } catch (error) {
        console.error('提交模板失败:', error)
        ElMessage.error(isEdit.value ? '更新模板失败' : '创建模板失败')
      }
    }
  })
}

// 生成报表
const generateReport = (row: any) => {
  currentTemplate.value = row
  generateDialogVisible.value = true
}

// 确认生成报表
const confirmGenerateReport = async () => {
  try {
    // 模拟API调用
    ElMessage.success('报表生成任务已提交，稍后可在下载中心查看')
    closeGenerateDialog()
  } catch (error) {
    console.error('生成报表失败:', error)
    ElMessage.error('生成报表失败')
  }
}

// 切换状态
const toggleStatus = async (row: any) => {
  try {
    const newStatus = row.enabled === 1 ? 0 : 1
    const action = newStatus === 1 ? '启用' : '禁用'
    
    await ElMessageBox.confirm(`确定要${action}此模板吗？`, '确认操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    
    // 模拟API调用
    ElMessage.success(`${action}模板成功`)
    loadTemplates() // 刷新列表
  } catch (error) {
    console.error('切换状态失败:', error)
    ElMessage.error('操作失败')
  }
}

// 删除模板
const deleteTemplate = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除此模板吗？', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    
    // 模拟API调用
    ElMessage.success('删除模板成功')
    loadTemplates() // 刷新列表
  } catch (error) {
    console.error('删除模板失败:', error)
    ElMessage.error('删除失败')
  }
}

// 重置筛选条件
const resetFilters = () => {
  queryParams.keyword = ''
  queryParams.category = ''
  queryParams.status = ''
  loadTemplates()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  loadTemplates()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadTemplates()
}

onMounted(() => {
  loadTemplates()
})
</script>

<style scoped>
.report-management-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-section {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>