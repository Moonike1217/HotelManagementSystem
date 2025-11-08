package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.dto.OrderQueryDto;
import org.example.hotelmanagementsystem.dto.OrderUpdateDto;
import org.example.hotelmanagementsystem.entity.Orders;
import org.example.hotelmanagementsystem.mapper.OrderMapper;
import org.example.hotelmanagementsystem.service.OrderService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Override
    public List<OrderDto> findOrders(OrderQueryDto query) {
        logger.debug("查询订单列表，查询条件: {}", query);
        List<OrderDto> orders = orderMapper.findOrders(query);
        logger.debug("查询到 {} 个订单", orders.size());
        return orders;
    }
    
    @Override
    public OrderDto getOrderById(Integer id) {
        logger.debug("根据ID查询订单详情，订单ID: {}", id);
        OrderDto order = orderMapper.getOrderById(id);
        if (order != null) {
            logger.debug("成功获取订单详情，订单ID: {}", id);
        } else {
            logger.warn("未找到ID为 {} 的订单", id);
        }
        return order;
    }
    
    @Override
    @Transactional
    public boolean updateOrder(OrderUpdateDto orderUpdateDto) {
        logger.info("更新订单信息，订单ID: {}", orderUpdateDto.getId());
        Orders order = new Orders();
        BeanUtils.copyProperties(orderUpdateDto, order);
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        boolean success = result > 0;
        if (success) {
            logger.info("订单信息更新成功，订单ID: {}", orderUpdateDto.getId());
        } else {
            logger.error("订单信息更新失败，订单ID: {}", orderUpdateDto.getId());
        }
        return success;
    }
    
    @Override
    @Transactional
    public boolean confirmOrder(Integer id) {
        logger.info("确认订单，订单ID: {}", id);
        // 获取订单信息
        Orders order = orderMapper.getOrderEntityById(id);
        if (order == null) {
            logger.warn("订单不存在，订单ID: {}", id);
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态是否为待确认
        if (!"pending".equals(order.getStatus())) {
            logger.warn("订单状态不正确，无法确认，订单ID: {}，当前状态: {}", id, order.getStatus());
            throw new RuntimeException("订单状态不正确，无法确认");
        }
        
        // 更新订单状态为已确认
        order.setStatus("confirmed");
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        
        // 更新房间状态为已占用
        orderMapper.updateRoomStatus(order.getRoomId(), "occupied");
        
        boolean success = result > 0;
        if (success) {
            logger.info("订单确认成功，订单ID: {}，房间ID: {} 状态已更新为已占用", id, order.getRoomId());
        } else {
            logger.error("订单确认失败，订单ID: {}", id);
        }
        return success;
    }
    
    @Override
    @Transactional
    public boolean cancelOrder(Integer id) {
        logger.info("取消订单，订单ID: {}", id);
        // 获取订单信息
        Orders order = orderMapper.getOrderEntityById(id);
        if (order == null) {
            logger.warn("订单不存在，订单ID: {}", id);
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态是否为待确认或已确认
        if (!"pending".equals(order.getStatus()) && !"confirmed".equals(order.getStatus())) {
            logger.warn("订单状态不正确，无法取消，订单ID: {}，当前状态: {}", id, order.getStatus());
            throw new RuntimeException("订单状态不正确，无法取消");
        }
        
        // 更新订单状态为已取消
        order.setStatus("cancelled");
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        
        // 更新房间状态为空闲
        orderMapper.updateRoomStatus(order.getRoomId(), "available");
        
        boolean success = result > 0;
        if (success) {
            logger.info("订单取消成功，订单ID: {}，房间ID: {} 状态已更新为空闲", id, order.getRoomId());
        } else {
            logger.error("订单取消失败，订单ID: {}", id);
        }
        return success;
    }
    
    @Override
    @Transactional
    public boolean checkInOrder(Integer id) {
        logger.info("办理入住，订单ID: {}", id);
        // 获取订单信息
        Orders order = orderMapper.getOrderEntityById(id);
        if (order == null) {
            logger.warn("订单不存在，订单ID: {}", id);
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态是否为已确认
        if (!"confirmed".equals(order.getStatus())) {
            logger.warn("订单状态不正确，无法办理入住，订单ID: {}，当前状态: {}", id, order.getStatus());
            throw new RuntimeException("订单状态不正确，无法办理入住");
        }
        
        // 更新订单状态为已入住
        order.setStatus("checked_in");
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        
        boolean success = result > 0;
        if (success) {
            logger.info("办理入住成功，订单ID: {}", id);
        } else {
            logger.error("办理入住失败，订单ID: {}", id);
        }
        return success;
    }
    
    @Override
    @Transactional
    public boolean checkOutOrder(Integer id) {
        logger.info("办理退房，订单ID: {}", id);
        // 获取订单信息
        Orders order = orderMapper.getOrderEntityById(id);
        if (order == null) {
            logger.warn("订单不存在，订单ID: {}", id);
            throw new RuntimeException("订单不存在");
        }
        
        // 检查订单状态是否为已入住
        if (!"checked_in".equals(order.getStatus())) {
            logger.warn("订单状态不正确，无法办理退房，订单ID: {}，当前状态: {}", id, order.getStatus());
            throw new RuntimeException("订单状态不正确，无法办理退房");
        }
        
        // 更新订单状态为已退房
        order.setStatus("checked_out");
        // 设置更新时间
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = orderMapper.updateOrder(order);
        
        // 更新房间状态为空闲
        orderMapper.updateRoomStatus(order.getRoomId(), "available");
        
        boolean success = result > 0;
        if (success) {
            logger.info("办理退房成功，订单ID: {}，房间ID: {} 状态已更新为空闲", id, order.getRoomId());
        } else {
            logger.error("办理退房失败，订单ID: {}", id);
        }
        return success;
    }
}