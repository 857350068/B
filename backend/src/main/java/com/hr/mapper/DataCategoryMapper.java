package com.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hr.model.entity.DataCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数据分类映射
 * 依据：项目功能设计 2.1
 */
@Mapper
public interface DataCategoryMapper extends BaseMapper<DataCategory> {

    List<DataCategory> selectAll();

    List<DataCategory> selectByParentId(Long parentId);
}
