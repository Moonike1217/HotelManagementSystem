package org.example.hotelmanagementsystem.service;

import org.example.hotelmanagementsystem.dto.BookingQueryDto;
import org.example.hotelmanagementsystem.dto.AvailableRoomDto;
import org.example.hotelmanagementsystem.dto.BookingRequestDto;
import org.example.hotelmanagementsystem.dto.BookingResultDto;
import java.util.List;

public interface BookingService {
    
    /**
     * 查询可用房间
     * @param query 查询条件
     * @return 可用房间列表
     */
    List<AvailableRoomDto> findAvailableRooms(BookingQueryDto query);
    
    /**
     * 预订房间
     * @param bookingRequest 预订请求
     * @return 预订结果
     */
    BookingResultDto bookRoom(BookingRequestDto bookingRequest);
}