package com.hr.controller;

import com.hr.common.Response;
import com.hr.model.dto.WarningVO;
import com.hr.service.WarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预警控制器
 * 依据：开题报告 3.2.1 节
 */
@RestController
@RequestMapping("/api/warning")
public class WarningController {

    @Autowired
    private WarningService warningService;

    /**
     * 获取预警信息列表
     */
    @GetMapping
    public Response<List<WarningVO>> list() {
        List<WarningVO> list = warningService.getWarningList();
        return Response.success(list);
    }
}
