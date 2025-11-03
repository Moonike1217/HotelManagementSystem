package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class OccupancyRateDto {
    // 酒店ID
    private Integer hotelId;
    // 酒店名称
    private String hotelName;
    // 日期
    private String date;
    // 总房间数
    private Integer totalRooms;
    // 已占用房间数
    private Integer occupiedRooms;
    // 入住率 (%)
    private Double occupancyRate;
}