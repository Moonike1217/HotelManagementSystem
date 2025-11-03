package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.entity.Hotel;
import org.example.hotelmanagementsystem.dto.HotelDto;
import org.example.hotelmanagementsystem.mapper.HotelMapper;
import org.example.hotelmanagementsystem.service.HotelService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    
    @Autowired
    private HotelMapper hotelMapper;
    
    @Override
    public boolean addHotel(HotelDto hotelDto) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelDto, hotel);
        // 设置创建时间
        if (hotel.getCreatedAt() == null) {
            hotel.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        }
        int result = hotelMapper.insert(hotel);
        return result > 0;
    }
    
    @Override
    public boolean updateHotel(HotelDto hotelDto) {
        Hotel hotel = new Hotel();
        BeanUtils.copyProperties(hotelDto, hotel);
        // 设置更新时间
        hotel.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = hotelMapper.updateById(hotel);
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