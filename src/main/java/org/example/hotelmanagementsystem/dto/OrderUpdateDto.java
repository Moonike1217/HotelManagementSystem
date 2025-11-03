package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class OrderUpdateDto {
    // 订单ID
    private Integer id;
    // 入住日期
    private String checkInDate;
    // 退房日期
    private String checkOutDate;
    // 订单状态
    private String status;
}