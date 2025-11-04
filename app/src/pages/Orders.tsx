import { useEffect, useState } from 'react';
import { Search, CheckCircle, XCircle, LogIn, LogOut, Bell } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Label } from '../components/ui/label';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../components/ui/table';
import { Badge } from '../components/ui/badge';
import { Select } from '../components/ui/select';
import { Toast } from '../components/ui/toast';
import { useToast } from '../hooks/useToast';
import { orderApi } from '../api';
import type { OrderDto, OrderQueryDto } from '../types';
import { format } from 'date-fns';

const statusMap: Record<string, { label: string; variant: 'default' | 'secondary' | 'destructive' | 'outline' }> = {
  pending: { label: '待确认', variant: 'secondary' },
  confirmed: { label: '已确认', variant: 'default' },
  checked_in: { label: '已入住', variant: 'default' },
  checked_out: { label: '已退房', variant: 'outline' },
  cancelled: { label: '已取消', variant: 'destructive' },
};

export function Orders() {
  const [orders, setOrders] = useState<OrderDto[]>([]);
  const [loading, setLoading] = useState(false);
  const { toast, showToast, hideToast } = useToast();
  const [searchQuery, setSearchQuery] = useState<OrderQueryDto>({
    customerName: '',
    customerPhone: '',
    hotelName: '',
    status: '',
  });

  const loadOrders = async () => {
    setLoading(true);
    try {
      const response = await orderApi.findOrders({});
      setOrders(response.data);
    } catch (error) {
      console.error('加载订单列表失败:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOrders();
  }, []);

  const handleSearch = async () => {
    setLoading(true);
    try {
      const response = await orderApi.findOrders(searchQuery);
      setOrders(response.data);
    } catch (error) {
      console.error('搜索订单失败:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleConfirm = async (id: number) => {
    try {
      await orderApi.confirmOrder(id);
      showToast('订单已确认', 'success');
      loadOrders();
    } catch (error) {
      console.error('确认订单失败:', error);
      showToast('操作失败，请稍后重试', 'error');
    }
  };

  const handleCancel = async (id: number) => {
    if (!confirm('确定要取消此订单吗？')) return;
    try {
      await orderApi.cancelOrder(id);
      showToast('订单已取消', 'success');
      loadOrders();
    } catch (error) {
      console.error('取消订单失败:', error);
      showToast('操作失败，请稍后重试', 'error');
    }
  };

  const handleCheckIn = async (id: number) => {
    try {
      await orderApi.checkInOrder(id);
      showToast('办理入住成功', 'success');
      loadOrders();
    } catch (error) {
      console.error('办理入住失败:', error);
      showToast('操作失败，请稍后重试', 'error');
    }
  };

  const handleCheckOut = async (id: number) => {
    try {
      await orderApi.checkOutOrder(id);
      showToast('办理退房成功', 'success');
      loadOrders();
    } catch (error) {
      console.error('办理退房失败:', error);
      showToast('操作失败，请稍后重试', 'error');
    }
  };

  const handleNotify = (order: OrderDto) => {
    showToast(`已向 ${order.customerName} (${order.customerPhone}) 发送入住提醒`, 'info');
  };

  const formatTimestamp = (timestamp?: number) => {
    if (!timestamp) return '-';
    return format(new Date(timestamp * 1000), 'yyyy-MM-dd HH:mm');
  };

  return (
    <div className="space-y-6">
      {toast.show && (
        <Toast message={toast.message} type={toast.type} onClose={hideToast} />
      )}
      <div>
        <h1 className="text-3xl font-bold">订单管理</h1>
        <p className="text-muted-foreground">管理所有预订订单</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>搜索订单</CardTitle>
          <CardDescription>根据客户信息、酒店名称或状态搜索</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
            <div className="space-y-2">
              <Label htmlFor="customerName">客户姓名</Label>
              <Input
                id="customerName"
                placeholder="输入客户姓名"
                value={searchQuery.customerName}
                onChange={(e) => setSearchQuery({ ...searchQuery, customerName: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="customerPhone">客户电话</Label>
              <Input
                id="customerPhone"
                placeholder="输入客户电话"
                value={searchQuery.customerPhone}
                onChange={(e) => setSearchQuery({ ...searchQuery, customerPhone: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="hotelName">酒店名称</Label>
              <Input
                id="hotelName"
                placeholder="输入酒店名称"
                value={searchQuery.hotelName}
                onChange={(e) => setSearchQuery({ ...searchQuery, hotelName: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="status">订单状态</Label>
              <Select
                id="status"
                value={searchQuery.status}
                onChange={(e) => setSearchQuery({ ...searchQuery, status: e.target.value })}
              >
                <option value="">全部</option>
                <option value="pending">待确认</option>
                <option value="confirmed">已确认</option>
                <option value="checked_in">已入住</option>
                <option value="checked_out">已退房</option>
                <option value="cancelled">已取消</option>
              </Select>
            </div>
          </div>
          <div className="mt-4 flex gap-2">
            <Button onClick={handleSearch}>
              <Search className="mr-2 h-4 w-4" />
              搜索
            </Button>
            <Button variant="outline" onClick={loadOrders}>
              显示全部
            </Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>订单列表</CardTitle>
          <CardDescription>共 {orders.length} 条订单</CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">加载中...</div>
          ) : orders.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">暂无订单数据</div>
          ) : (
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>订单号</TableHead>
                    <TableHead>客户信息</TableHead>
                    <TableHead>酒店信息</TableHead>
                    <TableHead>入住/退房</TableHead>
                    <TableHead>金额</TableHead>
                    <TableHead>状态</TableHead>
                    <TableHead>创建时间</TableHead>
                    <TableHead>操作</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {orders.map((order) => (
                    <TableRow key={order.id}>
                      <TableCell className="font-medium">{order.orderNumber}</TableCell>
                      <TableCell>
                        <div className="space-y-1">
                          <div>{order.customerName}</div>
                          <div className="text-xs text-muted-foreground">{order.customerPhone}</div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <div className="space-y-1">
                          <div>{order.hotelName}</div>
                          <div className="text-xs text-muted-foreground">
                            {order.roomType} {order.roomNumber}
                          </div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <div className="text-sm">
                          <div>{order.checkInDate}</div>
                          <div className="text-muted-foreground">至 {order.checkOutDate}</div>
                        </div>
                      </TableCell>
                      <TableCell className="font-semibold text-primary">
                        ¥{order.totalAmount}
                      </TableCell>
                      <TableCell>
                        <Badge variant={statusMap[order.status || 'pending']?.variant}>
                          {statusMap[order.status || 'pending']?.label}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-sm text-muted-foreground">
                        {formatTimestamp(order.createdAt)}
                      </TableCell>
                      <TableCell>
                        <div className="flex flex-wrap gap-1">
                          {order.status === 'pending' && (
                            <>
                              <Button size="sm" variant="outline" onClick={() => handleConfirm(order.id!)}>
                                <CheckCircle className="h-3 w-3" />
                              </Button>
                              <Button size="sm" variant="outline" onClick={() => handleCancel(order.id!)}>
                                <XCircle className="h-3 w-3" />
                              </Button>
                            </>
                          )}
                          {order.status === 'confirmed' && (
                            <>
                              <Button size="sm" variant="outline" onClick={() => handleCheckIn(order.id!)}>
                                <LogIn className="h-3 w-3" />
                              </Button>
                              <Button size="sm" variant="outline" onClick={() => handleNotify(order)}>
                                <Bell className="h-3 w-3" />
                              </Button>
                            </>
                          )}
                          {order.status === 'checked_in' && (
                            <Button size="sm" variant="outline" onClick={() => handleCheckOut(order.id!)}>
                              <LogOut className="h-3 w-3" />
                            </Button>
                          )}
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}

