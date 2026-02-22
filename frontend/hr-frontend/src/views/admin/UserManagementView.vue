<template>
  <div class="user-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button 
            type="primary" 
            :icon="Plus" 
            @click="openAddDialog"
          >
            新增用户
          </el-button>
        </div>
      </template>
      
      <div class="filter-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input 
              v-model="queryParams.keyword" 
              placeholder="工号/姓名" 
              :prefix-icon="Search"
              @keyup.enter="loadUsers"
              clearable
            />
          </el-col>
          <el-col :span="6">
            <el-select 
              v-model="queryParams.role" 
              placeholder="角色" 
              clearable
              @change="loadUsers"
            >
              <el-option label="HR管理员" value="HR_ADMIN" />
              <el-option label="部门负责人" value="DEPT_HEAD" />
              <el-option label="管理层" value="MANAGEMENT" />
              <el-option label="普通员工" value="EMPLOYEE" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-select 
              v-model="queryParams.deptId" 
              placeholder="部门" 
              clearable
              @change="loadUsers"
            >
              <el-option label="销售部" :value="101" />
              <el-option label="研发部" :value="102" />
              <el-option label="人事部" :value="103" />
              <el-option label="财务部" :value="104" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-button 
              type="primary" 
              :icon="Search" 
              @click="loadUsers"
            >
              搜索
            </el-button>
          </el-col>
        </el-row>
      </div>
      
      <div class="content-section">
        <el-table 
          :data="users" 
          v-loading="loading"
          row-key="id"
        >
          <el-table-column prop="username" label="工号" width="120" />
          <el-table-column prop="name" label="姓名" width="120" />
          <el-table-column prop="role" label="角色" width="120">
            <template #default="{ row }">
              <el-tag :type="getRoleTagType(row.role)">
                {{ getRoleName(row.role) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="deptName" label="部门" width="120" />
          <el-table-column prop="phone" label="手机号" width="150" />
          <el-table-column prop="email" label="邮箱" width="200" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button 
                type="primary" 
                size="small" 
                @click="openEditDialog(row)"
              >
                编辑
              </el-button>
              <el-button 
                :type="row.status === 1 ? 'danger' : 'success'" 
                size="small" 
                @click="toggleStatus(row)"
              >
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button 
                type="danger" 
                size="small" 
                @click="handleDeleteUser(row.id)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="pagination-section" v-if="total > 0">
          <el-pagination
            v-model:current-page="queryParams.pageNum"
            v-model:page-size="queryParams.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>
    
    <!-- 用户编辑对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="currentUser" 
        :rules="userRules" 
        ref="userFormRef"
        label-width="80px"
      >
        <el-form-item label="工号" prop="username">
          <el-input 
            v-model="currentUser.username" 
            :disabled="!!currentUser.id"
            placeholder="请输入工号"
          />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="currentUser.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="currentUser.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="HR管理员" value="HR_ADMIN" />
            <el-option label="部门负责人" value="DEPT_HEAD" />
            <el-option label="管理层" value="MANAGEMENT" />
            <el-option label="普通员工" value="EMPLOYEE" />
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="deptId">
          <el-select v-model="currentUser.deptId" placeholder="请选择部门" style="width: 100%">
            <el-option label="销售部" :value="101" />
            <el-option label="研发部" :value="102" />
            <el-option label="人事部" :value="103" />
            <el-option label="财务部" :value="104" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="currentUser.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="currentUser.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="currentUser.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { getUserPage, addUser, updateUser, deleteUser } from '@/api/admin/user'

// 用户类型定义
interface User {
  id?: number
  username: string
  name: string
  role: string
  deptId: number
  deptName?: string
  phone?: string
  email?: string
  status: number
}

// 响应式数据
const loading = ref(false)
const users = ref<User[]>([])
const total = ref(0)

// 查询参数
const queryParams = reactive({
  keyword: '',
  role: '',
  deptId: undefined as number | undefined,
  pageNum: 1,
  pageSize: 10
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const currentUser = ref<User>({
  username: '',
  name: '',
  role: '',
  deptId: 101,
  phone: '',
  email: '',
  status: 1
})

// 表单引用
const userFormRef = ref<FormInstance>()

// 表单验证规则
const userRules: FormRules = {
  username: [
    { required: true, message: '请输入工号', trigger: 'blur' },
    { pattern: /^\d{4,10}$/, message: '工号必须是4-10位数字', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ],
  deptId: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 加载用户列表
const loadUsers = async () => {
  loading.value = true
  try {
    const response = await getUserPage(
      queryParams.keyword || undefined,
      queryParams.role || undefined,
      queryParams.deptId,
      queryParams.pageNum,
      queryParams.pageSize
    )
    
    users.value = response.data.records
    total.value = response.data.total
    
    ElMessage.success('用户列表加载成功')
  } catch (error: any) {
    console.error('加载用户列表失败:', error)
    ElMessage.error(error.message || '加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 打开新增对话框
const openAddDialog = () => {
  dialogTitle.value = '新增用户'
  currentUser.value = {
    username: '',
    name: '',
    role: '',
    deptId: 101,
    phone: '',
    email: '',
    status: 1
  }
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (user: User) => {
  dialogTitle.value = '编辑用户'
  currentUser.value = { ...user }
  dialogVisible.value = true
}

// 保存用户
const saveUser = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (currentUser.value.id) {
          // 更新用户
          await updateUser(currentUser.value.id, currentUser.value)
          ElMessage.success('用户更新成功')
        } else {
          // 新增用户
          await addUser(currentUser.value)
          ElMessage.success('用户添加成功')
        }
        
        dialogVisible.value = false
        loadUsers()
      } catch (error: any) {
        console.error('保存用户失败:', error)
        ElMessage.error(error.message || '保存用户失败')
      }
    }
  })
}

// 删除用户
const handleDeleteUser = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这个用户吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const result = await deleteUser(id)
    ElMessage.success('用户删除成功')
    loadUsers()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除用户失败:', error)
      ElMessage.error(error.message || '删除用户失败')
    }
  }
}

// 切换用户状态
const toggleStatus = async (user: User) => {
  try {
    const newStatus = user.status === 1 ? 0 : 1
    await ElMessageBox.confirm(
      `确定要${newStatus === 1 ? '启用' : '禁用'}用户 "${user.name}" 吗？`, 
      '状态变更确认', 
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: newStatus === 1 ? 'success' : 'warning'
      }
    )
    
    console.log(`切换用户状态: ${user.id}, 新状态: ${newStatus}`)
    ElMessage.success(`用户状态已${newStatus === 1 ? '启用' : '禁用'}`)
    loadUsers()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('切换用户状态失败:', error)
      ElMessage.error(error.message || '切换用户状态失败')
    }
  }
}

// 获取角色标签类型
const getRoleTagType = (role: string) => {
  switch (role) {
    case 'HR_ADMIN': return 'primary'
    case 'DEPT_HEAD': return 'success'
    case 'MANAGEMENT': return 'warning'
    case 'EMPLOYEE': return 'info'
    default: return 'info'
  }
}

// 获取角色名称
const getRoleName = (role: string) => {
  switch (role) {
    case 'HR_ADMIN': return 'HR管理员'
    case 'DEPT_HEAD': return '部门负责人'
    case 'MANAGEMENT': return '管理层'
    case 'EMPLOYEE': return '普通员工'
    default: return role
  }
}

// 处理页面大小改变
const handleSizeChange = (size: number) => {
  queryParams.pageSize = size
  queryParams.pageNum = 1
  loadUsers()
}

// 处理当前页改变
const handleCurrentChange = (page: number) => {
  queryParams.pageNum = page
  loadUsers()
}

// 组件挂载时加载数据
onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-management-container {
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

.content-section {
  min-height: 400px;
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>