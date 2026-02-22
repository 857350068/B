<template>
  <div class="data-analysis-container">
    <el-card>
      <template #header>
        <h3>数据分析</h3>
      </template>
      <div class="content">
        <el-empty description="数据分析功能开发中..." />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.data-analysis-container {
  height: 100%;
}

.content {
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>