package com.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hr.common.ErrorCode;
import com.hr.exception.BaseException;
import com.hr.mapper.DataAnalysisMapper;
import com.hr.model.dto.DataAnalysisCreateDTO;
import com.hr.model.dto.DataAnalysisQueryDTO;
import com.hr.model.dto.DataAnalysisUpdateDTO;
import com.hr.model.entity.DataAnalysis;
import com.hr.service.DataAnalysisService;
import com.hr.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据分析服务实现类
 * 提供数据分析相关的业务逻辑实现
 * 
 * @author HR Datacenter
 * @since 1.0.0
 */
@Slf4j
@Service
public class DataAnalysisServiceImpl extends ServiceImpl<DataAnalysisMapper, DataAnalysis> implements DataAnalysisService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataAnalysis createAnalysis(DataAnalysisCreateDTO createDTO, Long userId) {
        log.info("创建数据分析任务, 用户ID: {}, 分析名称: {}", userId, createDTO.getAnalysisName());
        
        // 检查分析名称是否已存在
        LambdaQueryWrapper<DataAnalysis> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataAnalysis::getAnalysisName, createDTO.getAnalysisName())
                   .eq(DataAnalysis::getDeleted, 0);
        if (this.count(queryWrapper) > 0) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "分析名称已存在");
        }
        
        // 构建数据分析对象
        DataAnalysis analysis = new DataAnalysis();
        analysis.setCategoryId(createDTO.getCategoryId());
        analysis.setAnalysisName(createDTO.getAnalysisName());
        analysis.setDescription(createDTO.getDescription());
        analysis.setAnalysisType(createDTO.getAnalysisType());
        analysis.setQueryStatement(createDTO.getQueryStatement());
        analysis.setParameters(createDTO.getParameters());
        analysis.setResultMapping(createDTO.getResultMapping());
        analysis.setChartConfig(createDTO.getChartConfig());
        analysis.setDataSource(createDTO.getDataSource());
        analysis.setExecutionFrequency(createDTO.getExecutionFrequency());
        analysis.setStatus("ACTIVE");
        analysis.setCreatedBy(userId);
        
        // 设置下次执行时间
        setNextExecutionTime(analysis);
        
        // 保存到数据库
        if (!this.save(analysis)) {
            throw new BaseException(ErrorCode.OPERATION_ERROR, "创建分析任务失败");
        }
        
        log.info("数据分析任务创建成功, ID: {}", analysis.getId());
        return analysis;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataAnalysis updateAnalysis(DataAnalysisUpdateDTO updateDTO, Long userId) {
        log.info("更新数据分析任务, 用户ID: {}, 分析ID: {}", userId, updateDTO.getId());
        
        // 检查分析任务是否存在
        DataAnalysis existingAnalysis = this.getById(updateDTO.getId());
        if (existingAnalysis == null || existingAnalysis.getDeleted() == 1) {
            throw new BaseException(ErrorCode.NOT_FOUND, "分析任务不存在");
        }
        
        // 检查权限（只有创建者或管理员可以修改）
        if (!existingAnalysis.getCreatedBy().equals(userId) && !SecurityUtil.isAdmin()) {
            throw new BaseException(ErrorCode.NO_AUTH_ERROR, "无权限修改此分析任务");
        }
        
        // 检查分析名称是否重复（排除自己）
        if (StringUtils.hasText(updateDTO.getAnalysisName())) {
            LambdaQueryWrapper<DataAnalysis> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DataAnalysis::getAnalysisName, updateDTO.getAnalysisName())
                       .ne(DataAnalysis::getId, updateDTO.getId())
                       .eq(DataAnalysis::getDeleted, 0);
            if (this.count(queryWrapper) > 0) {
                throw new BaseException(ErrorCode.PARAMS_ERROR, "分析名称已存在");
            }
        }
        
        // 更新分析任务
        DataAnalysis analysis = new DataAnalysis();
        analysis.setId(updateDTO.getId());
        analysis.setCategoryId(updateDTO.getCategoryId());
        analysis.setAnalysisName(updateDTO.getAnalysisName());
        analysis.setDescription(updateDTO.getDescription());
        analysis.setAnalysisType(updateDTO.getAnalysisType());
        analysis.setQueryStatement(updateDTO.getQueryStatement());
        analysis.setParameters(updateDTO.getParameters());
        analysis.setResultMapping(updateDTO.getResultMapping());
        analysis.setChartConfig(updateDTO.getChartConfig());
        analysis.setDataSource(updateDTO.getDataSource());
        analysis.setExecutionFrequency(updateDTO.getExecutionFrequency());
        analysis.setStatus(updateDTO.getStatus());
        
        // 如果状态变为激活且执行频率不为空，重新计算下次执行时间
        if ("ACTIVE".equals(updateDTO.getStatus()) && StringUtils.hasText(updateDTO.getExecutionFrequency())) {
            setNextExecutionTime(analysis);
        }
        
        if (!this.updateById(analysis)) {
            throw new BaseException(ErrorCode.OPERATION_ERROR, "更新分析任务失败");
        }
        
        log.info("数据分析任务更新成功, ID: {}", analysis.getId());
        return this.getById(analysis.getId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAnalysis(Long id, Long userId) {
        log.info("删除数据分析任务, 用户ID: {}, 分析ID: {}", userId, id);
        
        // 检查分析任务是否存在
        DataAnalysis existingAnalysis = this.getById(id);
        if (existingAnalysis == null || existingAnalysis.getDeleted() == 1) {
            throw new BaseException(ErrorCode.NOT_FOUND, "分析任务不存在");
        }
        
        // 检查权限（只有创建者或管理员可以删除）
        if (!existingAnalysis.getCreatedBy().equals(userId) && !SecurityUtil.isAdmin()) {
            throw new BaseException(ErrorCode.NO_AUTH_ERROR, "无权限删除此分析任务");
        }
        
        // 逻辑删除
        if (!this.removeById(id)) {
            throw new BaseException(ErrorCode.OPERATION_ERROR, "删除分析任务失败");
        }
        
        log.info("数据分析任务删除成功, ID: {}", id);
    }
    
    @Override
    public IPage<DataAnalysis> queryAnalysisPage(DataAnalysisQueryDTO queryDTO, Long userId) {
        log.info("分页查询数据分析任务, 用户ID: {}, 查询参数: {}", userId, queryDTO);
        
        // 构建分页对象
        Page<DataAnalysis> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        // 非管理员只能查看自己的分析任务
        if (!SecurityUtil.isAdmin()) {
            queryDTO.setCreatedBy(userId);
        }
        
        // 执行分页查询
        IPage<DataAnalysis> result = this.baseMapper.selectAnalysisPage(page, queryDTO);
        
        log.info("分页查询完成, 总记录数: {}", result.getTotal());
        return result;
    }
    
    @Override
    public DataAnalysis getAnalysisById(Long id, Long userId) {
        log.info("获取数据分析详情, 用户ID: {}, 分析ID: {}", userId, id);
        
        DataAnalysis analysis = this.getById(id);
        if (analysis == null || analysis.getDeleted() == 1) {
            throw new BaseException(ErrorCode.NOT_FOUND, "分析任务不存在");
        }
        
        // 非管理员只能查看自己的分析任务
        if (!SecurityUtil.isAdmin() && !analysis.getCreatedBy().equals(userId)) {
            throw new BaseException(ErrorCode.NO_AUTH_ERROR, "无权限查看此分析任务");
        }
        
        return analysis;
    }
    
    // 辅助方法：设置下次执行时间
    private void setNextExecutionTime(DataAnalysis analysis) {
        String frequency = analysis.getExecutionFrequency();
        if (!StringUtils.hasText(frequency)) {
            analysis.setNextExecutionTime(null);
            return;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextTime;
        
        switch (frequency) {
            case "DAILY":
                nextTime = now.plusDays(1);
                break;
            case "WEEKLY":
                nextTime = now.plusWeeks(1);
                break;
            case "MONTHLY":
                nextTime = now.plusMonths(1);
                break;
            default:
                nextTime = null;
                break;
        }
        
        analysis.setNextExecutionTime(nextTime);
    }
    
    @Override
    public Object executeAnalysis(Long id, Long userId) {
        log.info("执行数据分析任务, 用户ID: {}, 分析ID: {}", userId, id);
        
        // 检查分析任务是否存在
        DataAnalysis analysis = this.getById(id);
        if (analysis == null || analysis.getDeleted() == 1) {
            throw new BaseException(ErrorCode.NOT_FOUND, "分析任务不存在");
        }
        
        // 检查权限
        if (!analysis.getCreatedBy().equals(userId) && !SecurityUtil.isAdmin()) {
            throw new BaseException(ErrorCode.NO_AUTH_ERROR, "无权限执行此分析任务");
        }
        
        // 检查状态
        if (!"ACTIVE".equals(analysis.getStatus())) {
            throw new BaseException(ErrorCode.PARAMS_ERROR, "分析任务未激活");
        }
        
        // 执行分析逻辑（简化实现）
        Object result = executeQuery(analysis);
        
        // 更新执行时间
        analysis.setLastExecutionTime(LocalDateTime.now());
        setNextExecutionTime(analysis);
        this.updateById(analysis);
        
        log.info("数据分析任务执行完成, ID: {}", id);
        return result;
    }
    
    @Override
    public List<DataAnalysis> getExecutableAnalysis() {
        return this.baseMapper.selectExecutableAnalysis(LocalDateTime.now());
    }
    
    @Override
    public String batchExecuteAnalysis(Long userId) {
        log.info("批量执行数据分析任务, 用户ID: {}", userId);
        
        List<DataAnalysis> executableList = getExecutableAnalysis();
        int successCount = 0;
        int failCount = 0;
        
        for (DataAnalysis analysis : executableList) {
            try {
                executeAnalysis(analysis.getId(), userId);
                successCount++;
            } catch (Exception e) {
                log.error("执行分析任务失败, ID: {}", analysis.getId(), e);
                failCount++;
            }
        }
        
        String result = String.format("批量执行完成: 成功%d个, 失败%d个", successCount, failCount);
        log.info(result);
        return result;
    }
    
    /**
     * 执行查询逻辑（简化实现）
     * 实际项目中应该根据数据源类型执行不同的查询逻辑
     */
    private Object executeQuery(DataAnalysis analysis) {
        // 这里应该根据dataSource类型选择不同的数据源执行查询
        // 简化实现，返回模拟数据
        return "{\"result\":\"执行成功\",\"data\":[{\"id\":1,\"name\":\"示例数据\"}]}";
    }
}