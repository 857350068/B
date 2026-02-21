package com.hr.service;

import com.hr.model.dto.WarningVO;

import java.util.List;

/**
 * 预警服务接口
 */
public interface WarningService {

    List<WarningVO> getWarningList();
}
