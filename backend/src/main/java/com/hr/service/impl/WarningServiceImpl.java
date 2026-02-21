package com.hr.service.impl;

import com.hr.mapper.WarningRuleMapper;
import com.hr.model.dto.WarningVO;
import com.hr.model.entity.WarningRule;
import com.hr.service.WarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预警服务实现
 * 依据：开题报告 3.2.1 节 - 基于 warning_rule 动态计算
 */
@Service
public class WarningServiceImpl implements WarningService {

    @Autowired
    private WarningRuleMapper warningRuleMapper;

    @Override
    public List<WarningVO> getWarningList() {
        // 从 warning_rule 表读取生效规则，后续接 Hive 计算实际触发
        warningRuleMapper.selectEffective();
        List<WarningVO> result = new ArrayList<>();
        // 模拟预警数据（后续接 Hive 实际计算）
        result.add(createVO("员工流失预警", "销售部", "12.5%", "8%", "高于行业平均4.5%"));
        result.add(createVO("人才缺口预警", "研发部", "5人", "3人", "关键岗位缺编"));
        return result;
    }

    private WarningVO createVO(String type, String dept, String value, String threshold, String detail) {
        WarningVO vo = new WarningVO();
        vo.setType(type);
        vo.setDept(dept);
        vo.setValue(value);
        vo.setThreshold(threshold);
        vo.setDetail(detail);
        return vo;
    }
}
