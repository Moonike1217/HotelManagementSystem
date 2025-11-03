package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.entity.Hotel;
import java.util.List;

@Mapper
public interface HotelMapper {
    
    /**
     * 插入酒店信息
     * @param hotel 酒店实体
     * @return 影响行数
     */
    @Insert("INSERT INTO hotels(name, address, phone, star_level, description, status, created_at) " +
            "VALUES(#{name}, #{address}, #{phone}, #{starLevel}, #{description}, #{status}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Hotel hotel);
    
    /**
     * 根据ID更新酒店信息
     * @param hotel 酒店实体
     * @return 影响行数
     */
    int updateById(Hotel hotel);
    
    /**
     * 根据ID查询酒店信息
     * @param id 酒店ID
     * @return 酒店实体
     */
    @Select("SELECT * FROM hotels WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "address", column = "address"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "starLevel", column = "star_level"),
        @Result(property = "description", column = "description"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    Hotel selectById(Integer id);
    
    /**
     * 查询所有酒店信息
     * @return 酒店列表
     */
    @Select("SELECT * FROM hotels")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "address", column = "address"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "starLevel", column = "star_level"),
        @Result(property = "description", column = "description"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Hotel> selectAll();
    
    /**
     * 根据名称模糊查询酒店
     * @param name 酒店名称
     * @return 酒店列表
     */
    @Select("SELECT * FROM hotels WHERE name LIKE CONCAT('%', #{name}, '%')")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "address", column = "address"),
        @Result(property = "phone", column = "phone"),
        @Result(property = "starLevel", column = "star_level"),
        @Result(property = "description", column = "description"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Hotel> selectByName(String name);
}