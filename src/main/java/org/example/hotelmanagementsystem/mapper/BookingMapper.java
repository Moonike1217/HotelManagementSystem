package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.dto.AvailableRoomDto;
import org.example.hotelmanagementsystem.entity.Orders;
import org.example.hotelmanagementsystem.entity.Customer;
import org.example.hotelmanagementsystem.entity.Room;
import org.example.hotelmanagementsystem.entity.User;
import java.util.List;

@Mapper
public interface BookingMapper {
    
    /**
     * 查询指定条件下可用的房间
     * @param checkInDate 入住日期
     * @param checkOutDate 退房日期
     * @param location 地点
     * @param roomType 房间类型
     * @param hotelName 酒店名称
     * @return 可用房间列表
     */
    List<AvailableRoomDto> findAvailableRooms(
        @Param("checkInDate") String checkInDate,
        @Param("checkOutDate") String checkOutDate,
        @Param("location") String location,
        @Param("roomType") String roomType,
        @Param("hotelName") String hotelName
    );
    
    /**
     * 根据ID查询房间信息
     * @param id 房间ID
     * @return 房间信息
     */
    Room findRoomById(Integer id);
    
    /**
     * 根据身份证号查找客户
     * @param idCard 身份证号
     * @return 客户信息
     */
    Customer findCustomerByIdCard(String idCard);
    
    /**
     * 插入新客户
     * @param customer 客户信息
     * @return 影响行数
     */
    int insertCustomer(Customer customer);
    
    /**
     * 插入订单
     * @param order 订单信息
     * @return 影响行数
     */
    int insertOrder(Orders order);
    
    /**
     * 更新房间状态
     * @param roomId 房间ID
     * @param status 状态
     * @return 影响行数
     */
    int updateRoomStatus(@Param("roomId") Integer roomId, @Param("status") String status);
    
    /**
     * 插入新用户
     * @param user 用户信息
     * @return 影响行数
     */
    int insertUser(User user);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User findUserByUsername(String username);
}