package com.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hr.common.ErrorCode;
import com.hr.exception.BaseException;
import com.hr.mapper.FavoriteMapper;
import com.hr.model.dto.FavoriteDTO;
import com.hr.model.dto.PageResult;
import com.hr.model.entity.Favorite;
import com.hr.service.FavoriteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收藏服务实现类
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FavoriteServiceImpl.class);
    
    @Autowired
    private FavoriteMapper favoriteMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Favorite addFavorite(FavoriteDTO favoriteDTO) {
        // 检查是否已收藏
        if (isFavorited(favoriteDTO.getUserId(), favoriteDTO.getFavType(), favoriteDTO.getFavoriteId())) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "该内容已被收藏");
        }
        
        // 创建收藏对象
        Favorite favorite = new Favorite();
        BeanUtils.copyProperties(favoriteDTO, favorite);
        favorite.setCreateTime(LocalDateTime.now());
        
        // 保存到数据库
        if (favoriteMapper.insert(favorite) <= 0) {
            throw new BaseException(ErrorCode.OPERATION_ERROR, "添加收藏失败");
        }
        
        log.info("用户 {} 添加收藏: type={}, id={}, title={}", 
                favoriteDTO.getUserId(), favoriteDTO.getFavType(), 
                favoriteDTO.getFavoriteId(), favoriteDTO.getTitle());
        
        return favorite;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFavorite(Long id) {
        if (id == null || id <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "收藏ID不能为空");
        }
        
        int result = favoriteMapper.deleteById(id);
        
        if (result > 0) {
            log.info("删除收藏: id={}", id);
        } else {
            log.warn("删除收藏失败: id={} (可能不存在)", id);
        }
        
        return result > 0;
    }
    
    @Override
    public List<Favorite> getUserFavorites(Long userId, String favType) {
        if (userId == null || userId <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId);
        
        if (favType != null && !favType.trim().isEmpty()) {
            queryWrapper.eq(Favorite::getFavType, favType);
        }
        
        queryWrapper.orderByDesc(Favorite::getCreateTime);
        
        return favoriteMapper.selectList(queryWrapper);
    }
    
    @Override
    public PageResult<Favorite> getUserFavoritesPage(Long userId, String favType, String keyword, Long pageNum, Long pageSize) {
        if (userId == null || userId <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1L;
        }
        
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10L;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId);
        
        if (favType != null && !favType.trim().isEmpty()) {
            queryWrapper.eq(Favorite::getFavType, favType);
        }
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like(Favorite::getTitle, keyword);
        }
        
        queryWrapper.orderByDesc(Favorite::getCreateTime);
        
        // 使用MyBatis-Plus的分页插件方式查询
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Favorite> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
        com.baomidou.mybatisplus.core.metadata.IPage<Favorite> result = favoriteMapper.selectPage(page, queryWrapper);
        
        return PageResult.of(pageNum, pageSize, result.getTotal(), result.getRecords());
    }
    
    @Override
    public boolean isFavorited(Long userId, String favType, Long favoriteId) {
        if (userId == null || userId <= 0 || favType == null || favType.trim().isEmpty() || favoriteId == null || favoriteId <= 0) {
            return false;
        }
        
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId)
                   .eq(Favorite::getFavType, favType)
                   .eq(Favorite::getFavoriteId, favoriteId);
        
        return favoriteMapper.selectCount(queryWrapper) > 0;
    }
    
    @Override
    public Long getFavoriteCount(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "用户ID不能为空");
        }
        
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId);
        
        return favoriteMapper.selectCount(queryWrapper).longValue();
    }
}