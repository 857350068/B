package com.hr.service;

import com.hr.model.dto.FavoriteDTO;
import com.hr.model.dto.PageResult;
import com.hr.model.entity.Favorite;
import java.util.List;

/**
 * 收藏服务接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
public interface FavoriteService {
    
    /**
     * 添加收藏
     * 
     * @param favoriteDTO 收藏信息
     * @return 收藏对象
     */
    Favorite addFavorite(FavoriteDTO favoriteDTO);
    
    /**
     * 删除收藏
     * 
     * @param id 收藏ID
     * @return 是否删除成功
     */
    boolean deleteFavorite(Long id);
    
    /**
     * 获取用户收藏列表
     * 
     * @param userId 用户ID
     * @param favType 收藏类型
     * @return 收藏列表
     */
    List<Favorite> getUserFavorites(Long userId, String favType);
    
    /**
     * 获取用户收藏分页列表
     * 
     * @param userId 用户ID
     * @param favType 收藏类型
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageResult<Favorite> getUserFavoritesPage(Long userId, String favType, String keyword, Long pageNum, Long pageSize);
    
    /**
     * 检查是否已收藏
     * 
     * @param userId 用户ID
     * @param favType 收藏类型
     * @param favoriteId 收藏对象ID
     * @return 是否已收藏
     */
    boolean isFavorited(Long userId, String favType, Long favoriteId);
    
    /**
     * 获取收藏总数
     * 
     * @param userId 用户ID
     * @return 收藏总数
     */
    Long getFavoriteCount(Long userId);
}