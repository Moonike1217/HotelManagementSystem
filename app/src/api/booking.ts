import { apiClient } from './client';
import type { BookingQueryDto, AvailableRoomDto, BookingRequestDto, BookingResultDto } from '../types';

export const bookingApi = {
  // 查询可用房间
  findAvailableRooms: (query: BookingQueryDto) => {
    return apiClient.post<AvailableRoomDto[]>('/bookings/search', query);
  },

  // 预订房间
  bookRoom: (booking: BookingRequestDto) => {
    return apiClient.post<BookingResultDto>('/bookings', booking);
  },
};

