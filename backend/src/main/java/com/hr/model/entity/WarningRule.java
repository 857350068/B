package com.hr.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * 预警规则实体
 * 依据：开题报告 3.2.2 节
 */
@Data
public class WarningRule {

    private Long id;
    private String ruleType;
    private Float threshold;
    private String description;
    private Integer effective;
    private String updateLog;
    private Date createTime;
    private Date updateTime;
}
