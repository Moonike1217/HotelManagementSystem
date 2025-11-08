package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.entity.Hotel;
import org.example.hotelmanagementsystem.dto.HotelDto;
import org.example.hotelmanagementsystem.entity.Room;
import org.example.hotelmanagementsystem.mapper.HotelMapper;
import org.example.hotelmanagementsystem.mapper.RoomMapper;
import org.example.hotelmanagementsystem.service.HotelService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    
    private static final Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);
    
    @Autowired
    private HotelMapper hotelMapper;
    @Autowired
    private RoomMapper roomMapper;
    
    @Override
    @Transactional
    public boolean addHotel(HotelDto hotelDto) {
        logger.info("添加新酒店，酒店名称: {}", hotelDto.getName());
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelDto, hotel);
        // 设置创建时间
        if (hotel.getCreatedAt() == null) {
            hotel.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        }
        // 插入酒店信息
        int result = hotelMapper.insert(hotel);
        logger.debug("酒店信息插入完成，酒店ID: {}", hotel.getId());
        // 取出酒店的房型信息
        List<HotelDto.RoomTypeDto> roomTypes = hotelDto.getRoomTypes();
        if (roomTypes != null && !roomTypes.isEmpty()) {
            logger.debug("开始插入房间信息，房间数: {}", roomTypes.size());
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

        boolean success = result > 0;
        if (success) {
            logger.info("酒店添加成功，酒店ID: {}", hotel.getId());
        } else {
            logger.error("酒店添加失败，酒店信息: {}", hotelDto);
        }
        return success;
    }
    
    @Override
    @Transactional
    public boolean updateHotel(HotelDto hotelDto) {
        logger.info("更新酒店信息，酒店ID: {}", hotelDto.getId());
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelDto, hotel);
        // 设置更新时间
        hotel.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = hotelMapper.updateById(hotel);
        // 更新酒店房型信息
        if (hotelDto.getRoomTypes() != null) {
            List<HotelDto.RoomTypeDto> roomTypes = hotelDto.getRoomTypes();
            logger.debug("开始更新房间信息，房间数: {}", roomTypes.size());
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
        boolean success = result > 0;
        if (success) {
            logger.info("酒店信息更新成功，酒店ID: {}", hotelDto.getId());
        } else {
            logger.error("酒店信息更新失败，酒店ID: {}", hotelDto.getId());
        }
        return success;
    }
    
    @Override
    public HotelDto getHotelById(Integer id) {
        logger.debug("根据ID查询酒店详情，酒店ID: {}", id);
        Hotel hotel = hotelMapper.selectById(id);
        if (hotel == null) {
            logger.warn("未找到ID为 {} 的酒店", id);
            return null;
        }
        HotelDto hotelDto = new HotelDto();
        BeanUtils.copyProperties(hotel, hotelDto);
        
        // 查询该酒店的所有房间信息
        List<Room> rooms = roomMapper.selectByHotelId(id);
        if (rooms != null && !rooms.isEmpty()) {
            logger.debug("查询到酒店房间数: {}", rooms.size());
            List<HotelDto.RoomTypeDto> roomTypeDtos = new java.util.ArrayList<>();
            for (Room room : rooms) {
                HotelDto.RoomTypeDto roomTypeDto = new HotelDto.RoomTypeDto();
                BeanUtils.copyProperties(room, roomTypeDto);
                roomTypeDtos.add(roomTypeDto);
            }
            hotelDto.setRoomTypes(roomTypeDtos);
        }
        
        logger.debug("成功获取酒店详情，酒店ID: {}", id);
        return hotelDto;
    }
    
    @Override
    public List<Hotel> getAllHotels() {
        logger.debug("查询所有酒店信息");
        List<Hotel> hotels = hotelMapper.selectAll();
        logger.debug("查询到 {} 个酒店", hotels.size());
        return hotels;
    }
    
    @Override
    public List<Hotel> getHotelsByName(String name) {
        logger.debug("根据名称模糊查询酒店，酒店名称: {}", name);
        List<Hotel> hotels = hotelMapper.selectByName(name);
        logger.debug("查询到 {} 个匹配的酒店", hotels.size());
        return hotels;
    }
}