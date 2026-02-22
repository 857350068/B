package com.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hr.model.entity.ReportTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报表模板映射器
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Mapper
public interface ReportTemplateMapper extends BaseMapper<ReportTemplate> {
}