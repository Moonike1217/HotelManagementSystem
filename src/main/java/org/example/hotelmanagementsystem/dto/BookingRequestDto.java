package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class BookingRequestDto {
    // 客户ID
    private Integer customerId;
    // 客户姓名
    private String customerName;
    // 客户联系电话
    private String customerPhone;
    // 客户邮箱
    private String customerEmail;
    // 客户身份证号
    private String customerIdCard;
    // 房间ID
    private Integer roomId;
    // 入住日期
    private String checkInDate;
    // 退房日期
    private String checkOutDate;
    // 总金额
    private java.math.BigDecimal totalAmount;
}