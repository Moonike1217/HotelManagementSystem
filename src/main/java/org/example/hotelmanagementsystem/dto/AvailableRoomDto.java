package org.example.hotelmanagementsystem.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AvailableRoomDto {
    // 房间ID
    private Integer id;
    // 酒店ID
    private Integer hotelId;
    // 酒店名称
    private String hotelName;
    // 酒店地址
    private String hotelAddress;
    // 房间类型
    private String roomType;
    // 房间号
    private String roomNumber;
    // 价格
    private BigDecimal price;
    // 入住日期
    private String checkInDate;
    // 退房日期
    private String checkOutDate;
}