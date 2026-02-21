package com.hr.model.dto;

import lombok.Data;

/**
 * 预警信息 VO
 * 依据：项目功能设计 3.2
 */
@Data
public class WarningVO {

    private String type;
    private String dept;
    private String value;
    private String threshold;
    private String detail;
}
