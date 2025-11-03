package org.example.hotelmanagementsystem.controller;

import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.dto.OrderQueryDto;
import org.example.hotelmanagementsystem.dto.OrderUpdateDto;
import org.example.hotelmanagementsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 查询订单列表
     * @param query 查询条件
     * @return 订单列表
     */
    @PostMapping("/search")
    public List<OrderDto> findOrders(@RequestBody OrderQueryDto query) {
        return orderService.findOrders(query);
    }
    
    /**
     * 根据ID获取订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }
    
    /**
     * 更新订单信息
     * @param orderUpdateDto 订单更新信息
     * @return 是否更新成功
     */
    @PutMapping
    public boolean updateOrder(@RequestBody OrderUpdateDto orderUpdateDto) {
        try {
            return orderService.updateOrder(orderUpdateDto);
        } catch (Exception e) {
            throw new RuntimeException("更新订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 确认订单
     * @param id 订单ID
     * @return 是否确认成功
     */
    @PutMapping("/{id}/confirm")
    public boolean confirmOrder(@PathVariable Integer id) {
        try {
            return orderService.confirmOrder(id);
        } catch (Exception e) {
            throw new RuntimeException("确认订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 取消订单
     * @param id 订单ID
     * @return 是否取消成功
     */
    @PutMapping("/{id}/cancel")
    public boolean cancelOrder(@PathVariable Integer id) {
        try {
            return orderService.cancelOrder(id);
        } catch (Exception e) {
            throw new RuntimeException("取消订单失败: " + e.getMessage());
        }
    }
    
    /**
     * 办理入住
     * @param id 订单ID
     * @return 是否办理成功
     */
    @PutMapping("/{id}/check-in")
    public boolean checkInOrder(@PathVariable Integer id) {
        try {
            return orderService.checkInOrder(id);
        } catch (Exception e) {
            throw new RuntimeException("办理入住失败: " + e.getMessage());
        }
    }
    
    /**
     * 办理退房
     * @param id 订单ID
     * @return 是否办理成功
     */
    @PutMapping("/{id}/check-out")
    public boolean checkOutOrder(@PathVariable Integer id) {
        try {
            return orderService.checkOutOrder(id);
        } catch (Exception e) {
            throw new RuntimeException("办理退房失败: " + e.getMessage());
        }
    }
}