package org.example.hotelmanagementsystem.entity;

import lombok.Data;

@Data
public class Hotel {
    // 酒店ID
    private Integer id;
    // 酒店名称
    private String name;
    // 酒店地址
    private String address;
    // 联系电话
    private String phone;
    // 星级
    private Integer starLevel;
    // 描述信息
    private String description;
    // 状态（active:启用, inactive:禁用）
    private String status = "active";
    // 创建时间 (秒级时间戳)
    private Long createdAt;
}