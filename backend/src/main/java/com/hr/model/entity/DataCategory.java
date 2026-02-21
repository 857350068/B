package com.hr.model.entity;

import lombok.Data;

/**
 * 数据分类实体
 * 依据：项目功能设计 2.1 - 8大分类
 */
@Data
public class DataCategory {

    private Long id;
    private String name;
    private Long parentId;
    private Integer sortOrder;
}
