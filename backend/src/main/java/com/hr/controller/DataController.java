package com.hr.controller;

import com.hr.common.Response;
import com.hr.model.dto.WarningVO;
import com.hr.model.entity.DataCategory;
import com.hr.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据控制器 - 看板、分类、预警
 * 依据：开题报告 3.2.1 节、项目功能设计 2、3
 */
@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * 获取数据分类树（8大分类）
     */
    @GetMapping("/category/tree")
    public Response<List<DataCategory>> categoryTree() {
        List<DataCategory> list = dataService.getCategoryTree();
        return Response.success(list);
    }

    /**
     * 首页关键指标（模拟数据，后续接 Hive）
     */
    @GetMapping("/dashboard/indicators")
    public Response<Map<String, Object>> indicators() {
        Map<String, Object> map = new HashMap<>();
        map.put("orgEfficiency", 85.2);
        map.put("talentHealth", 78.5);
        map.put("costRatio", "38.7%");
        map.put("performanceRate", "82.1%");
        return Response.success(map);
    }

    /**
     * 默认看板数据
     */
    @GetMapping("/dashboard/default")
    public Response<Map<String, Object>> dashboardDefault() {
        return indicators();
    }
}
