<template>
  <div class="rule-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>规则管理</span>
          <el-button type="primary" @click="openAddDialog">
            <el-icon><Plus /></el-icon>
            新增规则
          </el-button>
        </div>
      </template>
      
      <div class="filter-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input 
              v-model="queryParams.keyword" 
              placeholder="规则名称/描述" 
              :prefix-icon="Search"
              @keyup.enter="loadRules"
              clearable
            />
          </el-col>
          <el-col :span="6">
            <el-select 
              v-model="queryParams.ruleType" 
              placeholder="规则类型" 
              clearable
              @change="loadRules"
            >
              <el-option label="流失风险" value="FLIGHT_RISK" />
              <el-option label="人才短缺" value="TALENT_SHORTAGE" />
              <el-option label="绩效预警" value="PERFORMANCE_WARNING" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select 
              v-model="queryParams.status" 
              placeholder="状态" 
              clearable
              @change="loadRules"
            >
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="loadRules">搜索</el-button>
            <el-button @click="resetFilters">重置</el-button>
          </el-col>
        </el-row>
      </div>
      
      <el-table :data="tableData" style="width: 100%; margin-top: 20px;">
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="ruleType" label="规则类型" width="150">
          <template #default="{ row }">
            {{ getRuleTypeLabel(row.ruleType) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="规则描述" />
        <el-table-column prop="threshold" label="阈值" width="100" />
        <el-table-column prop="effective" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.effective === 1 ? 'success' : 'info'">
              {{ row.effective === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="version" label="版本" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button size="small" @click="openEditDialog(row)">编辑</el-button>
            <el-button 
              size="small" 
              :type="row.effective === 1 ? 'info' : 'success'"
              @click="toggleStatus(row)"
            >
              {{ row.effective === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button size="small" type="danger" @click="deleteRule(row)">删除</el-button>
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
    
    <!-- 规则编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="600px"
      :before-close="closeDialog"
    >
      <el-form 
        :model="ruleForm" 
        :rules="formRules" 
        ref="ruleFormRef" 
        label-width="100px"
      >
        <el-form-item label="规则类型" prop="ruleType">
          <el-select v-model="ruleForm.ruleType" placeholder="请选择规则类型">
            <el-option label="流失风险" value="FLIGHT_RISK" />
            <el-option label="人才短缺" value="TALENT_SHORTAGE" />
            <el-option label="绩效预警" value="PERFORMANCE_WARNING" />
          </el-select>
        </el-form-item>
        <el-form-item label="规则描述" prop="description">
          <el-input 
            v-model="ruleForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入规则描述"
          />
        </el-form-item>
        <el-form-item label="阈值" prop="threshold">
          <el-input-number 
            v-model="ruleForm.threshold" 
            :min="0" 
            :max="1" 
            :step="0.01"
            placeholder="请输入阈值"
          />
        </el-form-item>
        <el-form-item label="是否启用" prop="effective">
          <el-switch 
            v-model="ruleForm.effective" 
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
  ruleType: '',
  status: ''
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)

// 表单数据
const ruleForm = reactive({
  id: null as number | null,
  ruleType: '',
  description: '',
  threshold: 0.5,
  effective: 1
})

// 表单验证规则
const formRules: FormRules = {
  ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }],
  description: [{ required: true, message: '请输入规则描述', trigger: 'blur' }],
  threshold: [{ required: true, message: '请输入阈值', trigger: 'blur' }]
}

// 表单引用
const ruleFormRef = ref<FormInstance>()

// 获取规则列表
const loadRules = async () => {
  try {
    // 模拟API调用
    const mockData = [
      { id: 1, ruleType: 'FLIGHT_RISK', description: '员工离职风险超过80%时触发', threshold: 0.8, effective: 1, version: 1, createTime: '2026-02-21 10:00:00' },
      { id: 2, ruleType: 'TALENT_SHORTAGE', description: '关键岗位空缺超过30天时触发', threshold: 30, effective: 1, version: 1, createTime: '2026-02-21 09:30:00' },
      { id: 3, ruleType: 'PERFORMANCE_WARNING', description: '绩效低于70分时触发', threshold: 0.7, effective: 0, version: 2, createTime: '2026-02-20 15:20:00' },
    ]
    tableData.value = mockData
    total.value = mockData.length
  } catch (error) {
    console.error('获取规则列表失败:', error)
    ElMessage.error('获取规则列表失败')
  }
}

// 获取规则类型标签
const getRuleTypeLabel = (type: string) => {
  const labels: Record<string, string> = {
    FLIGHT_RISK: '流失风险',
    TALENT_SHORTAGE: '人才短缺',
    PERFORMANCE_WARNING: '绩效预警'
  }
  return labels[type] || type
}

// 打开新增对话框
const openAddDialog = () => {
  isEdit.value = false
  dialogTitle.value = '新增规则'
  resetForm()
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row: any) => {
  isEdit.value = true
  dialogTitle.value = '编辑规则'
  // 复制数据到表单
  Object.assign(ruleForm, { ...row })
  ruleForm.effective = row.effective // 确保状态值正确
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  Object.assign(ruleForm, {
    id: null,
    ruleType: '',
    description: '',
    threshold: 0.5,
    effective: 1
  })
  if (ruleFormRef.value) {
    ruleFormRef.value.clearValidate()
  }
}

// 关闭对话框
const closeDialog = () => {
  dialogVisible.value = false
  resetForm()
}

// 提交表单
const submitForm = async () => {
  if (!ruleFormRef.value) return
  
  await ruleFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          // 编辑规则
          ElMessage.success('规则更新成功')
        } else {
          // 新增规则
          ElMessage.success('规则创建成功')
        }
        closeDialog()
        loadRules() // 刷新列表
      } catch (error) {
        console.error('提交规则失败:', error)
        ElMessage.error(isEdit.value ? '更新规则失败' : '创建规则失败')
      }
    }
  })
}

// 切换状态
const toggleStatus = async (row: any) => {
  try {
    const newStatus = row.effective === 1 ? 0 : 1
    const action = newStatus === 1 ? '启用' : '禁用'
    
    await ElMessageBox.confirm(`确定要${action}此规则吗？`, '确认操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    
    // 模拟API调用
    ElMessage.success(`${action}规则成功`)
    loadRules() // 刷新列表
  } catch (error) {
    console.error('切换状态失败:', error)
    ElMessage.error('操作失败')
  }
}

// 删除规则
const deleteRule = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除此规则吗？', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    
    // 模拟API调用
    ElMessage.success('删除规则成功')
    loadRules() // 刷新列表
  } catch (error) {
    console.error('删除规则失败:', error)
    ElMessage.error('删除失败')
  }
}

// 重置筛选条件
const resetFilters = () => {
  queryParams.keyword = ''
  queryParams.ruleType = ''
  queryParams.status = ''
  loadRules()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pageSize.value = size
  loadRules()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  loadRules()
}

onMounted(() => {
  loadRules()
})
</script>

<style scoped>
.rule-management-container {
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