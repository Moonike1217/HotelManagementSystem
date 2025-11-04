import { useEffect, useState } from 'react';
import { Plus, Search, Edit, Star, Trash2 } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Label } from '../components/ui/label';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../components/ui/table';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '../components/ui/dialog';
import { Badge } from '../components/ui/badge';
import { Textarea } from '../components/ui/textarea';
import { Select } from '../components/ui/select';
import { Toast } from '../components/ui/toast';
import { useToast } from '../hooks/useToast';
import { hotelApi } from '../api';
import type { Hotel, HotelDto } from '../types';

export function Hotels() {
  const [hotels, setHotels] = useState<Hotel[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchName, setSearchName] = useState('');
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingHotel, setEditingHotel] = useState<HotelDto | null>(null);
  const { toast, showToast, hideToast } = useToast();

  const [formData, setFormData] = useState<HotelDto>({
    name: '',
    address: '',
    phone: '',
    starLevel: 3,
    description: '',
    status: 'active',
    roomTypes: [],
  });

  const loadHotels = async () => {
    setLoading(true);
    try {
      const response = await hotelApi.getAllHotels();
      setHotels(response.data);
    } catch (error) {
      console.error('加载酒店列表失败:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHotels();
  }, []);

  const handleSearch = async () => {
    if (!searchName.trim()) {
      loadHotels();
      return;
    }
    setLoading(true);
    try {
      const response = await hotelApi.searchHotelsByName(searchName);
      setHotels(response.data);
    } catch (error) {
      console.error('搜索酒店失败:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAdd = () => {
    setEditingHotel(null);
    setFormData({
      name: '',
      address: '',
      phone: '',
      starLevel: 3,
      description: '',
      status: 'active',
      roomTypes: [{ roomType: '', roomNumber: '', price: 0 }],
    });
    setIsDialogOpen(true);
  };

  const handleEdit = async (hotel: Hotel) => {
    try {
      // 从后端获取完整的酒店信息（包括房间数据）
      const response = await hotelApi.getHotelById(hotel.id!);
      const hotelDetail = response.data;
      
      setEditingHotel(hotel);
      setFormData({
        id: hotelDetail.id,
        name: hotelDetail.name,
        address: hotelDetail.address,
        phone: hotelDetail.phone,
        starLevel: hotelDetail.starLevel,
        description: hotelDetail.description,
        status: hotelDetail.status,
        roomTypes: hotelDetail.roomTypes && hotelDetail.roomTypes.length > 0 
          ? hotelDetail.roomTypes 
          : [{ roomType: '', roomNumber: '', price: 0 }],
      });
      setIsDialogOpen(true);
    } catch (error) {
      console.error('获取酒店详情失败:', error);
      showToast('获取酒店信息失败，请稍后重试', 'error');
    }
  };

  const addRoomType = () => {
    setFormData({
      ...formData,
      roomTypes: [...(formData.roomTypes || []), { roomType: '', roomNumber: '', price: 0 }],
    });
  };

  const removeRoomType = (index: number) => {
    const newRoomTypes = formData.roomTypes?.filter((_, i) => i !== index) || [];
    setFormData({ ...formData, roomTypes: newRoomTypes });
  };

  const updateRoomType = (index: number, field: string, value: any) => {
    const newRoomTypes = [...(formData.roomTypes || [])];
    newRoomTypes[index] = { ...newRoomTypes[index], [field]: value };
    setFormData({ ...formData, roomTypes: newRoomTypes });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingHotel) {
        await hotelApi.updateHotel(formData);
        showToast('酒店信息更新成功！', 'success');
      } else {
        await hotelApi.addHotel(formData);
        showToast('酒店添加成功！', 'success');
      }
      setIsDialogOpen(false);
      loadHotels();
    } catch (error) {
      console.error('保存酒店失败:', error);
      showToast('操作失败，请稍后重试', 'error');
    }
  };

  return (
    <div className="space-y-6">
      {toast.show && (
        <Toast message={toast.message} type={toast.type} onClose={hideToast} />
      )}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">酒店信息管理</h1>
          <p className="text-muted-foreground">管理系统中的所有酒店信息</p>
        </div>
        <Button onClick={handleAdd}>
          <Plus className="mr-2 h-4 w-4" />
          添加酒店
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>搜索酒店</CardTitle>
          <CardDescription>根据酒店名称搜索</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex gap-2">
            <Input
              placeholder="输入酒店名称..."
              value={searchName}
              onChange={(e) => setSearchName(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
            />
            <Button onClick={handleSearch}>
              <Search className="mr-2 h-4 w-4" />
              搜索
            </Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>酒店列表</CardTitle>
          <CardDescription>共 {hotels.length} 家酒店</CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">加载中...</div>
          ) : hotels.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">暂无酒店数据</div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>酒店名称</TableHead>
                  <TableHead>地址</TableHead>
                  <TableHead>电话</TableHead>
                  <TableHead>星级</TableHead>
                  <TableHead>状态</TableHead>
                  <TableHead>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {hotels.map((hotel) => (
                  <TableRow key={hotel.id}>
                    <TableCell className="font-medium">{hotel.name}</TableCell>
                    <TableCell>{hotel.address}</TableCell>
                    <TableCell>{hotel.phone}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1">
                        {Array.from({ length: hotel.starLevel }).map((_, i) => (
                          <Star key={i} className="h-4 w-4 fill-yellow-400 text-yellow-400" />
                        ))}
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant={hotel.status === 'active' ? 'default' : 'secondary'}>
                        {hotel.status === 'active' ? '启用' : '禁用'}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm" onClick={() => handleEdit(hotel)}>
                        <Edit className="h-4 w-4" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent>
          <form onSubmit={handleSubmit}>
            <DialogHeader>
              <DialogTitle>{editingHotel ? '编辑酒店' : '添加酒店'}</DialogTitle>
              <DialogDescription>
                请填写酒店的基本信息
              </DialogDescription>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <div className="grid gap-2">
                <Label htmlFor="name">酒店名称 *</Label>
                <Input
                  id="name"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  required
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="address">地址 *</Label>
                <Input
                  id="address"
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  required
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="phone">电话 *</Label>
                <Input
                  id="phone"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  required
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="starLevel">星级 *</Label>
                <Select
                  id="starLevel"
                  value={formData.starLevel.toString()}
                  onChange={(e) => setFormData({ ...formData, starLevel: parseInt(e.target.value) })}
                  required
                >
                  <option value="1">1星</option>
                  <option value="2">2星</option>
                  <option value="3">3星</option>
                  <option value="4">4星</option>
                  <option value="5">5星</option>
                </Select>
              </div>
              <div className="grid gap-2">
                <Label htmlFor="description">描述</Label>
                <Textarea
                  id="description"
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  rows={3}
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="status">状态</Label>
                <Select
                  id="status"
                  value={formData.status}
                  onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                >
                  <option value="active">启用</option>
                  <option value="inactive">禁用</option>
                </Select>
              </div>

              <div className="border-t pt-4 mt-4">
                <div className="flex items-center justify-between mb-3">
                  <Label>房间信息 *</Label>
                  <Button type="button" size="sm" onClick={addRoomType}>
                    <Plus className="h-4 w-4 mr-1" />
                    添加房间
                  </Button>
                </div>
                {formData.roomTypes && formData.roomTypes.length > 0 ? (
                  <div className="space-y-3">
                    {formData.roomTypes.map((room, index) => (
                      <div key={index} className="grid gap-2 p-3 border rounded-md">
                        <div className="flex items-center justify-between mb-2">
                          <span className="text-sm font-medium">房间 {index + 1}</span>
                          {formData.roomTypes!.length > 1 && (
                            <Button
                              type="button"
                              variant="ghost"
                              size="sm"
                              onClick={() => removeRoomType(index)}
                            >
                              <Trash2 className="h-4 w-4 text-destructive" />
                            </Button>
                          )}
                        </div>
                        <div className="grid grid-cols-3 gap-2">
                          <div>
                            <Label htmlFor={`roomType-${index}`}>房型 *</Label>
                            <Input
                              id={`roomType-${index}`}
                              placeholder="如：标准间"
                              value={room.roomType}
                              onChange={(e) => updateRoomType(index, 'roomType', e.target.value)}
                              required
                            />
                          </div>
                          <div>
                            <Label htmlFor={`roomNumber-${index}`}>房间号 *</Label>
                            <Input
                              id={`roomNumber-${index}`}
                              placeholder="如：101"
                              value={room.roomNumber}
                              onChange={(e) => updateRoomType(index, 'roomNumber', e.target.value)}
                              required
                            />
                          </div>
                          <div>
                            <Label htmlFor={`price-${index}`}>价格 *</Label>
                            <Input
                              id={`price-${index}`}
                              type="number"
                              placeholder="0.00"
                              value={room.price}
                              onChange={(e) => updateRoomType(index, 'price', parseFloat(e.target.value) || 0)}
                              required
                              min="0"
                              step="0.01"
                            />
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <div className="text-sm text-muted-foreground text-center py-3">
                    点击"添加房间"按钮添加房间信息
                  </div>
                )}
              </div>
            </div>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setIsDialogOpen(false)}>
                取消
              </Button>
              <Button type="submit">
                {editingHotel ? '保存' : '添加'}
              </Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}

