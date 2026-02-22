package com.hr.model.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 收藏DTO
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Data
public class FavoriteDTO {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    /**
     * 收藏类型 (REPORT/WARNING/TALENT/ANALYSIS)
     */
    @NotBlank(message = "收藏类型不能为空")
    private String favType;
    
    /**
     * 收藏对象ID (报表ID/预警ID/分析ID等)
     */
    @NotNull(message = "收藏对象ID不能为空")
    private Long favoriteId;
    
    public Long getFavoriteId() {
        return favoriteId;
    }
    
    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }
    
    public String getFavType() {
        return favType;
    }
    
    public void setFavType(String favType) {
        this.favType = favType;
    }
    
    /**
     * 收藏标题
     */
    @NotBlank(message = "收藏标题不能为空")
    private String title;
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
}