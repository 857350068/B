package com.hr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.model.dto.DataAnalysisCreateDTO;
import com.hr.model.dto.DataAnalysisQueryDTO;
import com.hr.model.dto.DataAnalysisUpdateDTO;
import com.hr.model.entity.DataAnalysis;

import java.util.List;

/**
 * 数据分析服务接口
 * 提供数据分析相关的业务逻辑
 * 
 * @author HR Datacenter
 * @since 1.0.0
 */
public interface DataAnalysisService extends IService<DataAnalysis> {
    
    /**
     * 创建数据分析任务
     * 
     * @param createDTO 创建参数
     * @param userId 当前用户ID
     * @return 创建的数据分析对象
     */
    DataAnalysis createAnalysis(DataAnalysisCreateDTO createDTO, Long userId);
    
    /**
     * 更新数据分析任务
     * 
     * @param updateDTO 更新参数
     * @param userId 当前用户ID
     * @return 更新后的数据分析对象
     */
    DataAnalysis updateAnalysis(DataAnalysisUpdateDTO updateDTO, Long userId);
    
    /**
     * 删除数据分析任务
     * 
     * @param id 分析任务ID
     * @param userId 当前用户ID
     */
    void deleteAnalysis(Long id, Long userId);
    
    /**
     * 分页查询数据分析任务
     * 
     * @param queryDTO 查询参数
     * @param userId 当前用户ID
     * @return 分页结果
     */
    IPage<DataAnalysis> queryAnalysisPage(DataAnalysisQueryDTO queryDTO, Long userId);
    
    /**
     * 根据ID获取数据分析详情
     * 
     * @param id 分析任务ID
     * @param userId 当前用户ID
     * @return 数据分析对象
     */
    DataAnalysis getAnalysisById(Long id, Long userId);
    
    /**
     * 执行数据分析任务
     * 
     * @param id 分析任务ID
     * @param userId 当前用户ID
     * @return 执行结果
     */
    Object executeAnalysis(Long id, Long userId);
    
    /**
     * 获取可执行的分析任务列表
     * 
     * @return 可执行的分析任务列表
     */
    List<DataAnalysis> getExecutableAnalysis();
    
    /**
     * 批量执行分析任务
     * 
     * @param userId 当前用户ID
     * @return 执行结果统计
     */
    String batchExecuteAnalysis(Long userId);
}