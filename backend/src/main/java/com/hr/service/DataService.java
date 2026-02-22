package com.hr.service;

import com.hr.model.entity.DataCategory;

import java.util.List;

/**
 * 数据服务接口
 */
public interface DataService {

    List<DataCategory> getCategoryTree();
    
    /**
     * 同步数据到Hive
     * 
     * @return 同步是否成功
     */
    boolean syncToHive();
    
    /**
     * 从Excel导入数据
     * 
     * @param filePath Excel文件路径
     * @return 导入是否成功
     */
    boolean importFromExcel(String filePath);
    
    /**
     * 导出数据到Excel
     * 
     * @param filePath Excel文件路径
     * @return 导出是否成功
     */
    boolean exportToExcel(String filePath);
}
