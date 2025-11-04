import { apiClient } from './client';
import type { BookingStatisticsDto, RevenueStatisticsDto, OccupancyRateDto, ReportQueryDto } from '../types';

export const reportApi = {
  // 获取预订统计信息
  getBookingStatistics: (query: ReportQueryDto) => {
    return apiClient.post<BookingStatisticsDto[]>('/reports/booking-statistics', query);
  },

  // 获取收入统计信息
  getRevenueStatistics: (query: ReportQueryDto) => {
    return apiClient.post<RevenueStatisticsDto[]>('/reports/revenue-statistics', query);
  },

  // 获取入住率统计信息
  getOccupancyRateStatistics: (query: ReportQueryDto) => {
    return apiClient.post<OccupancyRateDto[]>('/reports/occupancy-rate-statistics', query);
  },

  // 导出预订统计报表到Excel
  exportBookingStatistics: (query: ReportQueryDto) => {
    return apiClient.post('/reports/export/booking-statistics', query, {
      responseType: 'blob',
    });
  },

  // 导出收入统计报表到Excel
  exportRevenueStatistics: (query: ReportQueryDto) => {
    return apiClient.post('/reports/export/revenue-statistics', query, {
      responseType: 'blob',
    });
  },

  // 导出入住率统计报表到Excel
  exportOccupancyRateStatistics: (query: ReportQueryDto) => {
    return apiClient.post('/reports/export/occupancy-rate-statistics', query, {
      responseType: 'blob',
    });
  },
};

