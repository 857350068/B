import request from './request'

// 用户登录请求参数
export interface LoginRequest {
  username: string
  password: string
}

// 用户信息
export interface UserInfo {
  id: number
  username: string
  role: string
  deptId?: number
  deptName?: string
  deptScope?: string
}

// 登录响应
export interface LoginResponse {
  token: string
  id: number
  username: string
  role: string
  deptId?: number
  deptName?: string
  deptScope?: string
}

// 登录接口
export function login(data: LoginRequest) {
  return request.post<LoginResponse>('/api/auth/login', data)
}

// 获取用户信息
export function getProfile() {
  return request.get<UserInfo>('/api/auth/profile')
}