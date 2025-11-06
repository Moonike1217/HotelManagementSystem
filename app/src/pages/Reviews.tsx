import { useEffect, useState } from 'react';
import { Search, Star, MessageSquare, Trash2 } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Label } from '../components/ui/label';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../components/ui/table';
import { Dialog, DialogContent, DialogDescription, DialogBody, DialogFooter, DialogHeader, DialogTitle } from '../components/ui/dialog';
import { Badge } from '../components/ui/badge';
import { Textarea } from '../components/ui/textarea';
import { Select } from '../components/ui/select';
import { Toast } from '../components/ui/toast';
import { useToast } from '../hooks/useToast';
import { reviewApi } from '../api';
import type { ReviewDto, ReviewQueryDto, ReviewReplyDto } from '../types';
import { format } from 'date-fns';

export function Reviews() {
  const [reviews, setReviews] = useState<ReviewDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [isReplyDialogOpen, setIsReplyDialogOpen] = useState(false);
  const [selectedReview, setSelectedReview] = useState<ReviewDto | null>(null);
  const [replyText, setReplyText] = useState('');
  const { toast, showToast, hideToast } = useToast();

  const [searchQuery, setSearchQuery] = useState<ReviewQueryDto>({
    rating: undefined,
    hasReply: undefined,
  });

  const loadReviews = async () => {
    setLoading(true);
    try {
      const response = await reviewApi.findReviews({});
      setReviews(response.data);
    } catch (error) {
      console.error('加载评价列表失败:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadReviews();
  }, []);

  const handleSearch = async () => {
    setLoading(true);
    try {
      const response = await reviewApi.findReviews(searchQuery);
      setReviews(response.data);
    } catch (error) {
      console.error('搜索评价失败:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleReply = (review: ReviewDto) => {
    setSelectedReview(review);
    setReplyText(review.reply || '');
    setIsReplyDialogOpen(true);
  };

  const handleSubmitReply = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedReview) return;

    try {
      const replyData: ReviewReplyDto = {
        id: selectedReview.id!,
        reply: replyText,
      };
      await reviewApi.replyToReview(replyData);
      showToast('回复成功', 'success');
      setIsReplyDialogOpen(false);
      loadReviews();
    } catch (error) {
      console.error('回复评价失败:', error);
      showToast('回复失败，请稍后重试', 'error');
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm('确定要删除这条评价吗？')) return;
    try {
      await reviewApi.deleteReview(id);
      showToast('删除成功', 'success');
      loadReviews();
    } catch (error) {
      console.error('删除评价失败:', error);
      showToast('删除失败，请稍后重试', 'error');
    }
  };

  const formatTimestamp = (timestamp?: number) => {
    if (!timestamp) return '-';
    return format(new Date(timestamp * 1000), 'yyyy-MM-dd HH:mm');
  };

  const renderStars = (rating: number) => {
    return (
      <div className="flex items-center gap-1">
        {Array.from({ length: 5 }).map((_, i) => (
          <Star
            key={i}
            className={`h-4 w-4 ${
              i < rating ? 'fill-yellow-400 text-yellow-400' : 'text-gray-300'
            }`}
          />
        ))}
        <span className="ml-1 text-sm font-medium">{rating}</span>
      </div>
    );
  };

  return (
    <div className="space-y-6">
      {toast.show && (
        <Toast message={toast.message} type={toast.type} onClose={hideToast} />
      )}
      <div>
        <h1 className="text-3xl font-bold">评价管理</h1>
        <p className="text-muted-foreground">查看和回复客户评价</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>筛选评价</CardTitle>
          <CardDescription>根据评分或回复状态筛选</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-2">
            <div className="space-y-2">
              <Label htmlFor="rating">评分</Label>
              <Select
                id="rating"
                value={searchQuery.rating?.toString() || ''}
                onChange={(e) =>
                  setSearchQuery({
                    ...searchQuery,
                    rating: e.target.value ? parseInt(e.target.value) : undefined,
                  })
                }
              >
                <option value="">全部评分</option>
                <option value="5">5星</option>
                <option value="4">4星</option>
                <option value="3">3星</option>
                <option value="2">2星</option>
                <option value="1">1星</option>
              </Select>
            </div>
            <div className="space-y-2">
              <Label htmlFor="hasReply">回复状态</Label>
              <Select
                id="hasReply"
                value={searchQuery.hasReply === undefined ? '' : searchQuery.hasReply.toString()}
                onChange={(e) =>
                  setSearchQuery({
                    ...searchQuery,
                    hasReply: e.target.value ? e.target.value === 'true' : undefined,
                  })
                }
              >
                <option value="">全部</option>
                <option value="true">已回复</option>
                <option value="false">未回复</option>
              </Select>
            </div>
          </div>
          <div className="mt-4 flex gap-2">
            <Button onClick={handleSearch}>
              <Search className="mr-2 h-4 w-4" />
              搜索
            </Button>
            <Button variant="outline" onClick={loadReviews}>
              显示全部
            </Button>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>评价列表</CardTitle>
          <CardDescription>共 {reviews.length} 条评价</CardDescription>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="text-center py-8">加载中...</div>
          ) : reviews.length === 0 ? (
            <div className="text-center py-8 text-muted-foreground">暂无评价数据</div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>订单号</TableHead>
                  <TableHead>客户</TableHead>
                  <TableHead>酒店</TableHead>
                  <TableHead>评分</TableHead>
                  <TableHead>评价内容</TableHead>
                  <TableHead>回复</TableHead>
                  <TableHead>评价时间</TableHead>
                  <TableHead>操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {reviews.map((review) => (
                  <TableRow key={review.id}>
                    <TableCell className="font-medium">{review.orderNumber}</TableCell>
                    <TableCell>{review.customerName}</TableCell>
                    <TableCell>{review.hotelName}</TableCell>
                    <TableCell>{renderStars(review.rating)}</TableCell>
                    <TableCell>
                      <div className="max-w-xs truncate">{review.comment}</div>
                    </TableCell>
                    <TableCell>
                      {review.reply ? (
                        <Badge variant="default">已回复</Badge>
                      ) : (
                        <Badge variant="secondary">未回复</Badge>
                      )}
                    </TableCell>
                    <TableCell className="text-sm text-muted-foreground">
                      {formatTimestamp(review.createdAt)}
                    </TableCell>
                    <TableCell>
                      <div className="flex gap-1">
                        <Button variant="ghost" size="sm" onClick={() => handleReply(review)}>
                          <MessageSquare className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleDelete(review.id!)}
                        >
                          <Trash2 className="h-4 w-4" />
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

      <Dialog open={isReplyDialogOpen} onOpenChange={setIsReplyDialogOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>回复评价</DialogTitle>
            <DialogDescription>
              {selectedReview && `${selectedReview.customerName} 的评价`}
            </DialogDescription>
          </DialogHeader>
          <form onSubmit={handleSubmitReply} className="flex flex-col flex-1 min-h-0">
            <DialogBody>
              {selectedReview && (
              <div className="space-y-4">
                <div className="rounded-lg bg-muted p-4">
                  <div className="mb-2">{renderStars(selectedReview.rating)}</div>
                  <p className="text-sm">{selectedReview.comment}</p>
                  <p className="mt-2 text-xs text-muted-foreground">
                    {formatTimestamp(selectedReview.createdAt)}
                  </p>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="reply">管理员回复</Label>
                  <Textarea
                    id="reply"
                    value={replyText}
                    onChange={(e) => setReplyText(e.target.value)}
                    placeholder="请输入回复内容..."
                    rows={5}
                    required
                  />
                </div>
              </div>
              )}
            </DialogBody>
            <DialogFooter>
              <Button type="button" variant="outline" onClick={() => setIsReplyDialogOpen(false)}>
                取消
              </Button>
              <Button type="submit">提交回复</Button>
            </DialogFooter>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
}

