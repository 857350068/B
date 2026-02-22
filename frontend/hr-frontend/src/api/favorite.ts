import request from './request'

// 收藏相关接口

// 收藏DTO类型定义
export interface FavoriteDTO {
  userId?: number
  favType: string
  favoriteId: number
  title: string
}

// 收藏实体类型定义
export interface Favorite {
  id: number
  userId: number
  favType: string
  favoriteId: number
  title: string
  createTime: string
}

// 添加收藏
export function addFavorite(data: Omit<FavoriteDTO, 'userId'>) {
  return request.post<Favorite>('/favorite', data)
}

// 删除收藏
export function deleteFavorite(id: number) {
  return request.delete<boolean>(`/favorite/${id}`)
}

// 获取用户收藏列表
export function getFavoriteList(favType?: string) {
  return request.get<Favorite[]>('/favorite', {
    params: favType ? { favType } : {}
  })
}

// 检查是否已收藏
export function getFavoriteCheckStatus(favType: string, favoriteId: number) {
  return request.get<boolean>('/favorite/check', {
    params: { favType, favoriteId }
  })
}

// 获取收藏总数
export function getFavoriteCount() {
  return request.get<number>('/favorite/count')
}

// 获取用户收藏分页列表
export interface PageResult<T> {
  pageNum: number
  pageSize: number
  total: number
  pages: number
  records: T[]
}

export function getFavoritePage(favType?: string, keyword?: string, pageNum: number = 1, pageSize: number = 10) {
  return request.get<PageResult<Favorite>>('/favorite/page', {
    params: {
      favType,
      keyword,
      pageNum,
      pageSize
    }
  })
}