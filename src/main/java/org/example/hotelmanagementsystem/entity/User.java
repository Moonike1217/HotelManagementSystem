package org.example.hotelmanagementsystem.entity;

import lombok.Data;

@Data
public class User {
    // 用户ID
    private Integer id;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 角色（admin:管理员, staff:员工）
    private String role = "staff";
    // 创建时间
    private String createdAt;
}