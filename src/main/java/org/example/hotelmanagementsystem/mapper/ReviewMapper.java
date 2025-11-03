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
    List<ReviewDto> findReviews(ReviewQueryDto query);
    
    /**
     * 根据ID查询评价详情
     * @param id 评价ID
     * @return 评价详情
     */
    ReviewDto getReviewById(Integer id);
    
    /**
     * 根据订单ID查询评价
     * @param orderId 订单ID
     * @return 评价信息
     */
    Review getReviewByOrderId(Integer orderId);
    
    /**
     * 插入评价信息
     * @param review 评价信息
     * @return 影响行数
     */
    int insertReview(Review review);
    
    /**
     * 更新评价信息
     * @param review 评价信息
     * @return 影响行数
     */
    int updateReview(Review review);
    
    /**
     * 回复评价
     * @param id 评价ID
     * @param adminReply 管理员回复
     * @return 影响行数
     */
    int replyToReview(@Param("id") Integer id, @Param("adminReply") String adminReply);
    
    /**
     * 删除评价
     * @param id 评价ID
     * @return 影响行数
     */
    int deleteReview(Integer id);
}