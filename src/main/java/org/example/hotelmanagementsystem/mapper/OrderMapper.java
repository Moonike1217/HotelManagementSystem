package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.entity.Orders;
import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.dto.OrderQueryDto;
import java.util.List;

@Mapper
public interface OrderMapper {
    
    /**
     * 根据条件查询订单列表
     * @param query 查询条件
     * @return 订单列表
     */
    @Select({
        "<script>",
        "SELECT o.*, c.name AS customerName, c.phone AS customerPhone, c.email AS customerEmail, c.id_card AS customerIdCard,",
        "r.room_type, r.room_number, h.id AS hotelId, h.name AS hotelName, h.address AS hotelAddress",
        "FROM orders o",
        "LEFT JOIN customers c ON o.customer_id = c.id",
        "LEFT JOIN rooms r ON o.room_id = r.id",
        "LEFT JOIN hotels h ON r.hotel_id = h.id",
        "WHERE 1=1",
        "<if test='id != null'>",
        "AND o.id = #{id}",
        "</if>",
        "<if test='orderNumber != null and orderNumber != \"\"'>",
        "AND o.order_number = #{orderNumber}",
        "</if>",
        "<if test='customerId != null'>",
        "AND o.customer_id = #{customerId}",
        "</if>",
        "<if test='customerName != null and customerName != \"\"'>",
        "AND c.name LIKE CONCAT('%', #{customerName}, '%')",
        "</if>",
        "<if test='roomId != null'>",
        "AND o.room_id = #{roomId}",
        "</if>",
        "<if test='hotelId != null'>",
        "AND h.id = #{hotelId}",
        "</if>",
        "<if test='hotelName != null and hotelName != \"\"'>",
        "AND h.name LIKE CONCAT('%', #{hotelName}, '%')",
        "</if>",
        "<if test='checkInDateStart != null and checkInDateStart != \"\"'>",
        "AND o.check_in_date &gt;= #{checkInDateStart}",
        "</if>",
        "<if test='checkInDateEnd != null and checkInDateEnd != \"\"'>",
        "AND o.check_in_date &lt;= #{checkInDateEnd}",
        "</if>",
        "<if test='status != null and status != \"\"'>",
        "AND o.status = #{status}",
        "</if>",
        "ORDER BY o.created_at DESC",
        "</script>"
    })
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "orderNumber", column = "order_number"),
        @Result(property = "customerId", column = "customer_id"),
        @Result(property = "roomId", column = "room_id"),
        @Result(property = "checkInDate", column = "check_in_date"),
        @Result(property = "checkOutDate", column = "check_out_date"),
        @Result(property = "totalAmount", column = "total_amount"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "customerName", column = "customerName"),
        @Result(property = "customerPhone", column = "customerPhone"),
        @Result(property = "customerEmail", column = "customerEmail"),
        @Result(property = "customerIdCard", column = "customerIdCard"),
        @Result(property = "roomType", column = "room_type"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "hotelId", column = "hotelId"),
        @Result(property = "hotelName", column = "hotelName"),
        @Result(property = "hotelAddress", column = "hotelAddress")
    })
    List<OrderDto> findOrders(OrderQueryDto query);
    
    /**
     * 根据ID查询订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    @Select({
        "SELECT o.*, c.name AS customerName, c.phone AS customerPhone, c.email AS customerEmail, c.id_card AS customerIdCard,",
        "r.room_type, r.room_number, h.id AS hotelId, h.name AS hotelName, h.address AS hotelAddress",
        "FROM orders o",
        "LEFT JOIN customers c ON o.customer_id = c.id",
        "LEFT JOIN rooms r ON o.room_id = r.id",
        "LEFT JOIN hotels h ON r.hotel_id = h.id",
        "WHERE o.id = #{id}"
    })
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "orderNumber", column = "order_number"),
        @Result(property = "customerId", column = "customer_id"),
        @Result(property = "roomId", column = "room_id"),
        @Result(property = "checkInDate", column = "check_in_date"),
        @Result(property = "checkOutDate", column = "check_out_date"),
        @Result(property = "totalAmount", column = "total_amount"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "customerName", column = "customerName"),
        @Result(property = "customerPhone", column = "customerPhone"),
        @Result(property = "customerEmail", column = "customerEmail"),
        @Result(property = "customerIdCard", column = "customerIdCard"),
        @Result(property = "roomType", column = "room_type"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "hotelId", column = "hotelId"),
        @Result(property = "hotelName", column = "hotelName"),
        @Result(property = "hotelAddress", column = "hotelAddress")
    })
    OrderDto getOrderById(Integer id);
    
    /**
     * 根据ID查询订单实体
     * @param id 订单ID
     * @return 订单实体
     */
    @Select("SELECT * FROM orders WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "orderNumber", column = "order_number"),
        @Result(property = "customerId", column = "customer_id"),
        @Result(property = "roomId", column = "room_id"),
        @Result(property = "checkInDate", column = "check_in_date"),
        @Result(property = "checkOutDate", column = "check_out_date"),
        @Result(property = "totalAmount", column = "total_amount"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    Orders getOrderEntityById(Integer id);
    
    /**
     * 更新订单信息
     * @param order 订单信息
     * @return 影响行数
     */
    @Update({
        "<script>",
        "UPDATE orders",
        "<set>",
        "<if test='checkInDate != null'>check_in_date = #{checkInDate},</if>",
        "<if test='checkOutDate != null'>check_out_date = #{checkOutDate},</if>",
        "<if test='status != null'>status = #{status},</if>",
        "created_at = #{createdAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int updateOrder(Orders order);
    
    /**
     * 更新房间状态
     * @param roomId 房间ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE rooms SET status = #{status} WHERE id = #{roomId}")
    int updateRoomStatus(@Param("roomId") Integer roomId, @Param("status") String status);
}