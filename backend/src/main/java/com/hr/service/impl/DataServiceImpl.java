package com.hr.service.impl;

import com.hr.mapper.DataCategoryMapper;
import com.hr.model.entity.DataCategory;
import com.hr.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据服务实现
 * 依据：项目功能设计 2.1 - 数据分类树
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DataCategoryMapper dataCategoryMapper;

    @Override
    public List<DataCategory> getCategoryTree() {
        return dataCategoryMapper.selectAll();
    }
}
