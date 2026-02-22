package com.hr.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据分类实体
 * 依据：项目功能设计 2.1 - 8大分类
 */
@Data
@TableName("hr_data_category")
public class DataCategory {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private Date createTime;
    private Date updateTime;
    
    // 用于树形结构的子节点
    private List<DataCategory> children = new ArrayList<>();
    
    // getter和setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public List<DataCategory> getChildren() {
        return children;
    }
    
    public void setChildren(List<DataCategory> children) {
        this.children = children;
    }
}
