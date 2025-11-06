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
  orderNumber?: string;
  customerName?: string;
  hotelName?: string;
  checkInDateStart?: string;
  checkInDateEnd?: string;
  status?: string;
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

// 预订统计（匹配后端BookingStatisticsDto）
export interface BookingStatisticsDto {
  hotelId?: number;
  hotelName?: string;
  totalBookings: number;        // 总预订数
  confirmedBookings: number;    // 确认预订数
  checkInCount: number;         // 入住数
  cancelledBookings: number;    // 取消数
  bookingRate?: number;         // 预订率 (%)
  checkInRate?: number;         // 入住率 (%)
}

// 收入统计（匹配后端RevenueStatisticsDto）
export interface RevenueStatisticsDto {
  hotelId?: number;
  hotelName?: string;
  month?: string;               // 月份
  totalRevenue: number;         // 总收入（BigDecimal）
  averageRoomPrice?: number;    // 平均房价
  orderCount: number;           // 订单数
}

// 入住率统计（匹配后端OccupancyRateDto）
export interface OccupancyRateDto {
  hotelId?: number;
  hotelName?: string;
  date?: string;                // 日期
  totalRooms: number;           // 总房间数
  occupiedRooms: number;        // 已占用房间数
  occupancyRate: number;        // 入住率 (%)
}

