package org.example.hotelmanagementsystem.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RevenueStatisticsDto {
    // 酒店ID
    private Integer hotelId;
    // 酒店名称
    private String hotelName;
    // 月份
    private String month;
    // 总收入
    private BigDecimal totalRevenue;
    // 平均房价
    private BigDecimal averageRoomPrice;
    // 订单数
    private Integer orderCount;
}