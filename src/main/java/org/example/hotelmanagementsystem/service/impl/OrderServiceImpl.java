package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.dto.OrderQueryDto;
import org.example.hotelmanagementsystem.dto.OrderUpdateDto;
import org.example.hotelmanagementsystem.entity.Orders;
import org.example.hotelmanagementsystem.mapper.OrderMapper;
import org.example.hotelmanagementsystem.service.OrderService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Override
    public List<OrderDto> findOrders(OrderQueryDto query) {
        return orderMapper.findOrders(query);
    }
    
    @Override
    public OrderDto getOrderById(Integer id) {
        return orderMapper.getOrderById(id);
    }
    
    @Override
    @Transactional
    public boolean updateOrder(OrderUpdateDto orderUpdateDto) {
        Orders order = new Orders();
        BeanUtils.copyProperties(orderUpdateDto, order);
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean confirmOrder(Integer id) {
        // 获取订单信息
        Orders order = orderMapper.getOrderEntityById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态是否为待确认
        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法确认");
        }
        
        // 更新订单状态为已确认
        order.setStatus("confirmed");
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        
        // 更新房间状态为已占用
        orderMapper.updateRoomStatus(order.getRoomId(), "occupied");
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean cancelOrder(Integer id) {
        // 获取订单信息
        Orders order = orderMapper.getOrderEntityById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态是否为待确认或已确认
        if (!"pending".equals(order.getStatus()) && !"confirmed".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法取消");
        }
        
        // 更新订单状态为已取消
        order.setStatus("cancelled");
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        
        // 更新房间状态为空闲
        orderMapper.updateRoomStatus(order.getRoomId(), "available");
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean checkInOrder(Integer id) {
        // 获取订单信息
        Orders order = orderMapper.getOrderEntityById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态是否为已确认
        if (!"confirmed".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法办理入住");
        }
        
        // 更新订单状态为已入住
        order.setStatus("checked_in");
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean checkOutOrder(Integer id) {
        // 获取订单信息
        Orders order = orderMapper.getOrderEntityById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态是否为已入住
        if (!"checked_in".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确，无法办理退房");
        }
        
        // 更新订单状态为已退房
        order.setStatus("checked_out");
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        
        // 更新房间状态为空闲
        orderMapper.updateRoomStatus(order.getRoomId(), "available");
        
        return result > 0;
    }
}