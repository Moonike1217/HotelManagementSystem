package org.example.hotelmanagementsystem.controller;

import org.example.hotelmanagementsystem.entity.Hotel;
import org.example.hotelmanagementsystem.dto.HotelDto;
import org.example.hotelmanagementsystem.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {
    
    @Autowired
    private HotelService hotelService;
    
    /**
     * 添加酒店信息
     * @param hotelDto 酒店信息
     * @return 是否添加成功
     */
    @PostMapping
    public boolean addHotel(@RequestBody HotelDto hotelDto) {
        return hotelService.addHotel(hotelDto);
    }
    
    /**
     * 更新酒店信息
     * @param hotelDto 酒店信息
     * @return 是否更新成功
     */
    @PutMapping
    public boolean updateHotel(@RequestBody HotelDto hotelDto) {
        return hotelService.updateHotel(hotelDto);
    }
    
    /**
     * 根据ID查询酒店信息
     * @param id 酒店ID
     * @return 酒店信息
     */
    @GetMapping("/{id}")
    public HotelDto getHotelById(@PathVariable Integer id) {
        return hotelService.getHotelById(id);
    }
    
    /**
     * 查询所有酒店信息
     * @return 酒店列表
     */
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }
    
    /**
     * 根据名称模糊查询酒店
     * @param name 酒店名称
     * @return 酒店列表
     */
    @GetMapping("/search")
    public List<Hotel> searchHotelsByName(@RequestParam String name) {
        return hotelService.getHotelsByName(name);
    }
}