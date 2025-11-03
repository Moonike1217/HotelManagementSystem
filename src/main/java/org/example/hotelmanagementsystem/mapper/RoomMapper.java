package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.entity.Room;
import java.util.List;

@Mapper
public interface RoomMapper {
    
    /**
     * 插入房间信息
     * @param room 房间实体
     * @return 影响行数
     */
    @Insert("INSERT INTO rooms(hotel_id, room_type, room_number, price, status, created_at) " +
            "VALUES(#{hotelId}, #{roomType}, #{roomNumber}, #{price}, #{status}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Room room);
    
    /**
     * 根据ID更新房间信息
     * @param room 房间实体
     * @return 影响行数
     */
    @Update("UPDATE rooms SET hotel_id=#{hotelId}, room_type=#{roomType}, room_number=#{roomNumber}, " +
            "price=#{price}, status=#{status}, created_at=#{createdAt} " +
            "WHERE id=#{id}")
    int updateById(Room room);
    
    /**
     * 根据ID查询房间信息
     * @param id 房间ID
     * @return 房间实体
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
    Room selectById(Integer id);
    
    /**
     * 查询所有房间信息
     * @return 房间列表
     */
    @Select("SELECT * FROM rooms")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "hotelId", column = "hotel_id"),
        @Result(property = "roomType", column = "room_type"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "price", column = "price"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Room> selectAll();
    
    /**
     * 根据酒店ID查询房间列表
     * @param hotelId 酒店ID
     * @return 房间列表
     */
    @Select("SELECT * FROM rooms WHERE hotel_id = #{hotelId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "hotelId", column = "hotel_id"),
        @Result(property = "roomType", column = "room_type"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "price", column = "price"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Room> selectByHotelId(Integer hotelId);
    
    /**
     * 根据房间类型查询房间列表
     * @param roomType 房间类型
     * @return 房间列表
     */
    @Select("SELECT * FROM rooms WHERE room_type = #{roomType}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "hotelId", column = "hotel_id"),
        @Result(property = "roomType", column = "room_type"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "price", column = "price"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Room> selectByRoomType(String roomType);
    
    /**
     * 根据房间状态查询房间列表
     * @param status 房间状态
     * @return 房间列表
     */
    @Select("SELECT * FROM rooms WHERE status = #{status}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "hotelId", column = "hotel_id"),
        @Result(property = "roomType", column = "room_type"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "price", column = "price"),
        @Result(property = "status", column = "status"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<Room> selectByStatus(String status);
    
    /**
     * 根据ID删除房间信息
     * @param id 房间ID
     * @return 影响行数
     */
    @Delete("DELETE FROM rooms WHERE id = #{id}")
    int deleteById(Integer id);
}