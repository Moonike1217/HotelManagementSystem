package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.dto.BookingQueryDto;
import org.example.hotelmanagementsystem.dto.AvailableRoomDto;
import org.example.hotelmanagementsystem.dto.BookingRequestDto;
import org.example.hotelmanagementsystem.dto.BookingResultDto;
import org.example.hotelmanagementsystem.entity.Customer;
import org.example.hotelmanagementsystem.entity.Orders;
import org.example.hotelmanagementsystem.entity.Room;
import org.example.hotelmanagementsystem.mapper.BookingMapper;
import org.example.hotelmanagementsystem.service.BookingService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {
    
    @Autowired
    private BookingMapper bookingMapper;
    
    @Override
    public List<AvailableRoomDto> findAvailableRooms(BookingQueryDto query) {
        return bookingMapper.findAvailableRooms(
            query.getCheckInDate(),
            query.getCheckOutDate(),
            query.getLocation(),
            query.getRoomType(),
            query.getHotelName()
        );
    }
    
    @Override
    @Transactional
    public BookingResultDto bookRoom(BookingRequestDto bookingRequest) {
        // 检查房间是否存在且可用
        Room room = bookingMapper.findRoomById(bookingRequest.getRoomId());
        if (room == null || !"available".equals(room.getStatus())) {
            throw new RuntimeException("房间不存在或不可用");
        }
        
        // 查找或创建客户
        Customer customer = null;
        if (bookingRequest.getCustomerId() != null) {
            // 如果提供了客户ID，则使用该客户
            // 这里简化处理，实际应该通过ID查找客户
        } else if (bookingRequest.getCustomerIdCard() != null && !bookingRequest.getCustomerIdCard().isEmpty()) {
            // 通过身份证号查找客户
            customer = bookingMapper.findCustomerByIdCard(bookingRequest.getCustomerIdCard());
        }
        
        // 如果客户不存在，则创建新客户
        if (customer == null) {
            customer = new Customer();
            customer.setName(bookingRequest.getCustomerName());
            customer.setPhone(bookingRequest.getCustomerPhone());
            customer.setEmail(bookingRequest.getCustomerEmail());
            customer.setIdCard(bookingRequest.getCustomerIdCard());
            customer.setCreatedAt(TimestampUtil.getCurrentTimestamp());
            bookingMapper.insertCustomer(customer);
        }
        
        // 计算天数
        int days = calculateDays(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
        if (days <= 0) {
            throw new RuntimeException("入住日期必须早于退房日期");
        }
        
        // 计算总金额
        BigDecimal totalAmount = room.getPrice().multiply(new BigDecimal(days));
        
        // 创建订单
        Orders order = new Orders();
        String orderNumber = "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        order.setOrderNumber(orderNumber);
        order.setCustomerId(customer.getId());
        order.setRoomId(bookingRequest.getRoomId());
        order.setCheckInDate(bookingRequest.getCheckInDate());
        order.setCheckOutDate(bookingRequest.getCheckOutDate());
        order.setTotalAmount(totalAmount);
        order.setStatus("pending");
        order.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        bookingMapper.insertOrder(order);
        
        // 更新房间状态为已预订
        bookingMapper.updateRoomStatus(bookingRequest.getRoomId(), "occupied");
        
        // 返回预订结果
        BookingResultDto result = new BookingResultDto();
        result.setOrderId(order.getId());
        result.setOrderNumber(orderNumber);
        result.setCustomerId(customer.getId());
        result.setRoomId(bookingRequest.getRoomId());
        result.setCheckInDate(bookingRequest.getCheckInDate());
        result.setCheckOutDate(bookingRequest.getCheckOutDate());
        result.setTotalAmount(totalAmount);
        result.setStatus("pending");
        
        return result;
    }
    
    /**
     * 计算两个日期之间的天数
     * @param checkInDate 入住日期
     * @param checkOutDate 退房日期
     * @return 天数
     */
    private int calculateDays(String checkInDate, String checkOutDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(checkInDate);
            Date endDate = sdf.parse(checkOutDate);
            long diff = endDate.getTime() - startDate.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            throw new RuntimeException("日期格式错误", e);
        }
    }
}