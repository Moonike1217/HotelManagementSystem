package org.example.hotelmanagementsystem.service;

import org.example.hotelmanagementsystem.entity.Hotel;
import org.example.hotelmanagementsystem.dto.HotelDto;
import java.util.List;

public interface HotelService {
    
    /**
     * 添加酒店信息
     * @param hotelDto 酒店信息传输对象
     * @return 是否添加成功
     */
    boolean addHotel(HotelDto hotelDto);
    
    /**
     * 更新酒店信息
     * @param hotelDto 酒店信息传输对象
     * @return 是否更新成功
     */
    boolean updateHotel(HotelDto hotelDto);
    
    /**
     * 根据ID查询酒店信息
     * @param id 酒店ID
     * @return 酒店信息
     */
    HotelDto getHotelById(Integer id);
    
    /**
     * 查询所有酒店信息
     * @return 酒店列表
     */
    List<Hotel> getAllHotels();
    
    /**
     * 根据名称模糊查询酒店
     * @param name 酒店名称
     * @return 酒店列表
     */
    List<Hotel> getHotelsByName(String name);
}