package org.example.hotelmanagementsystem.service;

import org.example.hotelmanagementsystem.dto.BookingStatisticsDto;
import org.example.hotelmanagementsystem.dto.RevenueStatisticsDto;
import org.example.hotelmanagementsystem.dto.OccupancyRateDto;
import org.example.hotelmanagementsystem.dto.ReportQueryDto;
import java.io.IOException;
import java.util.List;

public interface ReportService {
    
    /**
     * 获取预订统计信息
     * @param query 查询条件
     * @return 预订统计列表
     */
    List<BookingStatisticsDto> getBookingStatistics(ReportQueryDto query);
    
    /**
     * 获取收入统计信息
     * @param query 查询条件
     * @return 收入统计列表
     */
    List<RevenueStatisticsDto> getRevenueStatistics(ReportQueryDto query);
    
    /**
     * 获取入住率统计信息
     * @param query 查询条件
     * @return 入住率统计列表
     */
    List<OccupancyRateDto> getOccupancyRateStatistics(ReportQueryDto query);
    
    /**
     * 导出预订统计报表到Excel
     * @param query 查询条件
     * @return Excel文件路径
     * @throws IOException IO异常
     */
    String exportBookingStatisticsToExcel(ReportQueryDto query) throws IOException;
    
    /**
     * 导出收入统计报表到Excel
     * @param query 查询条件
     * @return Excel文件路径
     * @throws IOException IO异常
     */
    String exportRevenueStatisticsToExcel(ReportQueryDto query) throws IOException;
    
    /**
     * 导出入住率统计报表到Excel
     * @param query 查询条件
     * @return Excel文件路径
     * @throws IOException IO异常
     */
    String exportOccupancyRateStatisticsToExcel(ReportQueryDto query) throws IOException;
}