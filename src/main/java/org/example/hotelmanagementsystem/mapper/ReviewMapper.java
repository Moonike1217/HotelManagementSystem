package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.entity.Review;
import org.example.hotelmanagementsystem.dto.ReviewDto;
import org.example.hotelmanagementsystem.dto.ReviewQueryDto;
import java.util.List;

@Mapper
public interface ReviewMapper {
    
    /**
     * 根据条件查询评价列表
     * @param query 查询条件
     * @return 评价列表
     */
    @Select({
        "<script>",
        "SELECT r.*, c.name AS customerName, h.name AS hotelName",
        "FROM reviews r",
        "LEFT JOIN customers c ON r.customer_id = c.id",
        "LEFT JOIN hotels h ON r.hotel_id = h.id",
        "WHERE 1=1",
        "<if test='id != null'>",
        "AND r.id = #{id}",
        "</if>",
        "<if test='orderId != null'>",
        "AND r.order_id = #{orderId}",
        "</if>",
        "<if test='customerId != null'>",
        "AND r.customer_id = #{customerId}",
        "</if>",
        "<if test='customerName != null and customerName != \"\"'>",
        "AND c.name LIKE CONCAT('%', #{customerName}, '%')",
        "</if>",
        "<if test='hotelId != null'>",
        "AND r.hotel_id = #{hotelId}",
        "</if>",
        "<if test='hotelName != null and hotelName != \"\"'>",
        "AND h.name LIKE CONCAT('%', #{hotelName}, '%')",
        "</if>",
        "<if test='minRating != null'>",
        "AND r.rating &gt;= #{minRating}",
        "</if>",
        "<if test='maxRating != null'>",
        "AND r.rating &lt;= #{maxRating}",
        "</if>",
        "<if test='hasReply != null and hasReply == true'>",
        "AND r.admin_reply IS NOT NULL AND r.admin_reply != ''",
        "</if>",
        "<if test='hasReply != null and hasReply == false'>",
        "AND (r.admin_reply IS NULL OR r.admin_reply = '')",
        "</if>",
        "ORDER BY r.created_at DESC",
        "</script>"
    })
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "customerId", column = "customer_id"),
        @Result(property = "hotelId", column = "hotel_id"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "comment", column = "comment"),
        @Result(property = "adminReply", column = "admin_reply"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "customerName", column = "customerName"),
        @Result(property = "hotelName", column = "hotelName")
    })
    List<ReviewDto> findReviews(ReviewQueryDto query);
    
    /**
     * 根据ID查询评价详情
     * @param id 评价ID
     * @return 评价详情
     */
    @Select({
        "SELECT r.*, c.name AS customerName, h.name AS hotelName",
        "FROM reviews r",
        "LEFT JOIN customers c ON r.customer_id = c.id",
        "LEFT JOIN hotels h ON r.hotel_id = h.id",
        "WHERE r.id = #{id}"
    })
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "customerId", column = "customer_id"),
        @Result(property = "hotelId", column = "hotel_id"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "comment", column = "comment"),
        @Result(property = "adminReply", column = "admin_reply"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "customerName", column = "customerName"),
        @Result(property = "hotelName", column = "hotelName")
    })
    ReviewDto getReviewById(Integer id);
    
    /**
     * 根据订单ID查询评价
     * @param orderId 订单ID
     * @return 评价信息
     */
    @Select("SELECT * FROM reviews WHERE order_id = #{orderId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "customerId", column = "customer_id"),
        @Result(property = "hotelId", column = "hotel_id"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "comment", column = "comment"),
        @Result(property = "adminReply", column = "admin_reply"),
        @Result(property = "createdAt", column = "created_at")
    })
    Review getReviewByOrderId(Integer orderId);
    
    /**
     * 插入评价信息
     * @param review 评价信息
     * @return 影响行数
     */
    @Insert("INSERT INTO reviews(order_id, customer_id, hotel_id, rating, comment, created_at) " +
            "VALUES(#{orderId}, #{customerId}, #{hotelId}, #{rating}, #{comment}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertReview(Review review);
    
    /**
     * 更新评价信息
     * @param review 评价信息
     * @return 影响行数
     */
    @Update({
        "<script>",
        "UPDATE reviews",
        "<set>",
        "<if test='rating != null'>rating = #{rating},</if>",
        "<if test='comment != null'>comment = #{comment},</if>",
        "created_at = #{createdAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int updateReview(Review review);
    
    /**
     * 回复评价
     * @param id 评价ID
     * @param adminReply 管理员回复
     * @return 影响行数
     */
    @Update("UPDATE reviews SET admin_reply = #{adminReply} WHERE id = #{id}")
    int replyToReview(@Param("id") Integer id, @Param("adminReply") String adminReply);
    
    /**
     * 删除评价
     * @param id 评价ID
     * @return 影响行数
     */
    @Delete("DELETE FROM reviews WHERE id = #{id}")
    int deleteReview(Integer id);
}