package org.example.hotelmanagementsystem.controller;

import org.example.hotelmanagementsystem.dto.BookingStatisticsDto;
import org.example.hotelmanagementsystem.dto.RevenueStatisticsDto;
import org.example.hotelmanagementsystem.dto.OccupancyRateDto;
import org.example.hotelmanagementsystem.dto.ReportQueryDto;
import org.example.hotelmanagementsystem.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    /**
     * 获取预订统计信息
     * @param query 查询条件
     * @return 预订统计列表
     */
    @PostMapping("/booking-statistics")
    public List<BookingStatisticsDto> getBookingStatistics(@RequestBody ReportQueryDto query) {
        return reportService.getBookingStatistics(query);
    }
    
    /**
     * 获取收入统计信息
     * @param query 查询条件
     * @return 收入统计列表
     */
    @PostMapping("/revenue-statistics")
    public List<RevenueStatisticsDto> getRevenueStatistics(@RequestBody ReportQueryDto query) {
        return reportService.getRevenueStatistics(query);
    }
    
    /**
     * 获取入住率统计信息
     * @param query 查询条件
     * @return 入住率统计列表
     */
    @PostMapping("/occupancy-rate-statistics")
    public List<OccupancyRateDto> getOccupancyRateStatistics(@RequestBody ReportQueryDto query) {
        return reportService.getOccupancyRateStatistics(query);
    }
    
    /**
     * 导出预订统计报表到Excel
     * @param query 查询条件
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    @PostMapping("/export/booking-statistics")
    public void exportBookingStatisticsToExcel(@RequestBody ReportQueryDto query, HttpServletResponse response) throws IOException {
        try {
            String filePath = reportService.exportBookingStatisticsToExcel(query);
            downloadFile(filePath, response);
        } catch (Exception e) {
            throw new RuntimeException("导出预订统计报表失败: " + e.getMessage());
        }
    }
    
    /**
     * 导出收入统计报表到Excel
     * @param query 查询条件
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    @PostMapping("/export/revenue-statistics")
    public void exportRevenueStatisticsToExcel(@RequestBody ReportQueryDto query, HttpServletResponse response) throws IOException {
        try {
            String filePath = reportService.exportRevenueStatisticsToExcel(query);
            downloadFile(filePath, response);
        } catch (Exception e) {
            throw new RuntimeException("导出收入统计报表失败: " + e.getMessage());
        }
    }
    
    /**
     * 导出入住率统计报表到Excel
     * @param query 查询条件
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    @PostMapping("/export/occupancy-rate-statistics")
    public void exportOccupancyRateStatisticsToExcel(@RequestBody ReportQueryDto query, HttpServletResponse response) throws IOException {
        try {
            String filePath = reportService.exportOccupancyRateStatisticsToExcel(query);
            downloadFile(filePath, response);
        } catch (Exception e) {
            throw new RuntimeException("导出入住率统计报表失败: " + e.getMessage());
        }
    }
    
    /**
     * 下载文件
     * @param filePath 文件路径
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    private void downloadFile(String filePath, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        
        FileInputStream fis = new FileInputStream(file);
        OutputStream os = response.getOutputStream();
        
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        
        fis.close();
        os.close();
    }
}