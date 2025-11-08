package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.entity.Review;
import org.example.hotelmanagementsystem.dto.ReviewDto;
import org.example.hotelmanagementsystem.dto.ReviewQueryDto;
import org.example.hotelmanagementsystem.dto.ReviewCreateDto;
import org.example.hotelmanagementsystem.dto.ReviewReplyDto;
import org.example.hotelmanagementsystem.mapper.ReviewMapper;
import org.example.hotelmanagementsystem.service.ReviewService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    
    @Autowired
    private ReviewMapper reviewMapper;
    
    @Override
    public List<ReviewDto> findReviews(ReviewQueryDto query) {
        logger.debug("查询评价列表，查询条件: {}", query);
        List<ReviewDto> reviews = reviewMapper.findReviews(query);
        logger.debug("查询到 {} 条评价", reviews.size());
        return reviews;
    }
    
    @Override
    public ReviewDto getReviewById(Integer id) {
        logger.debug("根据ID查询评价详情，评价ID: {}", id);
        ReviewDto review = reviewMapper.getReviewById(id);
        if (review != null) {
            logger.debug("成功获取评价详情，评价ID: {}", id);
        } else {
            logger.warn("未找到ID为 {} 的评价", id);
        }
        return review;
    }
    
    @Override
    public Review getReviewByOrderId(Integer orderId) {
        logger.debug("根据订单ID查询评价，订单ID: {}", orderId);
        Review review = reviewMapper.getReviewByOrderId(orderId);
        if (review != null) {
            logger.debug("成功获取订单评价，订单ID: {}", orderId);
        } else {
            logger.warn("未找到订单ID为 {} 的评价", orderId);
        }
        return review;
    }
    
    @Override
    public boolean addReview(ReviewCreateDto reviewCreateDto) {
        logger.info("添加新评价，订单ID: {}", reviewCreateDto.getOrderId());
        // 检查订单是否已经评价过
        Review existingReview = reviewMapper.getReviewByOrderId(reviewCreateDto.getOrderId());
        if (existingReview != null) {
            logger.warn("该订单已经评价过，订单ID: {}", reviewCreateDto.getOrderId());
            throw new RuntimeException("该订单已经评价过");
        }
        
        // 创建评价对象
        Review review = new Review();
        BeanUtils.copyProperties(reviewCreateDto, review);
        // 设置创建时间
        if (review.getCreatedAt() == null) {
            review.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        }
        
        // 插入评价
        int result = reviewMapper.insertReview(review);
        boolean success = result > 0;
        if (success) {
            logger.info("评价添加成功，评价ID: {}", review.getId());
        } else {
            logger.error("评价添加失败，评价信息: {}", reviewCreateDto);
        }
        return success;
    }
    
    @Override
    public boolean updateReview(Review review) {
        logger.info("更新评价信息，评价ID: {}", review.getId());
        // 设置更新时间
        review.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = reviewMapper.updateReview(review);
        boolean success = result > 0;
        if (success) {
            logger.info("评价信息更新成功，评价ID: {}", review.getId());
        } else {
            logger.error("评价信息更新失败，评价ID: {}", review.getId());
        }
        return success;
    }
    
    @Override
    public boolean replyToReview(ReviewReplyDto reviewReplyDto) {
        logger.info("回复评价，评价ID: {}", reviewReplyDto.getId());
        int result = reviewMapper.replyToReview(reviewReplyDto.getId(), reviewReplyDto.getReply());
        boolean success = result > 0;
        if (success) {
            logger.info("评价回复成功，评价ID: {}", reviewReplyDto.getId());
        } else {
            logger.error("评价回复失败，评价ID: {}", reviewReplyDto.getId());
        }
        return success;
    }
    
    @Override
    public boolean deleteReview(Integer id) {
        logger.info("删除评价，评价ID: {}", id);
        int result = reviewMapper.deleteReview(id);
        boolean success = result > 0;
        if (success) {
            logger.info("评价删除成功，评价ID: {}", id);
        } else {
            logger.error("评价删除失败，评价ID: {}", id);
        }
        return success;
    }
}