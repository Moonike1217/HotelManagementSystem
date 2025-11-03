package org.example.hotelmanagementsystem.service;

import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.dto.OrderQueryDto;
import org.example.hotelmanagementsystem.dto.OrderUpdateDto;
import java.util.List;

public interface OrderService {
    
    /**
     * 查询订单列表
     * @param query 查询条件
     * @return 订单列表
     */
    List<OrderDto> findOrders(OrderQueryDto query);
    
    /**
     * 根据ID获取订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    OrderDto getOrderById(Integer id);
    
    /**
     * 更新订单信息
     * @param orderUpdateDto 订单更新信息
     * @return 是否更新成功
     */
    boolean updateOrder(OrderUpdateDto orderUpdateDto);
    
    /**
     * 确认订单
     * @param id 订单ID
     * @return 是否确认成功
     */
    boolean confirmOrder(Integer id);
    
    /**
     * 取消订单
     * @param id 订单ID
     * @return 是否取消成功
     */
    boolean cancelOrder(Integer id);
    
    /**
     * 办理入住
     * @param id 订单ID
     * @return 是否办理成功
     */
    boolean checkInOrder(Integer id);
    
    /**
     * 办理退房
     * @param id 订单ID
     * @return 是否办理成功
     */
    boolean checkOutOrder(Integer id);
}