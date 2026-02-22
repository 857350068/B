import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'

// 创建axios实例
const request: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, msg, data } = response.data
    if (code === 200) {
      return data
    } else {
      // 处理业务错误
      console.error('API Error:', msg)
      if (code === 401) {
        // token过期，跳转到登录页
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(msg || '请求失败'))
    }
  },
  (error) => {
    console.error('Network Error:', error)
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default request