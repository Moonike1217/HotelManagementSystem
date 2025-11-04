package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.entity.Hotel;
import org.example.hotelmanagementsystem.dto.HotelDto;
import org.example.hotelmanagementsystem.entity.Room;
import org.example.hotelmanagementsystem.mapper.HotelMapper;
import org.example.hotelmanagementsystem.mapper.RoomMapper;
import org.example.hotelmanagementsystem.service.HotelService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    
    @Autowired
    private HotelMapper hotelMapper;
    @Autowired
    private RoomMapper roomMapper;
    
    @Override
    @Transactional
    public boolean addHotel(HotelDto hotelDto) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelDto, hotel);
        // 设置创建时间
        if (hotel.getCreatedAt() == null) {
            hotel.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        }
        // 插入酒店信息
        int result = hotelMapper.insert(hotel);
        // 取出酒店的房型信息
        List<HotelDto.RoomTypeDto> roomTypes = hotelDto.getRoomTypes();
        if (roomTypes != null && !roomTypes.isEmpty()) {
            for (HotelDto.RoomTypeDto roomType : roomTypes) {
                // 创建房间对象
                Room room = new Room();
                BeanUtils.copyProperties(roomType, room);
                room.setHotelId(hotel.getId());
                room.setStatus("available");
                room.setCreatedAt(TimestampUtil.getCurrentTimestamp());
                // 插入房间信息
                result += roomMapper.insert(room);
            }
        }

        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean updateHotel(HotelDto hotelDto) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelDto, hotel);
        // 设置更新时间
        hotel.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = hotelMapper.updateById(hotel);
        // 更新酒店房型信息
        if (hotelDto.getRoomTypes() != null) {
            List<HotelDto.RoomTypeDto> roomTypes = hotelDto.getRoomTypes();
            for (HotelDto.RoomTypeDto roomType : roomTypes) {
                // 创建房间对象
                Room room = new Room();
                BeanUtils.copyProperties(roomType, room);
                room.setHotelId(hotel.getId());
                room.setStatus("available");
                room.setCreatedAt(TimestampUtil.getCurrentTimestamp());
                // 更新房间信息
                result += roomMapper.updateById(room);
            }
        }
        return result > 0;
    }
    
    @Override
    public HotelDto getHotelById(Integer id) {
        Hotel hotel = hotelMapper.selectById(id);
        if (hotel == null) {
            return null;
        }
        HotelDto hotelDto = new HotelDto();
        BeanUtils.copyProperties(hotel, hotelDto);
        
        // 查询该酒店的所有房间信息
        List<Room> rooms = roomMapper.selectByHotelId(id);
        if (rooms != null && !rooms.isEmpty()) {
            List<HotelDto.RoomTypeDto> roomTypeDtos = new java.util.ArrayList<>();
            for (Room room : rooms) {
                HotelDto.RoomTypeDto roomTypeDto = new HotelDto.RoomTypeDto();
                BeanUtils.copyProperties(room, roomTypeDto);
                roomTypeDtos.add(roomTypeDto);
            }
            hotelDto.setRoomTypes(roomTypeDtos);
        }
        
        return hotelDto;
    }
    
    @Override
    public List<Hotel> getAllHotels() {
        return hotelMapper.selectAll();
    }
    
    @Override
    public List<Hotel> getHotelsByName(String name) {
        return hotelMapper.selectByName(name);
    }
}