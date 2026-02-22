<template>
  <div class="dashboard-container">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <h3>HR数据中心</h3>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="dashboard">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="charts">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据图表</span>
          </el-menu-item>
          <el-menu-item index="data-analysis">
            <el-icon><Histogram /></el-icon>
            <span>数据分析</span>
          </el-menu-item>
          <el-menu-item index="profile">
            <el-icon><User /></el-icon>
            <span>个人中心</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区 -->
      <el-container>
        <!-- 头部 -->
        <el-header class="header">
          <div class="header-left">
            <h2>{{ currentTitle }}</h2>
          </div>
          <div class="header-right">
            <el-dropdown @command="handleUserCommand">
              <span class="user-info">
                <el-icon><User /></el-icon>
                <span>{{ userStore.userInfo?.username }}</span>
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 内容区域 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import {
  House,
  DataAnalysis,
  Histogram,
  User,
  ArrowDown
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 当前激活的菜单
const activeMenu = computed(() => {
  return route.name?.toString().toLowerCase() || 'dashboard'
})

// 当前页面标题
const currentTitle = computed(() => {
  const titles: Record<string, string> = {
    dashboard: '首页',
    charts: '数据图表',
    'data-analysis': '数据分析',
    profile: '个人中心'
  }
  return titles[activeMenu.value] || '首页'
})

// 菜单选择处理
const handleMenuSelect = (index: string) => {
  router.push(`/${index}`)
}

// 用户下拉菜单处理
const handleUserCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      userStore.clearAuth()
      router.push('/login')
      ElMessage.success('已退出登录')
      break
  }
}
</script>

<style scoped>
.dashboard-container {
  height: 100vh;
}

.sidebar {
  background-color: #2c3e50;
  color: white;
}

.logo {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #34495e;
}

.logo h3 {
  color: white;
  margin: 0;
}

.sidebar-menu {
  border: none;
  background-color: #2c3e50;
}

.sidebar-menu :deep(.el-menu-item) {
  color: #bdc3c7;
}

.sidebar-menu :deep(.el-menu-item:hover) {
  background-color: #34495e;
  color: white;
}

.sidebar-menu :deep(.el-menu-item.is-active) {
  background-color: #3498db;
  color: white;
}

.header {
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left h2 {
  margin: 0;
  color: #333;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
}

.user-info:hover {
  background-color: #f5f5f5;
}

.main-content {
  background-color: #f5f5f5;
  padding: 20px;
}
</style>