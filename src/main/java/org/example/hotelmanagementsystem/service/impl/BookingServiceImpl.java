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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    
    @Autowired
    private BookingMapper bookingMapper;
    
    @Override
    public List<AvailableRoomDto> findAvailableRooms(BookingQueryDto query) {
        logger.debug("查询可用房间，参数: {}", query);
        List<AvailableRoomDto> rooms = bookingMapper.findAvailableRooms(
            query.getCheckInDate(),
            query.getCheckOutDate(),
            query.getLocation(),
            query.getRoomType(),
            query.getHotelName()
        );
        logger.debug("查询到 {} 个可用房间", rooms.size());
        return rooms;
    }
    
    @Override
    @Transactional
    public BookingResultDto bookRoom(BookingRequestDto bookingRequest) {
        logger.info("开始处理房间预订请求: {}", bookingRequest);
        
        // 检查房间是否存在且可用
        Room room = bookingMapper.findRoomById(bookingRequest.getRoomId());
        if (room == null || !"available".equals(room.getStatus())) {
            logger.warn("房间不存在或不可用，房间ID: {}", bookingRequest.getRoomId());
            throw new RuntimeException("房间不存在或不可用");
        }
        
        // 检查客户是否已存在，如果不存在则创建新客户
        Customer customer = null;
        if (bookingRequest.getCustomerIdCard() != null && !bookingRequest.getCustomerIdCard().isEmpty()) {
            customer = bookingMapper.findCustomerByIdCard(bookingRequest.getCustomerIdCard());
        }
        
        // 如果客户不存在，则创建新客户
        if (customer == null) {
            logger.debug("创建新客户: {}", bookingRequest.getCustomerName());
            customer = new Customer();
            customer.setName(bookingRequest.getCustomerName());
            customer.setPhone(bookingRequest.getCustomerPhone());
            customer.setEmail(bookingRequest.getCustomerEmail());
            customer.setIdCard(bookingRequest.getCustomerIdCard());
            customer.setCreatedAt(TimestampUtil.getCurrentTimestamp());
            bookingMapper.insertCustomer(customer);
        } else {
            logger.debug("使用现有客户信息，客户ID: {}", customer.getId());
        }

        
        // 计算天数
        int days = calculateDays(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
        if (days <= 0) {
            logger.warn("入住日期必须早于退房日期，入住日期: {}，退房日期: {}", bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
            throw new RuntimeException("入住日期必须早于退房日期");
        }
        
        // 计算总金额
        BigDecimal totalAmount = room.getPrice().multiply(new BigDecimal(days));
        logger.debug("计算订单总金额: {} = {} × {}天", totalAmount, room.getPrice(), days);
        
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
        logger.info("订单创建成功，订单号: {}，订单ID: {}", orderNumber, order.getId());
        
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
        
        logger.info("房间预订完成，订单号: {}", orderNumber);
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