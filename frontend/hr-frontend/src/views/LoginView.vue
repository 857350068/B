<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>人力资源数据中心</h2>
        <p>HR Data Center System</p>
      </div>
      
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="loginRules" 
        class="login-form"
        @submit.prevent="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <p>默认账号: admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'
import request from '@/api/request'
import type { LoginResponse } from '@/api/auth'

// 路由
const router = useRouter()

// 用户store
const userStore = useUserStore()

// 表单引用
const loginFormRef = ref<FormInstance>()

// 加载状态
const loading = ref(false)

// 登录表单数据
const loginForm = reactive({
  username: 'admin',
  password: '123456'
})

// 表单验证规则
const loginRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, message: '密码长度不能少于5位', trigger: 'blur' }
  ]
})

// 登录处理
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 调用登录接口
        const response: LoginResponse = await request.post('/api/auth/login', {
          username: loginForm.username,
          password: loginForm.password
        })
        
        // 由于响应拦截器已处理，直接使用response
        userStore.setToken(response.token || '')
        userStore.setUserInfo({
          id: response.id || 0,
          username: response.username || '',
          role: response.role || '',
          deptId: response.deptId || null,
          deptName: response.deptName || null,
          deptScope: response.deptScope ? JSON.parse(response.deptScope) : []
        })
        
        ElMessage.success('登录成功')
        
        // 跳转到首页
        router.push('/')
      } catch (error: any) {
        ElMessage.error(error.message || '登录失败')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  color: #333;
  margin-bottom: 10px;
  font-size: 24px;
}

.login-header p {
  color: #666;
  font-size: 14px;
}

.login-form {
  margin-bottom: 20px;
}

.login-button {
  width: 100%;
}

.login-footer {
  text-align: center;
  color: #999;
  font-size: 12px;
}
</style>