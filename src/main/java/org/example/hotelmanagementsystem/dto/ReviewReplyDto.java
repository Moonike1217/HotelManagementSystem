package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class ReviewReplyDto {
    // 评价ID
    private Integer id;
    // 管理员回复
    private String adminReply;
}