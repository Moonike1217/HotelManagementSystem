package org.example.hotelmanagementsystem.entity;

import lombok.Data;

@Data
public class Customer {
    // 客户ID
    private Integer id;
    // 客户姓名
    private String name;
    // 联系电话
    private String phone;
    // 邮箱地址
    private String email;
    // 身份证号
    private String idCard;
    // 创建时间
    private String createdAt;
}