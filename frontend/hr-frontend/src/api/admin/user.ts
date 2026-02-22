import request from '@/api/request'

// 用户管理相关接口

// 用户管理DTO类型定义
export interface UserAdminDTO {
  id?: number
  username: string
  name: string
  role: string
  deptId: number
  deptName?: string
  phone?: string
  email?: string
  status: number
}

// 分页获取用户列表
export function getUserPage(
  keyword?: string, 
  role?: string, 
  deptId?: number, 
  pageNum: number = 1, 
  pageSize: number = 10
) {
  return request.get<PageResult<UserAdminDTO>>('/admin/users', {
    params: {
      keyword,
      role,
      deptId,
      pageNum,
      pageSize
    }
  })
}

// 获取用户详情
export function getUserById(id: number) {
  return request.get<UserAdminDTO>(`/admin/users/${id}`)
}

// 添加用户
export function addUser(data: Omit<UserAdminDTO, 'id'>) {
  return request.post<UserAdminDTO>('/admin/users', data)
}

// 更新用户
export function updateUser(id: number, data: Partial<UserAdminDTO>) {
  return request.put<void>(`/admin/users/${id}`, data)
}

// 删除用户
export function deleteUser(id: number) {
  return request.delete<void>(`/admin/users/${id}`)
}

// 批量删除用户
export function batchDeleteUsers(ids: number[]) {
  return request.delete<void>('/admin/users', { data: ids })
}

// 切换用户状态
export function toggleUserStatus(id: number, status: number) {
  return request.put<void>(`/admin/users/${id}/status`, { status })
}

// 分页结果类型定义
export interface PageResult<T> {
  pageNum: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}