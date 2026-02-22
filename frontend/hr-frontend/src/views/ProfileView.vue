<template>
  <div class="profile-container">
    <el-card>
      <template #header>
        <h3>个人中心</h3>
      </template>
      <div class="profile-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="用户名">
            {{ userStore.userInfo?.username }}
          </el-descriptions-item>
          <el-descriptions-item label="角色">
            {{ userStore.userInfo?.role }}
          </el-descriptions-item>
          <el-descriptions-item label="部门">
            {{ userStore.userInfo?.deptName || '暂无' }}
          </el-descriptions-item>
          <el-descriptions-item label="部门权限范围">
            {{ userStore.userInfo?.deptScope?.join(', ') || '全部' }}
          </el-descriptions-item>
        </el-descriptions>
        
        <div class="actions" style="margin-top: 20px;">
          <el-button type="primary" @click="handleLogout">
            退出登录
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const handleLogout = () => {
  userStore.clearAuth()
  router.push('/login')
  ElMessage.success('已退出登录')
}
</script>

<style scoped>
.profile-container {
  height: 100%;
}

.profile-content {
  max-width: 600px;
}
</style>