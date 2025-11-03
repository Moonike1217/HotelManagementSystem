package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.entity.Review;
import org.example.hotelmanagementsystem.dto.ReviewDto;
import org.example.hotelmanagementsystem.dto.ReviewQueryDto;
import org.example.hotelmanagementsystem.dto.ReviewCreateDto;
import org.example.hotelmanagementsystem.dto.ReviewReplyDto;
import org.example.hotelmanagementsystem.mapper.ReviewMapper;
import org.example.hotelmanagementsystem.service.ReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private ReviewMapper reviewMapper;
    
    @Override
    public List<ReviewDto> findReviews(ReviewQueryDto query) {
        return reviewMapper.findReviews(query);
    }
    
    @Override
    public ReviewDto getReviewById(Integer id) {
        return reviewMapper.getReviewById(id);
    }
    
    @Override
    public Review getReviewByOrderId(Integer orderId) {
        return reviewMapper.getReviewByOrderId(orderId);
    }
    
    @Override
    public boolean addReview(ReviewCreateDto reviewCreateDto) {
        // 检查订单是否已经评价过
        Review existingReview = reviewMapper.getReviewByOrderId(reviewCreateDto.getOrderId());
        if (existingReview != null) {
            throw new RuntimeException("该订单已经评价过");
        }
        
        // 创建评价对象
        Review review = new Review();
        BeanUtils.copyProperties(reviewCreateDto, review);
        
        // 插入评价
        int result = reviewMapper.insertReview(review);
        return result > 0;
    }
    
    @Override
    public boolean updateReview(Review review) {
        int result = reviewMapper.updateReview(review);
        return result > 0;
    }
    
    @Override
    public boolean replyToReview(ReviewReplyDto reviewReplyDto) {
        int result = reviewMapper.replyToReview(reviewReplyDto.getId(), reviewReplyDto.getAdminReply());
        return result > 0;
    }
    
    @Override
    public boolean deleteReview(Integer id) {
        int result = reviewMapper.deleteReview(id);
        return result > 0;
    }
}