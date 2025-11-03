package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.entity.Customer;
import org.example.hotelmanagementsystem.dto.CustomerDto;
import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.dto.CustomerQueryDto;
import java.util.List;

@Mapper
public interface CustomerMapper {
    
    /**
     * 根据条件查询客户列表
     * @param query 查询条件
     * @return 客户列表
     */
    @Select({
        "<script>",
        "SELECT * FROM customers WHERE 1=1",
        "<if test='id != null'>",
        "AND id = #{id}",
        "</if>",
        "<if test='name != null and name != \"\"'>",
        "AND name LIKE CONCAT('%', #{name}, '%')",
        "</if>",
        "<if test='phone != null and phone != \"\"'>",
        "AND phone LIKE CONCAT('%', #{phone}, '%')",
        "</if>",
        "<if test='email != null and email != \"\"'>",
        "AND email LIKE CONCAT('%', #{email}, '%')",
        "</if>",
        "<if test='idCard != null and idCard != \"\"'>",
        "AND id_card = #{idCard}",
        "</if>",
        "ORDER BY created_at DESC",
        "</script>"
    })
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "email", column = "email"),
        @Result(property = "idCard", column = "id_card"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Customer> findCustomers(CustomerQueryDto query);
    
    /**
     * 根据ID查询客户详情
     * @param id 客户ID
     * @return 客户信息
     */
    @Select("SELECT * FROM customers WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "email", column = "email"),
        @Result(property = "idCard", column = "id_card"),
        @Result(property = "createdAt", column = "created_at")
    })
    Customer getCustomerById(Integer id);
    
    /**
     * 根据身份证号查询客户
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
    Customer getCustomerByIdCard(String idCard);
    
    /**
     * 根据客户ID查询历史订单
     * @param customerId 客户ID
     * @return 订单列表
     */
    @Select({
        "SELECT o.*, r.room_type, r.room_number, h.id AS hotelId, h.name AS hotelName, h.address AS hotelAddress",
        "FROM orders o",
        "LEFT JOIN rooms r ON o.room_id = r.id",
        "LEFT JOIN hotels h ON r.hotel_id = h.id",
        "WHERE o.customer_id = #{customerId}",
        "ORDER BY o.created_at DESC"
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
        @Result(property = "roomType", column = "room_type"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "hotelId", column = "hotelId"),
        @Result(property = "hotelName", column = "hotelName"),
        @Result(property = "hotelAddress", column = "hotelAddress")
    })
    List<OrderDto> getCustomerOrderHistory(Integer customerId);
    
    /**
     * 插入客户信息
     * @param customer 客户信息
     * @return 影响行数
     */
    @Insert("INSERT INTO customers(name, phone, email, id_card, created_at) " +
            "VALUES(#{name}, #{phone}, #{email}, #{idCard}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCustomer(Customer customer);
    
    /**
     * 更新客户信息
     * @param customer 客户信息
     * @return 影响行数
     */
    @Update({
        "<script>",
        "UPDATE customers",
        "<set>",
        "<if test='name != null'>name = #{name},</if>",
        "<if test='phone != null'>phone = #{phone},</if>",
        "<if test='email != null'>email = #{email},</if>",
        "<if test='idCard != null'>id_card = #{idCard},</if>",
        "created_at = #{createdAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int updateCustomer(Customer customer);
}