package org.example.hotelmanagementsystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class HotelDto {
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
    // 房型列表
    private List<RoomTypeDto> roomTypes;
    // 图片列表
    private List<String> images;
    
    @Data
    public static class RoomTypeDto {
        // 房型名称
        private String roomType;
        // 房间数量
        private Integer roomCount;
        // 价格
        private java.math.BigDecimal price;
    }
}