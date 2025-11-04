import { useEffect, useState } from 'react';
import { Plus, Search, Edit, Eye } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Label } from '../components/ui/label';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../components/ui/table';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '../components/ui/dialog';
import { Toast } from '../components/ui/toast';
import { useToast } from '../hooks/useToast';
import { customerApi } from '../api';
import type { Customer, CustomerDto, CustomerQueryDto } from '../types';
import { format } from 'date-fns';

export function Customers() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isDetailDialogOpen, setIsDetailDialogOpen] = useState(false);
  const [editingCustomer, setEditingCustomer] = useState<Customer | null>(null);
  const [customerDetail, setCustomerDetail] = useState<CustomerDto | null>(null);
  const { toast, showToast, hideToast } = useToast();

  const [searchQuery, setSearchQuery] = useState<CustomerQueryDto>({
    name: '',
    phone: '',
    idCard: '',
  });

  const [formData, setFormData] = useState<Customer>({
    name: '',
    phone: '',
    email: '',
    idCard: '',
  });

  const loadCustomers = async () => {
    setLoading(true);
    try {
      const response = await customerApi.findCustomers({});
      setCustomers(response.data);
    } catch (error) {
      console.error('加载客户列表失败:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCustomers();
  }, []);

  const handleSearch = async () => {
    setLoading(true);
    try {
      const response = await customerApi.findCustomers(searchQuery);
      setCustomers(response.data);
    } catch (error) {
      console.error('搜索客户失败:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setEditingCustomer(null);
    setFormData({
      name: '',
      phone: '',
      email: '',
      idCard: '',
    });
    setIsDialogOpen(true);
  };

  const handleEdit = (customer: Customer) => {
    setEditingCustomer(customer);
    setFormData(customer);
    setIsDialogOpen(true);
  };

  const handleViewDetail = async (customer: Customer) => {
    try {
      const response = await customerApi.getCustomerById(customer.id!);
      setCustomerDetail(response.data);
      setIsDetailDialogOpen(true);
    } catch (error) {
      console.error('获取客户详情失败:', error);
      alert('获取详情失败');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingCustomer) {
        await customerApi.updateCustomer(formData);
        showToast('客户信息更新成功', 'success');
      } else {
        await customerApi.addCustomer(formData);
        showToast('客户添加成功', 'success');
      }
      setIsDialogOpen(false);
      loadCustomers();
    } catch (error) {
      console.error('保存客户失败:', error);
      showToast('操作失败，请稍后重试', 'error');
    }
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
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">客户管理</h1>
          <p className="text-muted-foreground">管理客户信息和历史记录</p>
        </div>
        <Button onClick={handleAdd}>
          <Plus className="mr-2 h-4 w-4" />
          添加客户
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>搜索客户</CardTitle>
          <CardDescription>根据姓名、电话或身份证号搜索</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-3">
            <div className="space-y-2">
              <Label htmlFor="name">客户姓名</Label>
              <Input
                id="name"
                placeholder="输入客户姓名"
                value={searchQuery.name}
                onChange={(e) => setSearchQuery({ ...searchQuery, name: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="phone">联系电话</Label>
              <Input
                id="phone"
                placeholder="输入电话号码"
                value={searchQuery.phone}
                onChange={(e) => setSearchQuery({ ...searchQuery, phone: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="idCard">身份证号</Label>
              <Input
                id="idCard"
                placeholder="输入身份证号"
                value={searchQuery.idCard}
                onChange={(e) => setSearchQuery({ ...searchQuery, idCard: e.target.value })}
              />
            </div>
          </div>
          <div className="mt-4 flex gap-2">
            <Button onClick={handleSearch}>
              <Search className="mr-2 h-4 w-4" />
              搜索
            </Button>
            <Button variant="outline" onClick={loadCustomers}>
              显示全部
            </Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>客户列表</CardTitle>
          <CardDescription>共 {customers.length} 位客户</CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">加载中...</div>
          ) : customers.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">暂无客户数据</div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>姓名</TableHead>
                  <TableHead>联系电话</TableHead>
                  <TableHead>电子邮箱</TableHead>
                  <TableHead>身份证号</TableHead>
                  <TableHead>创建时间</TableHead>
                  <TableHead>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {customers.map((customer) => (
                  <TableRow key={customer.id}>
                    <TableCell className="font-medium">{customer.name}</TableCell>
                    <TableCell>{customer.phone}</TableCell>
                    <TableCell>{customer.email}</TableCell>
                    <TableCell>{customer.idCard}</TableCell>
                    <TableCell className="text-sm text-muted-foreground">
                      {formatTimestamp(customer.createdAt)}
                    </TableCell>
                    <TableCell>
                      <div className="flex gap-1">
                        <Button variant="ghost" size="sm" onClick={() => handleViewDetail(customer)}>
                          <Eye className="h-4 w-4" />
                        </Button>
                        <Button variant="ghost" size="sm" onClick={() => handleEdit(customer)}>
                          <Edit className="h-4 w-4" />
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      {/* 添加/编辑客户对话框 */}
      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent>
          <form onSubmit={handleSubmit}>
            <DialogHeader>
              <DialogTitle>{editingCustomer ? '编辑客户' : '添加客户'}</DialogTitle>
              <DialogDescription>
                请填写客户的基本信息
              </DialogDescription>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <div className="grid gap-2">
                <Label htmlFor="customerName">客户姓名 *</Label>
                <Input
                  id="customerName"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  required
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="customerPhone">联系电话 *</Label>
                <Input
                  id="customerPhone"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  required
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="customerEmail">电子邮箱 *</Label>
                <Input
                  id="customerEmail"
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  required
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="customerIdCard">身份证号 *</Label>
                <Input
                  id="customerIdCard"
                  value={formData.idCard}
                  onChange={(e) => setFormData({ ...formData, idCard: e.target.value })}
                  required
                />
              </div>
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
                取消
              </Button>
              <Button type="submit">
                {editingCustomer ? '保存' : '添加'}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>

      {/* 客户详情对话框 */}
      <Dialog open={isDetailDialogOpen} onOpenChange={setIsDetailDialogOpen}>
        <DialogContent className="max-w-3xl">
          <DialogHeader>
            <DialogTitle>客户详情</DialogTitle>
            <DialogDescription>
              查看客户的详细信息和历史订单
            </DialogDescription>
          </DialogHeader>
          {customerDetail && (
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <Label>姓名</Label>
                  <div className="mt-1 text-sm">{customerDetail.name}</div>
                </div>
                <div>
                  <Label>电话</Label>
                  <div className="mt-1 text-sm">{customerDetail.phone}</div>
                </div>
                <div>
                  <Label>邮箱</Label>
                  <div className="mt-1 text-sm">{customerDetail.email}</div>
                </div>
                <div>
                  <Label>身份证号</Label>
                  <div className="mt-1 text-sm">{customerDetail.idCard}</div>
                </div>
              </div>
              <div>
                <Label>历史订单</Label>
                <div className="mt-2">
                  {customerDetail.orders && customerDetail.orders.length > 0 ? (
                    <Table>
                      <TableHeader>
                        <TableRow>
                          <TableHead>订单号</TableHead>
                          <TableHead>酒店</TableHead>
                          <TableHead>入住日期</TableHead>
                          <TableHead>金额</TableHead>
                        </TableRow>
                      </TableHeader>
                      <TableBody>
                        {customerDetail.orders.map((order) => (
                          <TableRow key={order.id}>
                            <TableCell>{order.orderNumber}</TableCell>
                            <TableCell>{order.hotelName}</TableCell>
                            <TableCell>{order.checkInDate}</TableCell>
                            <TableCell>¥{order.totalAmount}</TableCell>
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  ) : (
                    <div className="text-sm text-muted-foreground">暂无历史订单</div>
                  )}
                </div>
              </div>
            </div>
          )}
          <DialogFooter>
            <Button onClick={() => setIsDetailDialogOpen(false)}>关闭</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}

