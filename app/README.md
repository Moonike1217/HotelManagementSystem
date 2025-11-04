# 酒店管理系统 - 前端项目

基于 React + TypeScript + Vite + shadcn/ui 构建的酒店管理系统前端应用。

## 技术栈

- **React 19** - UI框架
- **TypeScript** - 类型安全
- **Vite** - 构建工具
- **React Router** - 路由管理
- **Axios** - HTTP请求
- **Recharts** - 图表可视化
- **Tailwind CSS** - 样式框架
- **shadcn/ui** - UI组件库
- **date-fns** - 日期处理
- **Lucide React** - 图标库

## 功能模块

1. **登录系统** - 模拟登录（任意用户名，密码：123456）
2. **酒店管理** - 增删改查酒店信息
3. **房间预订** - 查询可用房间并创建预订
4. **订单管理** - 订单查询、确认、取消、入住、退房
5. **客户管理** - 客户信息管理和历史记录查看
6. **评价管理** - 查看评价、管理员回复
7. **统计报表** - 预订统计、收入统计、入住率统计（含图表）

## 安装依赖

```bash
pnpm install
```

## 启动开发服务器

```bash
pnpm dev
```

应用将运行在 http://localhost:3000

## 构建生产版本

```bash
pnpm build
```

## 项目结构

```
app/
├── src/
│   ├── api/              # API接口层
│   │   ├── client.ts     # Axios配置
│   │   ├── hotel.ts      # 酒店API
│   │   ├── booking.ts    # 预订API
│   │   ├── order.ts      # 订单API
│   │   ├── customer.ts   # 客户API
│   │   ├── review.ts     # 评价API
│   │   └── report.ts     # 报表API
│   ├── components/       # 组件
│   │   ├── ui/          # UI基础组件
│   │   └── Layout.tsx   # 主布局
│   ├── pages/           # 页面组件
│   │   ├── Login.tsx
│   │   ├── Hotels.tsx
│   │   ├── Bookings.tsx
│   │   ├── Orders.tsx
│   │   ├── Customers.tsx
│   │   ├── Reviews.tsx
│   │   └── Reports.tsx
│   ├── types/           # TypeScript类型定义
│   ├── utils/           # 工具函数
│   ├── lib/             # 第三方库配置
│   ├── App.tsx          # 根组件
│   ├── main.tsx         # 入口文件
│   └── index.css        # 全局样式
├── public/              # 静态资源
├── index.html           # HTML模板
├── package.json         # 依赖配置
├── tsconfig.json        # TypeScript配置
├── vite.config.ts       # Vite配置
└── tailwind.config.js   # Tailwind配置
```

## 后端配置

后端API默认运行在 `http://localhost:8080`

如需修改，请编辑 `src/api/client.ts` 中的 `API_BASE_URL`

## 登录说明

- 用户名：任意
- 密码：123456（固定）

## 注意事项

1. 确保后端服务已启动并运行在 8080 端口
2. 首次使用时数据库可能为空，需要先添加酒店信息
3. 图片功能使用占位图片
4. 通知提醒为模拟功能，仅显示提示信息
