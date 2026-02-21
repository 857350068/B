package com.hr.service;

import com.hr.model.entity.DataCategory;

import java.util.List;

/**
 * 数据服务接口
 */
public interface DataService {

    List<DataCategory> getCategoryTree();
}
