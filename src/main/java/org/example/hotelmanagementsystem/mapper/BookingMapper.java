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
    @Select({
        "<script>",
        "SELECT r.id, r.hotel_id, h.name AS hotelName, h.address AS hotelAddress, ",
        "r.room_type, r.room_number, r.price",
        "FROM rooms r",
        "JOIN hotels h ON r.hotel_id = h.id",
        "WHERE r.status = 'available'",
        "AND r.id NOT IN (",
        "  SELECT room_id FROM orders ",
        "  WHERE status IN ('pending', 'confirmed', 'checked_in')",
        "  AND ((check_in_date &lt; #{checkOutDate} AND check_out_date &gt; #{checkInDate}))",
        ")",
        "<if test='location != null and location != \"\"'>",
        "AND h.address LIKE CONCAT('%', #{location}, '%')",
        "</if>",
        "<if test='roomType != null and roomType != \"\"'>",
        "AND r.room_type = #{roomType}",
        "</if>",
        "<if test='hotelName != null and hotelName != \"\"'>",
        "AND h.name LIKE CONCAT('%', #{hotelName}, '%')",
        "</if>",
        "ORDER BY h.name, r.room_type, r.room_number",
        "</script>"
    })
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "hotelId", column = "hotel_id"),
        @Result(property = "hotelName", column = "hotelName"),
        @Result(property = "hotelAddress", column = "hotelAddress"),
        @Result(property = "roomType", column = "room_type"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "price", column = "price")
    })
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
    @Select("SELECT * FROM rooms WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "hotelId", column = "hotel_id"),
        @Result(property = "roomType", column = "room_type"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "price", column = "price"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    Room findRoomById(Integer id);
    
    /**
     * 根据身份证号查找客户
     * @param idCard 身份证号
     * @return 客户信息
     */
    @Select("SELECT * FROM customers WHERE id_card = #{idCard}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "email", column = "email"),
        @Result(property = "idCard", column = "id_card"),
        @Result(property = "createdAt", column = "created_at")
    })
    Customer findCustomerByIdCard(String idCard);
    
    /**
     * 插入新客户
     * @param customer 客户信息
     * @return 影响行数
     */
    @Insert("INSERT INTO customers(name, phone, email, id_card, created_at) " +
            "VALUES(#{name}, #{phone}, #{email}, #{idCard}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCustomer(Customer customer);
    
    /**
     * 插入订单
     * @param order 订单信息
     * @return 影响行数
     */
    @Insert("INSERT INTO orders(order_number, customer_id, room_id, check_in_date, check_out_date, total_amount, status, created_at) " +
            "VALUES(#{orderNumber}, #{customerId}, #{roomId}, #{checkInDate}, #{checkOutDate}, #{totalAmount}, #{status}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertOrder(Orders order);
    
    /**
     * 更新房间状态
     * @param roomId 房间ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE rooms SET status = #{status} WHERE id = #{roomId}")
    int updateRoomStatus(@Param("roomId") Integer roomId, @Param("status") String status);
    
    /**
     * 插入新用户
     * @param user 用户信息
     * @return 影响行数
     */
    @Insert("INSERT INTO users(username, password, role, created_at) " +
            "VALUES(#{username}, #{password}, #{role}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "username", column = "username"),
        @Result(property = "password", column = "password"),
        @Result(property = "role", column = "role"),
        @Result(property = "createdAt", column = "created_at")
    })
    User findUserByUsername(String username);
}