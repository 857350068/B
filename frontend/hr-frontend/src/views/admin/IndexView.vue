<template>
  <div class="admin-index-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>后台管理中心</span>
        </div>
      </template>
      
      <div class="dashboard-grid">
        <el-card class="dashboard-item">
          <template #header>
            <div class="item-header">
              <i class="el-icon-user"></i>
              <span>用户管理</span>
            </div>
          </template>
          <div class="item-content">
            <p>管理用户账号、角色和权限</p>
            <el-button type="primary" @click="$router.push('/admin/users')">进入管理</el-button>
          </div>
        </el-card>
        
        <el-card class="dashboard-item">
          <template #header>
            <div class="item-header">
              <i class="el-icon-folder"></i>
              <span>分类管理</span>
            </div>
          </template>
          <div class="item-content">
            <p>管理数据分类和层级结构</p>
            <el-button type="primary">进入管理</el-button>
          </div>
        </el-card>
        
        <el-card class="dashboard-item">
          <template #header>
            <div class="item-header">
              <i class="el-icon-data-analysis"></i>
              <span>数据管理</span>
            </div>
          </template>
          <div class="item-content">
            <p>数据同步、导入导出功能</p>
            <el-button type="primary">进入管理</el-button>
          </div>
        </el-card>
        
        <el-card class="dashboard-item">
          <template #header>
            <div class="item-header">
              <i class="el-icon-warning"></i>
              <span>规则管理</span>
            </div>
          </template>
          <div class="item-content">
            <p>预警规则配置和管理</p>
            <el-button type="primary">进入管理</el-button>
          </div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

// 管理中心页面
</script>

<style scoped>
.admin-index-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.dashboard-item {
  height: 200px;
}

.item-header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.item-content {
  display: flex;
  flex-direction: column;
  height: calc(100% - 40px);
  justify-content: space-between;
}
</style>