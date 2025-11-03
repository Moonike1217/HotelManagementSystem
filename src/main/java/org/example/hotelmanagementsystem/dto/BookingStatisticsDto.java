package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class BookingStatisticsDto {
    // 酒店ID
    private Integer hotelId;
    // 酒店名称
    private String hotelName;
    // 总预订数
    private Integer totalBookings;
    // 确认预订数
    private Integer confirmedBookings;
    // 入住数
    private Integer checkInCount;
    // 取消数
    private Integer cancelledBookings;
    // 预订率 (%)
    private Double bookingRate;
    // 入住率 (%)
    private Double checkInRate;
}