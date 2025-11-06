import { useState } from 'react';
import { Search, Calendar, MapPin, UserCheck, UserPlus } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Label } from '../components/ui/label';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../components/ui/table';
import { Dialog, DialogContent, DialogDescription, DialogBody, DialogFooter, DialogHeader, DialogTitle } from '../components/ui/dialog';
// import { Badge } from '../components/ui/badge';
import { Toast } from '../components/ui/toast';
import { useToast } from '../hooks/useToast';
import { bookingApi, customerApi } from '../api';
import type { AvailableRoomDto, BookingQueryDto, BookingRequestDto } from '../types';

export function Bookings() {
  const [availableRooms, setAvailableRooms] = useState<AvailableRoomDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [isBookingDialogOpen, setIsBookingDialogOpen] = useState(false);
  const [selectedRoom, setSelectedRoom] = useState<AvailableRoomDto | null>(null);
  const { toast, showToast, hideToast } = useToast();
  
  // 用户类型：existing 老用户，new 新用户
  const [customerType, setCustomerType] = useState<'existing' | 'new'>('new');
  const [searchIdCard, setSearchIdCard] = useState('');
  const [searchingCustomer, setSearchingCustomer] = useState(false);

  const [searchQuery, setSearchQuery] = useState<BookingQueryDto>({
    checkInDate: '',
    checkOutDate: '',
    location: '',
    roomType: '',
  });

  const [bookingForm, setBookingForm] = useState<BookingRequestDto>({
    roomId: 0,
    customerName: '',
    customerPhone: '',
    customerEmail: '',
    customerIdCard: '',
    checkInDate: '',
    checkOutDate: '',
  });

  const handleSearch = async () => {
    if (!searchQuery.checkInDate || !searchQuery.checkOutDate) {
      alert('请选择入住和退房日期');
      return;
    }
    setLoading(true);
    try {
      const response = await bookingApi.findAvailableRooms(searchQuery);
      setAvailableRooms(response.data);
    } catch (error) {
      console.error('查询可用房间失败:', error);
      alert('查询失败，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  // 查询老用户信息
  const handleSearchCustomer = async () => {
    if (!searchIdCard.trim()) {
      showToast('请输入身份证号', 'error');
      return;
    }
    setSearchingCustomer(true);
    try {
      const response = await customerApi.getCustomerByIdCard(searchIdCard);
      if (response.data) {
        // 自动填充表单
        setBookingForm({
          ...bookingForm,
          customerName: response.data.name || '',
          customerPhone: response.data.phone || '',
          customerEmail: response.data.email || '',
          customerIdCard: response.data.idCard || '',
        });
        showToast('已找到客户信息', 'success');
      }
    } catch (error) {
      console.error('查询客户失败:', error);
      showToast('未找到该身份证号对应的客户信息', 'error');
    } finally {
      setSearchingCustomer(false);
    }
  };

  const handleBooking = (room: AvailableRoomDto) => {
    setSelectedRoom(room);
    setCustomerType('new'); // 默认新用户
    setSearchIdCard(''); // 清空身份证搜索框
    setBookingForm({
      roomId: room.id,  // 后端返回的字段是 id
      customerName: '',
      customerPhone: '',
      customerEmail: '',
      customerIdCard: '',
      checkInDate: searchQuery.checkInDate,
      checkOutDate: searchQuery.checkOutDate,
    });
    setIsBookingDialogOpen(true);
  };

  const handleSubmitBooking = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await bookingApi.bookRoom(bookingForm);
      // 后端返回 BookingResultDto，只要有 orderNumber 就表示成功
      if (response.data && response.data.orderNumber) {
        showToast(`预订成功！订单号：${response.data.orderNumber}`, 'success');
        setIsBookingDialogOpen(false);
        // 重新搜索可用房间
        handleSearch();
      } else {
        showToast('预订失败，请稍后重试', 'error');
      }
    } catch (error) {
      console.error('预订失败:', error);
      showToast('预订失败，请稍后重试', 'error');
    }
  };

  return (
    <div className="space-y-6">
      {toast.show && (
        <Toast message={toast.message} type={toast.type} onClose={hideToast} />
      )}
      <div>
        <h1 className="text-3xl font-bold">房间预订</h1>
        <p className="text-muted-foreground">查询可用房间并进行预订</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>搜索可用房间</CardTitle>
          <CardDescription>请输入预订条件查询可用房间</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
            <div className="space-y-2">
              <Label htmlFor="checkInDate">入住日期 *</Label>
              <Input
                id="checkInDate"
                type="date"
                value={searchQuery.checkInDate}
                onChange={(e) => setSearchQuery({ ...searchQuery, checkInDate: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="checkOutDate">退房日期 *</Label>
              <Input
                id="checkOutDate"
                type="date"
                value={searchQuery.checkOutDate}
                onChange={(e) => setSearchQuery({ ...searchQuery, checkOutDate: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="location">地点</Label>
              <Input
                id="location"
                placeholder="输入地址关键词"
                value={searchQuery.location}
                onChange={(e) => setSearchQuery({ ...searchQuery, location: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="roomType">房型</Label>
              <Input
                id="roomType"
                placeholder="如：标准间、大床房"
                value={searchQuery.roomType}
                onChange={(e) => setSearchQuery({ ...searchQuery, roomType: e.target.value })}
              />
            </div>
          </div>
          <div className="mt-4">
            <Button onClick={handleSearch}>
              <Search className="mr-2 h-4 w-4" />
              搜索可用房间
            </Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>可用房间列表</CardTitle>
          <CardDescription>共 {availableRooms.length} 间可用房间</CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">查询中...</div>
          ) : availableRooms.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">
              请先设置查询条件并搜索
            </div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>酒店名称</TableHead>
                  <TableHead>地址</TableHead>
                  <TableHead>房型</TableHead>
                  <TableHead>房间号</TableHead>
                  <TableHead>价格</TableHead>
                  <TableHead>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {availableRooms.map((room) => (
                  <TableRow key={`${room.id}-${room.hotelId}`}>
                    <TableCell className="font-medium">{room.hotelName}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1">
                        <MapPin className="h-3 w-3" />
                        {room.hotelAddress}
                      </div>
                    </TableCell>
                    <TableCell>{room.roomType}</TableCell>
                    <TableCell>{room.roomNumber}</TableCell>
                    <TableCell className="font-semibold text-primary">
                      ¥{room.price}
                    </TableCell>
                    <TableCell>
                      <Button size="sm" onClick={() => handleBooking(room)}>
                        <Calendar className="mr-1 h-3 w-3" />
                        预订
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      <Dialog open={isBookingDialogOpen} onOpenChange={setIsBookingDialogOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>预订房间</DialogTitle>
            <DialogDescription>
              {selectedRoom && `${selectedRoom.hotelName} - ${selectedRoom.roomType} ${selectedRoom.roomNumber}`}
            </DialogDescription>
          </DialogHeader>
          <form onSubmit={handleSubmitBooking} className="flex flex-col flex-1 min-h-0">
            <DialogBody>
              <div className="grid gap-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label>入住日期</Label>
                    <Input value={bookingForm.checkInDate} disabled />
                  </div>
                  <div className="space-y-2">
                    <Label>退房日期</Label>
                    <Input value={bookingForm.checkOutDate} disabled />
                  </div>
                </div>

                {/* 用户类型选择 */}
                <div className="space-y-3 p-4 bg-muted/50 rounded-lg">
                  <Label className="text-base font-semibold">客户类型</Label>
                  <div className="flex gap-4">
                    <button
                      type="button"
                      onClick={() => {
                        setCustomerType('new');
                        setSearchIdCard('');
                        setBookingForm({
                          ...bookingForm,
                          customerName: '',
                          customerPhone: '',
                          customerEmail: '',
                          customerIdCard: '',
                        });
                      }}
                      className={`flex items-center gap-2 px-4 py-2 rounded-md border-2 transition-colors ${
                        customerType === 'new'
                          ? 'border-primary bg-primary text-primary-foreground'
                          : 'border-muted-foreground/30 hover:border-muted-foreground/50'
                      }`}
                    >
                      <UserPlus className="h-4 w-4" />
                      新客户
                    </button>
                    <button
                      type="button"
                      onClick={() => setCustomerType('existing')}
                      className={`flex items-center gap-2 px-4 py-2 rounded-md border-2 transition-colors ${
                        customerType === 'existing'
                          ? 'border-primary bg-primary text-primary-foreground'
                          : 'border-muted-foreground/30 hover:border-muted-foreground/50'
                      }`}
                    >
                      <UserCheck className="h-4 w-4" />
                      老客户
                    </button>
                  </div>
                </div>

                {/* 老客户查询 */}
                {customerType === 'existing' && (
                  <div className="space-y-2 p-4 bg-blue-50 dark:bg-blue-950 rounded-lg border border-blue-200 dark:border-blue-800">
                    <Label htmlFor="searchIdCard" className="text-blue-900 dark:text-blue-100">
                      通过身份证号查询客户信息
                    </Label>
                    <div className="flex gap-2">
                      <Input
                        id="searchIdCard"
                        placeholder="请输入身份证号"
                        value={searchIdCard}
                        onChange={(e) => setSearchIdCard(e.target.value)}
                        className="flex-1"
                      />
                      <Button
                        type="button"
                        onClick={handleSearchCustomer}
                        disabled={searchingCustomer}
                      >
                        {searchingCustomer ? '查询中...' : '查询'}
                      </Button>
                    </div>
                  </div>
                )}

                {/* 客户信息表单 */}
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="customerName">客户姓名 *</Label>
                    <Input
                      id="customerName"
                      value={bookingForm.customerName}
                      onChange={(e) => setBookingForm({ ...bookingForm, customerName: e.target.value })}
                      disabled={customerType === 'existing' && !bookingForm.customerName}
                      required
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="customerPhone">联系电话 *</Label>
                    <Input
                      id="customerPhone"
                      value={bookingForm.customerPhone}
                      onChange={(e) => setBookingForm({ ...bookingForm, customerPhone: e.target.value })}
                      disabled={customerType === 'existing' && !bookingForm.customerPhone}
                      required
                    />
                  </div>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <Label htmlFor="customerEmail">电子邮箱 *</Label>
                    <Input
                      id="customerEmail"
                      type="email"
                      value={bookingForm.customerEmail}
                      onChange={(e) => setBookingForm({ ...bookingForm, customerEmail: e.target.value })}
                      disabled={customerType === 'existing' && !bookingForm.customerEmail}
                      required
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="customerIdCard">身份证号 *</Label>
                    <Input
                      id="customerIdCard"
                      value={bookingForm.customerIdCard}
                      onChange={(e) => setBookingForm({ ...bookingForm, customerIdCard: e.target.value })}
                      disabled={customerType === 'existing' && !bookingForm.customerIdCard}
                      required
                    />
                  </div>
                </div>
              </div>
            </DialogBody>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setIsBookingDialogOpen(false)}>
                取消
              </Button>
              <Button type="submit">确认预订</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}

