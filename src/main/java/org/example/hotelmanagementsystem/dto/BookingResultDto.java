package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class BookingResultDto {
    // 订单ID
    private Integer orderId;
    // 订单编号
    private String orderNumber;
    // 客户ID
    private Integer customerId;
    // 房间ID
    private Integer roomId;
    // 入住日期
    private String checkInDate;
    // 退房日期
    private String checkOutDate;
    // 总金额
    private java.math.BigDecimal totalAmount;
    // 订单状态
    private String status;
}