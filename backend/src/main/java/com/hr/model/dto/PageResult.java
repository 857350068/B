package com.hr.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 分页结果封装类
 *
 * @author hr-system
 * @since 2026-02-21
 */
@Data
public class PageResult<T> {
    
    /**
     * 当前页码
     */
    private Long pageNum;
    
    /**
     * 每页大小
     */
    private Long pageSize;
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 总页数
     */
    private Long pages;
    
    /**
     * 数据列表
     */
    private List<T> records;
    
    public PageResult() {
    }
    
    public PageResult(Long pageNum, Long pageSize, Long total, List<T> records) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.records = records;
        this.pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }
    
    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(Long pageNum, Long pageSize, Long total, List<T> records) {
        return new PageResult<>(pageNum, pageSize, total, records);
    }
}