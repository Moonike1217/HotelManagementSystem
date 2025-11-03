package org.example.hotelmanagementsystem.controller;

import org.example.hotelmanagementsystem.entity.Review;
import org.example.hotelmanagementsystem.dto.ReviewDto;
import org.example.hotelmanagementsystem.dto.ReviewQueryDto;
import org.example.hotelmanagementsystem.dto.ReviewCreateDto;
import org.example.hotelmanagementsystem.dto.ReviewReplyDto;
import org.example.hotelmanagementsystem.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    /**
     * 查询评价列表
     * @param query 查询条件
     * @return 评价列表
     */
    @PostMapping("/search")
    public List<ReviewDto> findReviews(@RequestBody ReviewQueryDto query) {
        return reviewService.findReviews(query);
    }
    
    /**
     * 根据ID获取评价详情
     * @param id 评价ID
     * @return 评价详情
     */
    @GetMapping("/{id}")
    public ReviewDto getReviewById(@PathVariable Integer id) {
        return reviewService.getReviewById(id);
    }
    
    /**
     * 根据订单ID获取评价
     * @param orderId 订单ID
     * @return 评价信息
     */
    @GetMapping("/order/{orderId}")
    public Review getReviewByOrderId(@PathVariable Integer orderId) {
        return reviewService.getReviewByOrderId(orderId);
    }
    
    /**
     * 添加评价
     * @param reviewCreateDto 评价信息
     * @return 是否添加成功
     */
    @PostMapping
    public boolean addReview(@RequestBody ReviewCreateDto reviewCreateDto) {
        try {
            return reviewService.addReview(reviewCreateDto);
        } catch (Exception e) {
            throw new RuntimeException("添加评价失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新评价信息
     * @param review 评价信息
     * @return 是否更新成功
     */
    @PutMapping
    public boolean updateReview(@RequestBody Review review) {
        try {
            return reviewService.updateReview(review);
        } catch (Exception e) {
            throw new RuntimeException("更新评价失败: " + e.getMessage());
        }
    }
    
    /**
     * 回复评价
     * @param reviewReplyDto 回复信息
     * @return 是否回复成功
     */
    @PutMapping("/reply")
    public boolean replyToReview(@RequestBody ReviewReplyDto reviewReplyDto) {
        try {
            return reviewService.replyToReview(reviewReplyDto);
        } catch (Exception e) {
            throw new RuntimeException("回复评价失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除评价
     * @param id 评价ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public boolean deleteReview(@PathVariable Integer id) {
        try {
            return reviewService.deleteReview(id);
        } catch (Exception e) {
            throw new RuntimeException("删除评价失败: " + e.getMessage());
        }
    }
}