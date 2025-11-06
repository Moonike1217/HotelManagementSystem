package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class ReviewQueryDto {
    // 评价ID
    private Integer id;
    // 订单ID
    private Integer orderId;
    // 客户ID
    private Integer customerId;
    // 客户姓名
    private String customerName;
    // 酒店ID
    private Integer hotelId;
    // 酒店名称
    private String hotelName;
    // 评分
    private Integer Rating;
    // 是否有管理员回复
    private Boolean hasReply;
}