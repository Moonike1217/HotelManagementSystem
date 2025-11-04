USE hotel_management_njupt;

-- 插入一些初始数据
INSERT INTO hotels (id, name, address, phone, star_level, description, status, created_at) VALUES
(1, '北京国际大酒店', '北京市朝阳区', '010-12345678', 5, '五星级豪华酒店', 'active', 1695000000),
(2, '上海商务酒店', '上海市浦东新区', '021-87654321', 4, '商务型酒店', 'active', 1695000000);

INSERT INTO rooms (id, hotel_id, room_type, room_number, price, status, created_at) VALUES
(1, 1, '豪华大床房', '801', 888.00, 'available', 1695000000),
(2, 1, '行政套房', '901', 1888.00, 'available', 1695000000),
(3, 2, '商务双床房', '501', 688.00, 'available', 1695000000);

INSERT INTO customers (id, name, phone, email, id_card, created_at) VALUES
(1, '张三', '13800138000', 'zhangsan@example.com', '110101199001011234', 1695000000),
(2, '李四', '13900139000', 'lisi@example.com', '110101199002021235', 1695000000);

INSERT INTO users (id, username, password, role, created_at) VALUES
(1, 'admin', 'admin', 'admin', 1695000000),
(2, 'staff', 'staff', 'staff', 1695000000);

-- 插入订单数据
INSERT INTO orders (id, order_number, customer_id, room_id, check_in_date, check_out_date, total_amount, status, created_at) VALUES 
(1, 'ORD20231001001', 1, 1, '2023-10-01', '2023-10-03', 1776.00, 'checked_out', 1695637800),
(2, 'ORD20231002001', 2, 2, '2023-10-02', '2023-10-05', 5664.00, 'checked_out', 1695737700),
(3, 'ORD20231005001', 1, 3, '2023-10-05', '2023-10-07', 1376.00, 'checked_out', 1695894300),
(4, 'ORD20231010001', 2, 1, '2023-10-10', '2023-10-12', 1776.00, 'checked_in', 1696155600),
(5, 'ORD20231015001', 1, 2, '2023-10-15', '2023-10-18', 5664.00, 'confirmed', 1696523400),
(6, 'ORD20231020001', 2, 3, '2023-10-20', '2023-10-23', 2064.00, 'pending', 1696743900),
(7, 'ORD20231025001', 1, 1, '2023-10-25', '2023-10-27', 1776.00, 'pending', 1696922400);

-- 插入评价数据
INSERT INTO reviews (id, order_id, customer_id, hotel_id, rating, comment, created_at) VALUES 
(1, 1, 1, 1, 5, '酒店环境很好，服务也很周到，下次还会选择这里。', 1696415400),
(2, 2, 2, 1, 4, '房间宽敞，设施齐全，位置便利。', 1696592700),
(3, 3, 1, 2, 5, '性价比很高，服务态度好，非常满意。', 1696774800);