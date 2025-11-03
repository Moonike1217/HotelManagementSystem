package org.example.hotelmanagementsystem.entity;

import lombok.Data;

@Data
public class Orders {
    // 订单ID
    private Integer id;
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
    // 订单状态（pending:待确认, confirmed:已确认, checked_in:已入住, checked_out:已退房, cancelled:已取消）
    private String status = "pending";
    // 创建时间 (秒级时间戳)
    private Long createdAt;
}