import { apiClient } from './client';
import type { OrderDto, OrderQueryDto, OrderUpdateDto } from '../types';

export const orderApi = {
  // 查询订单列表
  findOrders: (query: OrderQueryDto) => {
    return apiClient.post<OrderDto[]>('/orders/search', query);
  },

  // 根据ID获取订单详情
  getOrderById: (id: number) => {
    return apiClient.get<OrderDto>(`/orders/${id}`);
  },

  // 更新订单信息
  updateOrder: (order: OrderUpdateDto) => {
    return apiClient.put<boolean>('/orders', order);
  },

  // 确认订单
  confirmOrder: (id: number) => {
    return apiClient.put<boolean>(`/orders/${id}/confirm`);
  },

  // 取消订单
  cancelOrder: (id: number) => {
    return apiClient.put<boolean>(`/orders/${id}/cancel`);
  },

  // 办理入住
  checkInOrder: (id: number) => {
    return apiClient.put<boolean>(`/orders/${id}/check-in`);
  },

  // 办理退房
  checkOutOrder: (id: number) => {
    return apiClient.put<boolean>(`/orders/${id}/check-out`);
  },
};

