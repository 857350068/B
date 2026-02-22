<template>
  <el-button 
    :icon="isFavorited ? 'StarFilled' : 'Star'"
    :type="isFavorited ? 'warning' : 'default'"
    :loading="loading"
    @click="toggleFavorite"
    :size="size"
  >
    {{ isFavorited ? '已收藏' : '收藏' }}
  </el-button>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'
import { getFavoriteCheckStatus, addFavorite, deleteFavorite } from '@/api/favorite'

// 定义props
interface Props {
  favType: string
  favoriteId: number
  title: string
  size?: 'large' | 'default' | 'small'
}

const props = withDefaults(defineProps<Props>(), {
  size: 'default'
})

// 定义emit
const emit = defineEmits<{
  (e: 'change', favorited: boolean): void
}>()

// 响应式数据
const isFavorited = ref(false)
const loading = ref(false)

// 检查收藏状态
const checkFavoriteStatus = async () => {
  try {
    const result = await getFavoriteCheckStatus(props.favType, props.favoriteId)
    isFavorited.value = result as unknown as boolean
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 切换收藏状态
const toggleFavorite = async () => {
  if (loading.value) return
  
  loading.value = true
  
  try {
    if (isFavorited.value) {
      // 取消收藏
      await deleteFavorite(props.favoriteId)
      isFavorited.value = false
      ElMessage.success('已取消收藏')
    } else {
      // 添加收藏
      await addFavorite({
        favType: props.favType,
        favoriteId: props.favoriteId,
        title: props.title
      })
      isFavorited.value = true
      ElMessage.success('收藏成功')
    }
    
    // 触发状态改变事件
    emit('change', isFavorited.value)
  } catch (error: any) {
    console.error('操作收藏失败:', error)
    ElMessage.error(error.message || '操作失败')
  } finally {
    loading.value = false
  }
}

// 监听favoriteId变化，重新检查状态
watch(() => props.favoriteId, () => {
  if (props.favoriteId) {
    checkFavoriteStatus()
  }
})

// 组件挂载时检查收藏状态
onMounted(() => {
  if (props.favoriteId) {
    checkFavoriteStatus()
  }
})
</script>

<style scoped>
</style>