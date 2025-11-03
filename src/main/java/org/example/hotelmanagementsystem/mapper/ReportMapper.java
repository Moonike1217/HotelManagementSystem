package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.dto.BookingStatisticsDto;
import org.example.hotelmanagementsystem.dto.RevenueStatisticsDto;
import org.example.hotelmanagementsystem.dto.OccupancyRateDto;
import java.util.List;

@Mapper
public interface ReportMapper {
    
    /**
     * 查询预订统计信息
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param hotelId 酒店ID（可选）
     * @return 预订统计列表
     */
    @Select({
        "<script>",
        "SELECT h.id AS hotelId, h.name AS hotelName,",
        "COUNT(o.id) AS totalBookings,",
        "COUNT(CASE WHEN o.status = 'confirmed' THEN 1 END) AS confirmedBookings,",
        "COUNT(CASE WHEN o.status = 'checked_in' THEN 1 END) AS checkInCount,",
        "COUNT(CASE WHEN o.status = 'cancelled' THEN 1 END) AS cancelledBookings,",
        "(COUNT(CASE WHEN o.status IN ('confirmed', 'checked_in', 'checked_out') THEN 1 END) * 100.0 / COUNT(o.id)) AS bookingRate,",
        "(COUNT(CASE WHEN o.status = 'checked_in' THEN 1 END) * 100.0 / COUNT(o.id)) AS checkInRate",
        "FROM hotels h",
        "LEFT JOIN rooms r ON h.id = r.hotel_id",
        "LEFT JOIN orders o ON r.id = o.room_id AND o.created_at &gt;= #{startDate} AND o.created_at &lt;= #{endDate}",
        "WHERE 1=1",
        "<if test='hotelId != null'>",
        "AND h.id = #{hotelId}",
        "</if>",
        "GROUP BY h.id, h.name",
        "ORDER BY h.id",
        "</script>"
    })
    List<BookingStatisticsDto> getBookingStatistics(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("hotelId") Integer hotelId
    );
    
    /**
     * 查询收入统计信息
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param hotelId 酒店ID（可选）
     * @return 收入统计列表
     */
    @Select({
        "<script>",
        "SELECT h.id AS hotelId, h.name AS hotelName,",
        "DATE_FORMAT(o.created_at, '%Y-%m') AS month,",
        "SUM(o.total_amount) AS totalRevenue,",
        "AVG(o.total_amount) AS averageRoomPrice,",
        "COUNT(o.id) AS orderCount",
        "FROM hotels h",
        "LEFT JOIN rooms r ON h.id = r.hotel_id",
        "LEFT JOIN orders o ON r.id = o.room_id AND o.created_at &gt;= #{startDate} AND o.created_at &lt;= #{endDate} AND o.status != 'cancelled'",
        "WHERE 1=1",
        "<if test='hotelId != null'>",
        "AND h.id = #{hotelId}",
        "</if>",
        "GROUP BY h.id, h.name, DATE_FORMAT(o.created_at, '%Y-%m')",
        "ORDER BY h.id, month",
        "</script>"
    })
    List<RevenueStatisticsDto> getRevenueStatistics(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("hotelId") Integer hotelId
    );
    
    /**
     * 查询入住率统计信息
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param hotelId 酒店ID（可选）
     * @return 入住率统计列表
     */
    @Select({
        "<script>",
        "SELECT h.id AS hotelId, h.name AS hotelName,",
        "DATE(o.check_in_date) AS date,",
        "COUNT(r.id) AS totalRooms,",
        "COUNT(CASE WHEN o.status IN ('checked_in', 'checked_out') THEN 1 END) AS occupiedRooms,",
        "(COUNT(CASE WHEN o.status IN ('checked_in', 'checked_out') THEN 1 END) * 100.0 / COUNT(r.id)) AS occupancyRate",
        "FROM hotels h",
        "LEFT JOIN rooms r ON h.id = r.hotel_id",
        "LEFT JOIN orders o ON r.id = o.room_id AND o.check_in_date &gt;= #{startDate} AND o.check_in_date &lt;= #{endDate}",
        "WHERE 1=1",
        "<if test='hotelId != null'>",
        "AND h.id = #{hotelId}",
        "</if>",
        "GROUP BY h.id, h.name, DATE(o.check_in_date)",
        "ORDER BY h.id, date",
        "</script>"
    })
    List<OccupancyRateDto> getOccupancyRateStatistics(
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        @Param("hotelId") Integer hotelId
    );
}