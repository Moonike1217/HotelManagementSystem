package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.dto.BookingStatisticsDto;
import org.example.hotelmanagementsystem.dto.RevenueStatisticsDto;
import org.example.hotelmanagementsystem.dto.OccupancyRateDto;
import java.util.List;

@Mapper
public interface ReportMapper {
    
    /**
     * 查询预订统计信息
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param hotelId 酒店ID（可选）
     * @return 预订统计列表
     */
    List<BookingStatisticsDto> getBookingStatistics(
        @Param("startDate") Long startDate,
        @Param("endDate") Long endDate,
        @Param("hotelId") Integer hotelId
    );
    
    /**
     * 查询收入统计信息
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param hotelId 酒店ID（可选）
     * @return 收入统计列表
     */
    List<RevenueStatisticsDto> getRevenueStatistics(
        @Param("startDate") Long startDate,
        @Param("endDate") Long endDate,
        @Param("hotelId") Integer hotelId
    );
    
    /**
     * 查询入住率统计信息
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param hotelId 酒店ID（可选）
     * @return 入住率统计列表
     */
    List<OccupancyRateDto> getOccupancyRateStatistics(
        @Param("startDate") Long startDate,
        @Param("endDate") Long endDate,
        @Param("hotelId") Integer hotelId
    );
}