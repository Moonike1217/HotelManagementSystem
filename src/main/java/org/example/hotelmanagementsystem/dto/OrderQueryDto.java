package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class OrderQueryDto {
    // 订单ID
    private Integer id;
    // 订单编号
    private String orderNumber;
    // 客户ID
    private Integer customerId;
    // 客户姓名
    private String customerName;
    // 房间ID
    private Integer roomId;
    // 酒店ID
    private Integer hotelId;
    // 酒店名称
    private String hotelName;
    // 入住日期起始
    private String checkInDateStart;
    // 入住日期结束
    private String checkInDateEnd;
    // 订单状态
    private String status;
}