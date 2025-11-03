package org.example.hotelmanagementsystem.service;

import org.example.hotelmanagementsystem.dto.OrderDto;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    /**
     * 发送订单确认信息
     * @param order 订单信息
     */
    public void sendOrderConfirmation(OrderDto order) {
        // 这里应该是实际的通知逻辑，比如发送邮件或短信
        // 为了简化，我们只打印日志
        System.out.println("=== 订单确认通知 ===");
        System.out.println("尊敬的 " + order.getCustomerName() + "，您的订单已确认！");
        System.out.println("订单编号: " + order.getOrderNumber());
        System.out.println("酒店名称: " + order.getHotelName());
        System.out.println("房型: " + order.getRoomType());
        System.out.println("房间号: " + order.getRoomNumber());
        System.out.println("入住日期: " + order.getCheckInDate());
        System.out.println("退房日期: " + order.getCheckOutDate());
        System.out.println("总金额: " + order.getTotalAmount());
        System.out.println("===============");
    }
    
    /**
     * 发送入住提醒
     * @param order 订单信息
     */
    public void sendCheckInReminder(OrderDto order) {
        // 这里应该是实际的通知逻辑，比如发送邮件或短信
        // 为了简化，我们只打印日志
        System.out.println("=== 入住提醒 ===");
        System.out.println("尊敬的 " + order.getCustomerName() + "，您预订的房间即将到期，请准备入住！");
        System.out.println("订单编号: " + order.getOrderNumber());
        System.out.println("酒店名称: " + order.getHotelName());
        System.out.println("房型: " + order.getRoomType());
        System.out.println("入住日期: " + order.getCheckInDate());
        System.out.println("===============");
    }
}