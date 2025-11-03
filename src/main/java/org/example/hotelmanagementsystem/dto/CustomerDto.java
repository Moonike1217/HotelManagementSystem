package org.example.hotelmanagementsystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class CustomerDto {
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
    // 创建时间 (秒级时间戳)
    private Long createdAt;
    // 历史订单列表
    private List<OrderDto> orderHistory;
}