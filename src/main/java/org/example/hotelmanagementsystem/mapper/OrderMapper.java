package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.entity.Orders;
import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.dto.OrderQueryDto;
import java.util.List;

@Mapper
public interface OrderMapper {
    
    /**
     * 根据条件查询订单列表
     * @param query 查询条件
     * @return 订单列表
     */
    List<OrderDto> findOrders(OrderQueryDto query);
    
    /**
     * 根据ID查询订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    OrderDto getOrderById(Integer id);
    
    /**
     * 根据ID查询订单实体
     * @param id 订单ID
     * @return 订单实体
     */
    Orders getOrderEntityById(Integer id);
    
    /**
     * 更新订单信息
     * @param order 订单信息
     * @return 影响行数
     */
    int updateOrder(Orders order);
    
    /**
     * 更新房间状态
     * @param roomId 房间ID
     * @param status 状态
     * @return 影响行数
     */
    int updateRoomStatus(@Param("roomId") Integer roomId, @Param("status") String status);
}