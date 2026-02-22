package com.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hr.mapper.DataCategoryMapper;
import com.hr.model.entity.DataCategory;
import com.hr.service.DataCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据分类服务实现类
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Service
public class DataCategoryServiceImpl extends ServiceImpl<DataCategoryMapper, DataCategory> implements DataCategoryService {
    
    @Autowired
    private DataCategoryMapper dataCategoryMapper;
    
    @Override
    public List<DataCategory> getCategoryTree() {
        // 获取所有分类
        List<DataCategory> allCategories = dataCategoryMapper.selectAll();
        
        // 构建树形结构
        return buildCategoryTree(allCategories, 0L);
    }
    
    @Override
    public List<DataCategory> getChildCategories(Long parentId) {
        return dataCategoryMapper.selectByParentId(parentId);
    }
    
    @Override
    public boolean moveCategory(Long categoryId, Long newParentId) {
        DataCategory category = dataCategoryMapper.selectById(categoryId);
        if (category == null) {
            return false;
        }
        
        category.setParentId(newParentId);
        int result = dataCategoryMapper.updateById(category);
        return result > 0;
    }
    
    /**
     * 构建分类树
     * 
     * @param allCategories 所有分类
     * @param parentId 父级ID
     * @return 分类树
     */
    private List<DataCategory> buildCategoryTree(List<DataCategory> allCategories, Long parentId) {
        return allCategories.stream()
                .filter(category -> parentId.equals(category.getParentId()))
                .sorted(Comparator.comparingInt(cat -> cat.getSortOrder() != null ? cat.getSortOrder() : 0))
                .peek(category -> {
                    // 递归设置子节点
                    List<DataCategory> children = buildCategoryTree(allCategories, category.getId());
                    category.setChildren(children);
                })
                .collect(Collectors.toList());
    }
}