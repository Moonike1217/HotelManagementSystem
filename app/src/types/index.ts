// 酒店信息
export interface Hotel {
  id?: number;
  name: string;
  address: string;
  phone: string;
  starLevel: number;
  description: string;
  status?: string;
  createdAt?: number;
}

// 房型信息
export interface RoomType {
  id?: number;
  roomType: string;
  roomNumber: string;
  price: number;
}

// 酒店DTO（带房型列表）
export interface HotelDto extends Hotel {
  roomTypes?: RoomType[];
}

// 客户信息
export interface Customer {
  id?: number;
  name: string;
  phone: string;
  email: string;
  idCard: string;
  createdAt?: number;
}

// 客户DTO（带订单历史）
export interface CustomerDto extends Customer {
  orders?: OrderDto[];
}

// 客户查询条件
export interface CustomerQueryDto {
  name?: string;
  phone?: string;
  idCard?: string;
}

// 订单信息
export interface OrderDto {
  id?: number;
  orderNumber?: string;
  customerId?: number;
  customerName?: string;
  customerPhone?: string;
  customerEmail?: string;
  customerIdCard?: string;
  roomId?: number;
  hotelId?: number;
  hotelName?: string;
  hotelAddress?: string;
  roomType?: string;
  roomNumber?: string;
  checkInDate: string;
  checkOutDate: string;
  totalAmount?: number;
  status?: string;
  createdAt?: number;
}

// 订单查询条件
export interface OrderQueryDto {
  customerName?: string;
  customerPhone?: string;
  hotelName?: string;
  status?: string;
  checkInDateFrom?: string;
  checkInDateTo?: string;
}

// 订单更新信息
export interface OrderUpdateDto {
  id: number;
  checkInDate?: string;
  checkOutDate?: string;
  roomId?: number;
}

// 预订查询条件
export interface BookingQueryDto {
  checkInDate: string;
  checkOutDate: string;
  location?: string;
  roomType?: string;
  hotelName?: string;
}

// 可用房间信息
export interface AvailableRoomDto {
  id: number;  // 后端返回的是 id 不是 roomId
  hotelId: number;
  hotelName: string;
  hotelAddress: string;
  roomType: string;
  roomNumber: string;
  price: number;
}

// 预订请求
export interface BookingRequestDto {
  roomId: number;
  customerName: string;
  customerPhone: string;
  customerEmail: string;
  customerIdCard: string;
  checkInDate: string;
  checkOutDate: string;
}

// 预订结果
export interface BookingResultDto {
  orderId: number;
  orderNumber: string;
  customerId: number;
  roomId: number;
  checkInDate: string;
  checkOutDate: string;
  totalAmount: number;
  status: string;
}

// 评价信息
export interface Review {
  id?: number;
  orderId: number;
  customerId?: number;
  hotelId?: number;
  rating: number;
  comment: string;
  reply?: string;
  createdAt?: number;
  replyTime?: number;
}

// 评价DTO
export interface ReviewDto extends Review {
  customerName?: string;
  hotelName?: string;
  orderNumber?: string;
}

// 评价查询条件
export interface ReviewQueryDto {
  hotelId?: number;
  customerId?: number;
  rating?: number;
  hasReply?: boolean;
}

// 评价创建信息
export interface ReviewCreateDto {
  orderId: number;
  rating: number;
  comment: string;
}

// 评价回复信息
export interface ReviewReplyDto {
  id: number;
  reply: string;
}

// 报表查询条件
export interface ReportQueryDto {
  hotelId?: number;
  startDate?: string;
  endDate?: string;
}

// 预订统计
export interface BookingStatisticsDto {
  date: string;
  hotelId: number;
  hotelName: string;
  bookingCount: number;
  totalRevenue: number;
}

// 收入统计
export interface RevenueStatisticsDto {
  date: string;
  hotelId: number;
  hotelName: string;
  revenue: number;
  orderCount: number;
}

// 入住率统计
export interface OccupancyRateDto {
  date: string;
  hotelId: number;
  hotelName: string;
  totalRooms: number;
  occupiedRooms: number;
  occupancyRate: number;
}

