import { Link, Outlet, useLocation } from 'react-router-dom';
import { 
  Hotel, 
  CalendarCheck, 
  FileText, 
  Users, 
  Star, 
  BarChart3,
  LogOut
} from 'lucide-react';
import { cn } from '../lib/utils';

const menuItems = [
  { path: '/hotels', label: '酒店管理', icon: Hotel },
  { path: '/bookings', label: '房间预订', icon: CalendarCheck },
  { path: '/orders', label: '订单管理', icon: FileText },
  { path: '/customers', label: '客户管理', icon: Users },
  { path: '/reviews', label: '评价管理', icon: Star },
  { path: '/reports', label: '统计报表', icon: BarChart3 },
];

export function Layout() {
  const location = useLocation();

  const handleLogout = () => {
    localStorage.removeItem('isLoggedIn');
    window.location.href = '/login';
  };

  return (
    <div className="flex h-screen bg-background">
      {/* 侧边栏 */}
      <aside className="w-64 border-r bg-card">
        <div className="flex h-16 items-center border-b px-6">
          <h1 className="text-xl font-bold">酒店管理系统</h1>
        </div>
        <nav className="flex-1 space-y-1 p-4">
          {menuItems.map((item) => {
            const Icon = item.icon;
            const isActive = location.pathname.startsWith(item.path);
            return (
              <Link
                key={item.path}
                to={item.path}
                className={cn(
                  "flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors",
                  isActive
                    ? "bg-primary text-primary-foreground"
                    : "text-muted-foreground hover:bg-accent hover:text-accent-foreground"
                )}
              >
                <Icon className="h-5 w-5" />
                {item.label}
              </Link>
            );
          })}
        </nav>
        <div className="border-t p-4">
          <button
            onClick={handleLogout}
            className="flex w-full items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium text-muted-foreground transition-colors hover:bg-accent hover:text-accent-foreground"
          >
            <LogOut className="h-5 w-5" />
            退出登录
          </button>
        </div>
      </aside>

      {/* 主内容区 */}
      <main className="flex-1 overflow-auto">
        <div className="container mx-auto p-6">
          <Outlet />
        </div>
      </main>
    </div>
  );
}

