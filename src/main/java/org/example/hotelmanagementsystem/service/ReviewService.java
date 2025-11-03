package org.example.hotelmanagementsystem.service;

import org.example.hotelmanagementsystem.entity.Review;
import org.example.hotelmanagementsystem.dto.ReviewDto;
import org.example.hotelmanagementsystem.dto.ReviewQueryDto;
import org.example.hotelmanagementsystem.dto.ReviewCreateDto;
import org.example.hotelmanagementsystem.dto.ReviewReplyDto;
import java.util.List;

public interface ReviewService {
    
    /**
     * 查询评价列表
     * @param query 查询条件
     * @return 评价列表
     */
    List<ReviewDto> findReviews(ReviewQueryDto query);
    
    /**
     * 根据ID获取评价详情
     * @param id 评价ID
     * @return 评价详情
     */
    ReviewDto getReviewById(Integer id);
    
    /**
     * 根据订单ID获取评价
     * @param orderId 订单ID
     * @return 评价信息
     */
    Review getReviewByOrderId(Integer orderId);
    
    /**
     * 添加评价
     * @param reviewCreateDto 评价信息
     * @return 是否添加成功
     */
    boolean addReview(ReviewCreateDto reviewCreateDto);
    
    /**
     * 更新评价信息
     * @param review 评价信息
     * @return 是否更新成功
     */
    boolean updateReview(Review review);
    
    /**
     * 回复评价
     * @param reviewReplyDto 回复信息
     * @return 是否回复成功
     */
    boolean replyToReview(ReviewReplyDto reviewReplyDto);
    
    /**
     * 删除评价
     * @param id 评价ID
     * @return 是否删除成功
     */
    boolean deleteReview(Integer id);
}