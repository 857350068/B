package com.hr.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏实体类
 * 对应 sys_favorite 表
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Data
@TableName("sys_favorite")
public class Favorite {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
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
    private String favType;
    
    public String getFavType() {
        return favType;
    }
    
    public void setFavType(String favType) {
        this.favType = favType;
    }
    
    /**
     * 收藏对象ID (报表ID/预警ID/分析ID等)
     */
    private Long favoriteId;
    
    public Long getFavoriteId() {
        return favoriteId;
    }
    
    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }
    
    /**
     * 收藏标题
     */
    private String title;
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}