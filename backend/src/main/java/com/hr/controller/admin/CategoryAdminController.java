package com.hr.controller.admin;

import com.hr.common.Response;
import com.hr.model.entity.DataCategory;
import com.hr.service.DataCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理控制器
 * 提供数据分类的增删改查功能
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/admin/categories")
public class CategoryAdminController extends AdminBaseController {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CategoryAdminController.class);
    
    @Autowired
    private DataCategoryService dataCategoryService;
    
    /**
     * 获取分类树
     * 
     * @return 分类树列表
     */
    @GetMapping
    public Response<List<DataCategory>> getCategoryTree() {
        try {
            List<DataCategory> categories = dataCategoryService.getCategoryTree();
            return success(categories);
        } catch (Exception e) {
            log.error("获取分类树失败: ", e);
            return error(500, "获取分类树失败: " + e.getMessage());
        }
    }
    
    /**
     * 添加分类
     * 
     * @param category 分类信息
     * @return 添加结果
     */
    @PostMapping
    public Response<DataCategory> addCategory(@RequestBody DataCategory category) {
        try {
            boolean result = dataCategoryService.save(category);
            if (!result) {
                return error(500, "添加分类失败");
            }
            // 查询保存后的完整分类信息
            DataCategory savedCategory = dataCategoryService.getById(category.getId());
            return success(savedCategory);
        } catch (Exception e) {
            log.error("添加分类失败: ", e);
            return error(500, "添加分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新分类
     * 
     * @param id 分类ID
     * @param category 分类信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Response<DataCategory> updateCategory(@PathVariable Long id, @RequestBody DataCategory category) {
        try {
            category.setId(id);
            boolean result = dataCategoryService.updateById(category);
            if (!result) {
                return error(500, "更新分类失败");
            }
            return success(category);
        } catch (Exception e) {
            log.error("更新分类失败: id={}", id, e);
            return error(500, "更新分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除分类
     * 
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Response<Void> deleteCategory(@PathVariable Long id) {
        try {
            boolean result = dataCategoryService.removeById(id);
            return result ? success() : error(500, "删除分类失败");
        } catch (Exception e) {
            log.error("删除分类失败: id={}", id, e);
            return error(500, "删除分类失败: " + e.getMessage());
        }
    }
    
    /**
     * 重新排序分类
     * 
     * @param categories 排序后的分类列表
     * @return 排序结果
     */
    @PutMapping("/sort")
    public Response<Void> sortCategories(@RequestBody List<DataCategory> categories) {
        try {
            // 需要实现排序逻辑
            log.info("开始重新排序分类");
            return success();
        } catch (Exception e) {
            log.error("重新排序分类失败: ", e);
            return error(500, "排序分类失败: " + e.getMessage());
        }
    }
}