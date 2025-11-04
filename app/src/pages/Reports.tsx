import { useState } from 'react';
import { Search, Download, TrendingUp, DollarSign, Percent } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Label } from '../components/ui/label';
import { reportApi } from '../api';
import type { BookingStatisticsDto, RevenueStatisticsDto, OccupancyRateDto, ReportQueryDto } from '../types';
import { BarChart, Bar, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

export function Reports() {
  const [loading, setLoading] = useState(false);
  const [bookingStats, setBookingStats] = useState<BookingStatisticsDto[]>([]);
  const [revenueStats, setRevenueStats] = useState<RevenueStatisticsDto[]>([]);
  const [occupancyStats, setOccupancyStats] = useState<OccupancyRateDto[]>([]);

  const [queryParams, setQueryParams] = useState<ReportQueryDto>({
    startDate: '',
    endDate: '',
    hotelId: undefined,
  });

  const handleQuery = async () => {
    if (!queryParams.startDate || !queryParams.endDate) {
      alert('请选择开始和结束日期');
      return;
    }
    setLoading(true);
    try {
      const [bookingRes, revenueRes, occupancyRes] = await Promise.all([
        reportApi.getBookingStatistics(queryParams),
        reportApi.getRevenueStatistics(queryParams),
        reportApi.getOccupancyRateStatistics(queryParams),
      ]);
      setBookingStats(bookingRes.data);
      setRevenueStats(revenueRes.data);
      setOccupancyStats(occupancyRes.data);
    } catch (error) {
      console.error('获取统计数据失败:', error);
      alert('获取数据失败，请稍后重试');
    } finally {
      setLoading(false);
    }
  };

  const handleExportBooking = async () => {
    try {
      const response = await reportApi.exportBookingStatistics(queryParams);
      const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `预订统计_${new Date().getTime()}.xlsx`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('导出失败:', error);
      alert('导出失败');
    }
  };

  const handleExportRevenue = async () => {
    try {
      const response = await reportApi.exportRevenueStatistics(queryParams);
      const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `收入统计_${new Date().getTime()}.xlsx`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('导出失败:', error);
      alert('导出失败');
    }
  };

  const handleExportOccupancy = async () => {
    try {
      const response = await reportApi.exportOccupancyRateStatistics(queryParams);
      const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `入住率统计_${new Date().getTime()}.xlsx`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('导出失败:', error);
      alert('导出失败');
    }
  };

  // 计算汇总数据
  const totalBookings = bookingStats.reduce((sum, item) => sum + item.bookingCount, 0);
  const totalRevenue = revenueStats.reduce((sum, item) => sum + item.revenue, 0);
  const avgOccupancy = occupancyStats.length > 0
    ? (occupancyStats.reduce((sum, item) => sum + item.occupancyRate, 0) / occupancyStats.length).toFixed(2)
    : '0.00';

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold">统计报表</h1>
        <p className="text-muted-foreground">查看和导出各类统计数据</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>查询条件</CardTitle>
          <CardDescription>选择日期范围查询统计数据</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-3">
            <div className="space-y-2">
              <Label htmlFor="startDate">开始日期 *</Label>
              <Input
                id="startDate"
                type="date"
                value={queryParams.startDate}
                onChange={(e) => setQueryParams({ ...queryParams, startDate: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="endDate">结束日期 *</Label>
              <Input
                id="endDate"
                type="date"
                value={queryParams.endDate}
                onChange={(e) => setQueryParams({ ...queryParams, endDate: e.target.value })}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="hotelId">酒店ID（可选）</Label>
              <Input
                id="hotelId"
                type="number"
                placeholder="留空查询所有酒店"
                value={queryParams.hotelId || ''}
                onChange={(e) => setQueryParams({ ...queryParams, hotelId: e.target.value ? parseInt(e.target.value) : undefined })}
              />
            </div>
          </div>
          <div className="mt-4">
            <Button onClick={handleQuery} disabled={loading}>
              <Search className="mr-2 h-4 w-4" />
              {loading ? '查询中...' : '查询统计'}
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* 汇总数据卡片 */}
      {(bookingStats.length > 0 || revenueStats.length > 0 || occupancyStats.length > 0) && (
        <div className="grid gap-4 md:grid-cols-3">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">总预订量</CardTitle>
              <TrendingUp className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{totalBookings}</div>
              <p className="text-xs text-muted-foreground">
                在所选时间范围内
              </p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">总收入</CardTitle>
              <DollarSign className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">¥{totalRevenue.toFixed(2)}</div>
              <p className="text-xs text-muted-foreground">
                在所选时间范围内
              </p>
            </CardContent>
          </Card>
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">平均入住率</CardTitle>
              <Percent className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{avgOccupancy}%</div>
              <p className="text-xs text-muted-foreground">
                在所选时间范围内
              </p>
            </CardContent>
          </Card>
        </div>
      )}

      {/* 预订统计图表 */}
      {bookingStats.length > 0 && (
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>预订统计</CardTitle>
                <CardDescription>每日预订量和收入趋势</CardDescription>
              </div>
              <Button variant="outline" size="sm" onClick={handleExportBooking}>
                <Download className="mr-2 h-4 w-4" />
                导出Excel
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={bookingStats}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="bookingCount" fill="#3b82f6" name="预订量" />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      )}

      {/* 收入统计图表 */}
      {revenueStats.length > 0 && (
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>收入统计</CardTitle>
                <CardDescription>每日收入趋势</CardDescription>
              </div>
              <Button variant="outline" size="sm" onClick={handleExportRevenue}>
                <Download className="mr-2 h-4 w-4" />
                导出Excel
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={revenueStats}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="revenue" stroke="#10b981" strokeWidth={2} name="收入 (¥)" />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      )}

      {/* 入住率统计图表 */}
      {occupancyStats.length > 0 && (
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>入住率统计</CardTitle>
                <CardDescription>每日入住率变化</CardDescription>
              </div>
              <Button variant="outline" size="sm" onClick={handleExportOccupancy}>
                <Download className="mr-2 h-4 w-4" />
                导出Excel
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={occupancyStats}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="occupancyRate" stroke="#f59e0b" strokeWidth={2} name="入住率 (%)" />
              </LineChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      )}

      {!loading && bookingStats.length === 0 && revenueStats.length === 0 && occupancyStats.length === 0 && (
        <Card>
          <CardContent className="py-12">
            <div className="text-center text-muted-foreground">
              请先设置查询条件并查询统计数据
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
}

