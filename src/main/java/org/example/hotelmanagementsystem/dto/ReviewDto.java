package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class ReviewDto {
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
    // 评分（1-5分）
    private Integer rating;
    // 评价内容
    private String comment;
    // 管理员回复
    private String reply;
    // 创建时间
    private String createdAt;
}