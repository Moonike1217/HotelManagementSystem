package org.example.hotelmanagementsystem.entity;

import lombok.Data;

@Data
public class Review {
    // 评价ID
    private Integer id;
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
    // 创建时间 (秒级时间戳)
    private Long createdAt;
}