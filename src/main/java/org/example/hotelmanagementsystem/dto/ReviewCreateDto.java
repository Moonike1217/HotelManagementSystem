package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class ReviewCreateDto {
    // 订单ID
    private Integer orderId;
    // 客户ID
    private Integer customerId;
    // 酒店ID
    private Integer hotelId;
    // 评分（1-5分）
    private Integer rating;
    // 评价内容
    private String comment;
}