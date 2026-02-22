import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

// 路由配置
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/DashboardView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/DashboardView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/data-analysis',
    name: 'DataAnalysis',
    component: () => import('@/views/DataAnalysisView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/charts',
    name: 'Charts',
    component: () => import('@/views/ChartsView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/ProfileView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/favorites',
    name: 'Favorites',
    component: () => import('@/views/FavoriteManagementView.vue'),
    meta: { requiresAuth: true }
  },
  // 管理员路由
  {
    path: '/admin',
    name: 'Admin',
    redirect: '/admin/index',
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: 'index',
        name: 'AdminIndex',
        component: () => import('@/views/admin/IndexView.vue')
      },
      {
        path: 'users',
        name: 'UserManagement',
        component: () => import('@/views/admin/UserManagementView.vue')
      },
      {
        path: 'data',
        name: 'DataManagement',
        component: () => import('@/views/admin/DataManagementView.vue')
      },
      {
        path: 'rules',
        name: 'RuleManagement',
        component: () => import('@/views/admin/RuleManagementView.vue')
      },
      {
        path: 'reports',
        name: 'ReportManagement',
        component: () => import('@/views/admin/ReportManagementView.vue')
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFoundView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from) => {
  const userStore = useUserStore()
  
  // 检查是否需要认证
  if (to.meta.requiresAuth && !userStore.isLoggedIn()) {
    return '/login'
  } 
  // 检查是否需要管理员权限
  else if (to.meta.requiresAdmin && !userStore.isAdmin()) {
    // 如果用户未登录，重定向到登录页
    if (!userStore.isLoggedIn()) {
      return '/login'
    } 
    // 如果用户已登录但不是管理员，重定向到首页
    else {
      ElMessage.error('您没有权限访问此页面')
      return '/'
    }
  }
  else if (to.name === 'Login' && userStore.isLoggedIn()) {
    // 已登录用户访问登录页，重定向到首页
    return '/'
  }
  // 允许导航继续
})

export default router
