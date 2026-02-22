package com.hr.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 预警规则实体
 * 依据：开题报告 3.2.2 节
 */
@TableName("warning_rule")
public class WarningRule {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 规则类型
     */
    private String ruleType;
    
    /**
     * 阈值
     */
    private Float threshold;
    
    /**
     * 规则描述
     */
    private String description;
    
    /**
     * 是否生效 (0-否, 1-是)
     */
    private Integer effective;
    
    /**
     * 更新日志
     */
    private String updateLog;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 规则版本号
     */
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public Float getThreshold() {
        return threshold;
    }

    public void setThreshold(Float threshold) {
        this.threshold = threshold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEffective() {
        return effective;
    }

    public void setEffective(Integer effective) {
        this.effective = effective;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
