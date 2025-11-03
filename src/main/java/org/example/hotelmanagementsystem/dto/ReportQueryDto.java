package org.example.hotelmanagementsystem.dto;

import lombok.Data;

@Data
public class ReportQueryDto {
    // 酒店ID
    private Integer hotelId;
    // 开始日期
    private String startDate;
    // 结束日期
    private String endDate;
    // 报表类型 (booking:预订统计, revenue:收入统计, occupancy:入住率统计)
    private String reportType;
}