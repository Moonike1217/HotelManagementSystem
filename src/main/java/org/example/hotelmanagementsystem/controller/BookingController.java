package org.example.hotelmanagementsystem.controller;

import org.example.hotelmanagementsystem.dto.BookingQueryDto;
import org.example.hotelmanagementsystem.dto.AvailableRoomDto;
import org.example.hotelmanagementsystem.dto.BookingRequestDto;
import org.example.hotelmanagementsystem.dto.BookingResultDto;
import org.example.hotelmanagementsystem.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    /**
     * 查询可用房间
     * @param query 查询条件
     * @return 可用房间列表
     */
    @PostMapping("/search")
    public List<AvailableRoomDto> findAvailableRooms(@RequestBody BookingQueryDto query) {
        return bookingService.findAvailableRooms(query);
    }
    
    /**
     * 预订房间
     * @param bookingRequest 预订请求
     * @return 预订结果
     */
    @PostMapping
    public BookingResultDto bookRoom(@RequestBody BookingRequestDto bookingRequest) {
        try {
            return bookingService.bookRoom(bookingRequest);
        } catch (Exception e) {
            throw new RuntimeException("预订失败: " + e.getMessage());
        }
    }
}