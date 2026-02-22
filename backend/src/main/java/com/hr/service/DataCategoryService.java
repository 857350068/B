package com.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.model.entity.DataCategory;

import java.util.List;

/**
 * 数据分类服务接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
public interface DataCategoryService extends IService<DataCategory> {
    
    /**
     * 获取分类树
     * 
     * @return 分类树列表
     */
    List<DataCategory> getCategoryTree();
    
    /**
     * 根据父级ID获取子分类
     * 
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<DataCategory> getChildCategories(Long parentId);
    
    /**
     * 移动分类
     * 
     * @param categoryId 分类ID
     * @param newParentId 新父级ID
     * @return 是否移动成功
     */
    boolean moveCategory(Long categoryId, Long newParentId);
}