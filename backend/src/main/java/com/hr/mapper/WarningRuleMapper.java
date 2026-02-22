package com.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hr.model.entity.WarningRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 预警规则映射
 * 依据：开题报告 3.2.2 节
 */
@Mapper
public interface WarningRuleMapper extends BaseMapper<WarningRule> {

    List<WarningRule> selectEffective();
}
