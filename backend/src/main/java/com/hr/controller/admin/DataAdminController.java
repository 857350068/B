package com.hr.controller.admin;

import com.hr.common.Response;
import com.hr.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 数据管理控制器
 * 提供数据同步、导入、导出等管理功能
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/admin/data")
public class DataAdminController extends AdminBaseController {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataAdminController.class);
    
    @Autowired
    private DataService dataService;
    
    /**
     * 同步数据到Hive
     * 
     * @return 同步结果
     */
    @PostMapping("/sync")
    public Response<Void> syncDataToHive() {
        try {
            log.info("开始同步数据到Hive");
            boolean result = dataService.syncToHive();
            if (result) {
                log.info("数据同步到Hive成功");
                return success();
            } else {
                log.error("数据同步到Hive失败");
                return error(500, "数据同步失败");
            }
        } catch (Exception e) {
            log.error("同步数据到Hive失败: ", e);
            return error(500, "同步数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 从Excel导入数据
     * 
     * @param file 上传的Excel文件
     * @return 导入结果
     */
    @PostMapping("/import")
    public Response<Void> importDataFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return error(400, "请选择要上传的文件");
            }
            
            // 获取文件名并生成唯一文件名
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = System.getProperty("java.io.tmpdir") + "/" + fileName;
            
            // 保存文件到临时目录
            File destFile = new File(filePath);
            file.transferTo(destFile);
            
            log.info("开始从Excel导入数据: {}", filePath);
            boolean result = dataService.importFromExcel(filePath);
            if (result) {
                log.info("从Excel导入数据成功");
                return success();
            } else {
                log.error("从Excel导入数据失败");
                return error(500, "导入数据失败");
            }
        } catch (Exception e) {
            log.error("从Excel导入数据失败: ", e);
            return error(500, "导入数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 导出数据到Excel
     * 
     * @param response HttpServletResponse对象，用于下载文件
     * @param fileName 导出的文件名
     */
    @GetMapping("/export")
    public void exportDataToExcel(HttpServletResponse response, 
                                  @RequestParam(value = "fileName", defaultValue = "data_export.xlsx") String fileName) {
        try {
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            
            String filePath = System.getProperty("java.io.tmpdir") + "/" + fileName;
            
            log.info("开始导出数据到Excel: {}", filePath);
            boolean result = dataService.exportToExcel(filePath);
            if (result) {
                log.info("导出数据到Excel成功");
                
                // 将文件内容写入响应流
                File file = new File(filePath);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    OutputStream os = response.getOutputStream();
                    
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    
                    fis.close();
                    os.flush();
                    os.close();
                    
                    // 删除临时文件
                    file.delete();
                }
            } else {
                log.error("导出数据到Excel失败");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("导出数据到Excel失败: ", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"code\":500,\"message\":\"导出数据失败: " + e.getMessage() + "\"}");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
    /**
     * 获取数据同步日志
     * 
     * @return 同步日志列表
     */
    @GetMapping("/sync-log")
    public Response<List<Map<String, Object>>> getSyncLog(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        try {
            log.info("获取数据同步日志");
            
            // 模拟返回同步日志列表
            List<Map<String, Object>> logs = new ArrayList<>();
            
            // 模拟一些日志数据
            for (int i = 0; i < 5; i++) {
                Map<String, Object> logEntry = new HashMap<>();
                logEntry.put("id", i + 1);
                logEntry.put("startTime", new Date(System.currentTimeMillis() - i * 3600000));
                logEntry.put("endTime", new Date(System.currentTimeMillis() - i * 3600000 + 300000)); // 5分钟
                logEntry.put("status", i % 2 == 0 ? "SUCCESS" : "FAILED");
                logEntry.put("recordsProcessed", 1000 + i * 100);
                logEntry.put("details", "同步了" + (1000 + i * 100) + "条记录");
                logs.add(logEntry);
            }
            
            // 模拟分页逻辑
            int startIndex = Math.toIntExact((pageNum - 1) * pageSize);
            int endIndex = Math.min(startIndex + Math.toIntExact(pageSize), logs.size());
            
            if (startIndex >= logs.size()) {
                logs = new ArrayList<>();
            } else {
                logs = logs.subList(startIndex, endIndex);
            }
            
            return success(logs);
        } catch (Exception e) {
            log.error("获取数据同步日志失败: ", e);
            return error(500, "获取日志失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除数据同步日志
     * 
     * @param ids 要删除的日志ID列表
     * @return 删除结果
     */
    @DeleteMapping("/sync-log")
    public Response<Void> deleteSyncLogs(@RequestBody List<Long> ids) {
        try {
            log.info("删除数据同步日志: {}", ids);
            // TODO: 实现删除同步日志的逻辑
            return success();
        } catch (Exception e) {
            log.error("删除数据同步日志失败: ", e);
            return error(500, "删除日志失败: " + e.getMessage());
        }
    }
}