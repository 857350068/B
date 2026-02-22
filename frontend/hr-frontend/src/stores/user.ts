import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface UserInfo {
  id: number
  username: string
  role: string
  deptId: number | null
  deptName: string | null
  deptScope: number[]
}

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(
    localStorage.getItem('userInfo') 
      ? JSON.parse(localStorage.getItem('userInfo')!) 
      : null
  )

  // actions
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const clearAuth = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  const isLoggedIn = () => {
    return !!token.value && !!userInfo.value
  }

  const hasRole = (role: string) => {
    return userInfo.value?.role === role
  }

  const isAdmin = () => {
    return hasRole('HR_ADMIN')
  }

  const isDeptHead = () => {
    return hasRole('DEPT_HEAD')
  }

  const isManagement = () => {
    return hasRole('MANAGEMENT')
  }

  const isEmployee = () => {
    return hasRole('EMPLOYEE')
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    clearAuth,
    isLoggedIn,
    hasRole,
    isAdmin,
    isDeptHead,
    isManagement,
    isEmployee
  }
})