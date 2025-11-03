package org.example.hotelmanagementsystem.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderDto {
    // 订单ID
    private Integer id;
    // 订单编号
    private String orderNumber;
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
    // 入住日期
    private String checkInDate;
    // 退房日期
    private String checkOutDate;
    // 总金额
    private BigDecimal totalAmount;
    // 订单状态
    private String status;
    // 创建时间 (秒级时间戳)
    private Long createdAt;
}