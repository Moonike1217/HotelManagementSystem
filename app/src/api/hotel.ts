import { apiClient } from './client';
import type { Hotel, HotelDto } from '../types';

export const hotelApi = {
  // 添加酒店
  addHotel: (hotel: HotelDto) => {
    return apiClient.post<boolean>('/hotels', hotel);
  },

  // 更新酒店信息
  updateHotel: (hotel: HotelDto) => {
    return apiClient.put<boolean>('/hotels', hotel);
  },

  // 根据ID查询酒店
  getHotelById: (id: number) => {
    return apiClient.get<HotelDto>(`/hotels/${id}`);
  },

  // 查询所有酒店
  getAllHotels: () => {
    return apiClient.get<Hotel[]>('/hotels');
  },

  // 根据名称搜索酒店
  searchHotelsByName: (name: string) => {
    return apiClient.get<Hotel[]>('/hotels/search', {
      params: { name },
    });
  },
};

