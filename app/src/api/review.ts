import { apiClient } from './client';
import type { Review, ReviewDto, ReviewQueryDto, ReviewCreateDto, ReviewReplyDto } from '../types';

export const reviewApi = {
  // 查询评价列表
  findReviews: (query: ReviewQueryDto) => {
    return apiClient.post<ReviewDto[]>('/reviews/search', query);
  },

  // 根据ID获取评价详情
  getReviewById: (id: number) => {
    return apiClient.get<ReviewDto>(`/reviews/${id}`);
  },

  // 根据订单ID获取评价
  getReviewByOrderId: (orderId: number) => {
    return apiClient.get<Review>(`/reviews/order/${orderId}`);
  },

  // 添加评价
  addReview: (review: ReviewCreateDto) => {
    return apiClient.post<boolean>('/reviews', review);
  },

  // 更新评价信息
  updateReview: (review: Review) => {
    return apiClient.put<boolean>('/reviews', review);
  },

  // 回复评价
  replyToReview: (reply: ReviewReplyDto) => {
    return apiClient.put<boolean>('/reviews/reply', reply);
  },

  // 删除评价
  deleteReview: (id: number) => {
    return apiClient.delete<boolean>(`/reviews/${id}`);
  },
};

