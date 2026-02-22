package com.hr.controller;

import com.hr.common.Response;
import com.hr.model.dto.FavoriteDTO;
import com.hr.model.dto.PageResult;
import com.hr.model.entity.Favorite;
import com.hr.service.FavoriteService;
import com.hr.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏控制器
 * 提供收藏相关的API接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FavoriteController.class);
    
    /**
     * 从请求头中获取当前用户ID
     * 
     * @return 用户ID
     */
    private Long getCurrentUserIdFromRequest() {
        // 在实际实现中，应该从JWT token中解析用户ID
        // 这里简化处理，实际应用中需要从Security Context获取
        // 从SecurityContextHolder获取认证信息
        org.springframework.security.core.Authentication auth = 
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            // 如果实现了JWT过滤器，应该能从其中获取用户信息
            // 这里返回一个模拟值，实际应用中需要正确解析JWT
            // 通常会在JWT过滤器中将用户ID设置到Security Context中
            if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                org.springframework.security.core.userdetails.UserDetails userDetails = 
                    (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal();
                // 实际应用中需要从UserDetails或额外属性中获取ID
            } else if (auth.getPrincipal() != null && auth.getPrincipal() instanceof String) {
                // 如果是字符串，尝试解析为ID
                String principal = (String) auth.getPrincipal();
                try {
                    return Long.parseLong(principal);
                } catch (NumberFormatException e) {
                    // 如果不是数字，说明不是用户ID
                }
            }
        }
        
        // 这里返回null，实际应用中需要在JWT过滤器中正确设置用户信息
        return null;
    }
    
    @Autowired
    private FavoriteService favoriteService;
    
    /**
     * 添加收藏
     * 
     * @param favoriteDTO 收藏信息
     * @return 收藏结果
     */
    @PostMapping
    public Response<Favorite> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        try {
            // 设置当前用户ID - 从JWT Token中获取
            Long userId = getCurrentUserIdFromRequest();
            if (userId == null) {
                return Response.error(401, "用户未登录");
            }
            favoriteDTO.setUserId(userId);
            
            Favorite favorite = favoriteService.addFavorite(favoriteDTO);
            return Response.success(favorite);
        } catch (Exception e) {
            log.error("添加收藏失败: ", e);
            return Response.error(500, "添加收藏失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除收藏
     * 
     * @param id 收藏ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Response<Boolean> deleteFavorite(@PathVariable Long id) {
        try {
            // 验证收藏属于当前用户
            Long userId = getCurrentUserIdFromRequest();
            if (userId == null) {
                return Response.error(401, "用户未登录");
            }
            
            // 在实际实现中，需要先查询收藏记录确认归属关系
            // 这里简化处理，只做基本参数验证
            if (id == null || id <= 0) {
                return Response.error(400, "收藏ID不能为空");
            }
            
            boolean result = favoriteService.deleteFavorite(id);
            return Response.success(result);
        } catch (Exception e) {
            log.error("删除收藏失败: id={}", id, e);
            return Response.error(500, "删除收藏失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户收藏列表
     * 
     * @param favType 收藏类型（可选）
     * @return 收藏列表
     */
    @GetMapping
    public Response<List<Favorite>> getUserFavorites(@RequestParam(required = false) String favType) {
        try {
            Long userId = getCurrentUserIdFromRequest();
            if (userId == null) {
                return Response.error(401, "用户未登录");
            }
            List<Favorite> favorites = favoriteService.getUserFavorites(userId, favType);
            return Response.success(favorites);
        } catch (Exception e) {
            log.error("获取收藏列表失败: ", e);
            return Response.error(500, "获取收藏列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户收藏分页列表
     * 
     * @param favType 收藏类型（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页收藏列表
     */
    @GetMapping("/page")
    public Response<PageResult<Favorite>> getUserFavoritesPage(
            @RequestParam(required = false) String favType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            Long userId = getCurrentUserIdFromRequest();
            if (userId == null) {
                return Response.error(401, "用户未登录");
            }
            PageResult<Favorite> favorites = favoriteService.getUserFavoritesPage(userId, favType, keyword, pageNum, pageSize);
            return Response.success(favorites);
        } catch (Exception e) {
            log.error("获取收藏分页列表失败: ", e);
            return Response.error(500, "获取收藏分页列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查是否已收藏
     * 
     * @param favType 收藏类型
     * @param favoriteId 收藏对象ID
     * @return 是否已收藏
     */
    @GetMapping("/check")
    public Response<Boolean> checkFavorite(@RequestParam String favType, @RequestParam Long favoriteId) {
        try {
            Long userId = getCurrentUserIdFromRequest();
            if (userId == null) {
                return Response.error(401, "用户未登录");
            }
            boolean isFavorited = favoriteService.isFavorited(userId, favType, favoriteId);
            return Response.success(isFavorited);
        } catch (Exception e) {
            log.error("检查收藏状态失败: type={}, id={}", favType, favoriteId, e);
            return Response.error(500, "检查收藏状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取收藏总数
     * 
     * @return 收藏总数
     */
    @GetMapping("/count")
    public Response<Long> getFavoriteCount() {
        try {
            Long userId = getCurrentUserIdFromRequest();
            if (userId == null) {
                return Response.error(401, "用户未登录");
            }
            Long count = favoriteService.getFavoriteCount(userId);
            return Response.success(count);
        } catch (Exception e) {
            log.error("获取收藏总数失败: ", e);
            return Response.error(500, "获取收藏总数失败: " + e.getMessage());
        }
    }
}