package com.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hr.model.dto.DataAnalysisQueryDTO;
import com.hr.model.entity.DataAnalysis;
import org.apache.ibatis.annotations.Param;

/**
 * 数据分析Mapper接口
 * 提供数据分析相关的数据库操作
 * 
 * @author HR Datacenter
 * @since 1.0.0
 */
public interface DataAnalysisMapper extends BaseMapper<DataAnalysis> {
    
    /**
     * 分页查询数据分析任务
     * 
     * @param page 分页对象
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<DataAnalysis> selectAnalysisPage(Page<DataAnalysis> page, @Param("query") DataAnalysisQueryDTO queryDTO);
    
    /**
     * 根据分类ID查询分析任务数量
     * 
     * @param categoryId 分类ID
     * @return 分析任务数量
     */
    Integer countByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 查询需要执行的分析任务
     * 
     * @param currentTime 当前时间
     * @return 需要执行的分析任务列表
     */
    java.util.List<DataAnalysis> selectExecutableAnalysis(@Param("currentTime") java.time.LocalDateTime currentTime);
}