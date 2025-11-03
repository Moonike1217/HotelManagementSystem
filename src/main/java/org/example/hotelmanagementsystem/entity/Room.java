package org.example.hotelmanagementsystem.entity;

import lombok.Data;

@Data
public class Room {
    // 房间ID
    private Integer id;
    // 所属酒店ID
    private Integer hotelId;
    // 房间类型
    private String roomType;
    // 房间号
    private String roomNumber;
    // 价格
    private java.math.BigDecimal price;
    // 状态（available:空闲, occupied:已入住, maintenance:维修中）
    private String status = "available";
    // 创建时间 (秒级时间戳)
    private Long createdAt;
}