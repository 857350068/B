<template>
  <div class="favorite-management-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>收藏管理</span>
          <el-button 
            type="primary" 
            :icon="Refresh" 
            @click="loadFavorites"
            :loading="loading"
          >
            刷新
          </el-button>
        </div>
      </template>
      
      <div class="filter-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-select 
              v-model="queryParams.favType" 
              placeholder="收藏类型" 
              clearable
              @change="loadFavorites"
            >
              <el-option label="报表" value="REPORT" />
              <el-option label="预警" value="WARNING" />
              <el-option label="分析" value="ANALYSIS" />
              <el-option label="人才" value="TALENT" />
            </el-select>
          </el-col>
          <el-col :span="18">
            <el-input 
              v-model="queryParams.keyword" 
              placeholder="输入标题关键词搜索" 
              :prefix-icon="Search"
              @keyup.enter="loadFavorites"
              clearable
            >
              <template #append>
                <el-button :icon="Search" @click="loadFavorites">搜索</el-button>
              </template>
            </el-input>
          </el-col>
        </el-row>
      </div>
      
      <div class="content-section">
        <el-table 
          :data="favorites" 
          v-loading="loading"
          row-key="id"
          :default-sort="{ prop: 'createTime', order: 'descending' }"
        >
          <el-table-column prop="title" label="标题" min-width="200">
            <template #default="{ row }">
              <span>{{ row.title }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="favType" label="类型" width="120">
            <template #default="{ row }">
              <el-tag 
                :type="getTagType(row.favType)"
                size="small"
              >
                {{ getFavTypeName(row.favType) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="createTime" label="收藏时间" width="180">
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button 
                type="primary" 
                size="small" 
                @click="goToDetail(row)"
              >
                查看
              </el-button>
              <el-button 
                type="danger" 
                size="small" 
                @click="removeFavorite(row.id)"
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
        
        <div v-if="favorites.length === 0 && !loading" class="empty-state">
          <el-empty description="暂无收藏内容" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'
import { getFavoriteList, deleteFavorite, getFavoritePage, type Favorite } from '@/api/favorite'

// 响应式数据
const loading = ref(false)
const favorites = ref<Favorite[]>([])
const total = ref(0)

// 查询参数
const queryParams = reactive({
  favType: '',
  keyword: '',
  pageNum: 1,
  pageSize: 10
})

// 加载收藏列表
const loadFavorites = async () => {
  loading.value = true
  try {
    // 使用分页接口
    const response = await getFavoritePage(queryParams.favType || undefined, queryParams.keyword || undefined, queryParams.pageNum, queryParams.pageSize)
    
    // 直接使用后端返回的数据
    favorites.value = response.data.records
    total.value = response.data.total
    
    ElMessage.success('收藏列表加载成功')
  } catch (error: any) {
    console.error('加载收藏列表失败:', error)
    ElMessage.error(error.message || '加载收藏列表失败')
  } finally {
    loading.value = false
  }
}

// 删除收藏
const removeFavorite = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这条收藏吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteFavorite(id)
    ElMessage.success('删除成功')
    // 重新加载列表
    loadFavorites()
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除收藏失败:', error)
      ElMessage.error(error.message || '删除收藏失败')
    }
  }
}

// 跳转到详情页面
const goToDetail = (row: Favorite) => {
  // 根据收藏类型跳转到相应页面
  let path = '/'
  switch (row.favType) {
    case 'REPORT':
      path = `/reports/${row.favoriteId}`
      break
    case 'WARNING':
      path = `/warnings/${row.favoriteId}`
      break
    case 'ANALYSIS':
      path = `/analysis/${row.favoriteId}`
      break
    case 'TALENT':
      path = `/talent/${row.favoriteId}`
      break
    default:
      path = `/detail/${row.favoriteId}`
  }
  
  // 使用window.open打开新标签页
  window.open(path, '_blank')
}

// 获取标签类型
const getTagType = (favType: string) => {
  switch (favType) {
    case 'REPORT': return 'primary'
    case 'WARNING': return 'danger'
    case 'ANALYSIS': return 'success'
    case 'TALENT': return 'warning'
    default: return 'info'
  }
}

// 获取收藏类型名称
const getFavTypeName = (favType: string) => {
  switch (favType) {
    case 'REPORT': return '报表'
    case 'WARNING': return '预警'
    case 'ANALYSIS': return '分析'
    case 'TALENT': return '人才'
    default: return '未知'
  }
}

// 格式化日期
const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 处理页面大小改变
const handleSizeChange = (size: number) => {
  queryParams.pageSize = size
  queryParams.pageNum = 1
  loadFavorites()
}

// 处理当前页改变
const handleCurrentChange = (page: number) => {
  queryParams.pageNum = page
  loadFavorites()
}

// 组件挂载时加载数据
onMounted(() => {
  loadFavorites()
})
</script>

<style scoped>
.favorite-management-container {
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

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}
</style>