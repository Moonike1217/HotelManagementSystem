package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class BookingQueryDto {
    // 入住日期
    private String checkInDate;
    // 退房日期
    private String checkOutDate;
    // 地点（酒店地址）
    private String location;
    // 房间类型
    private String roomType;
    // 酒店名称
    private String hotelName;
}