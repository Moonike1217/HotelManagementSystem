create database hotel_management_njupt;
use hotel_management_njupt;

-- 酒店表
CREATE TABLE hotels (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100) NOT NULL,
address VARCHAR(200),
phone VARCHAR(20),
star_level INT,
description TEXT,
status ENUM('active', 'inactive') DEFAULT 'active',
created_at BIGINT DEFAULT 0
);

-- 房间表
CREATE TABLE rooms (
id INT PRIMARY KEY AUTO_INCREMENT,
hotel_id INT NOT NULL,
room_type VARCHAR(50) NOT NULL,
room_number VARCHAR(20),
price DECIMAL(10,2) NOT NULL,
status ENUM('available', 'occupied', 'maintenance') DEFAULT 'available',
created_at BIGINT DEFAULT 0,
FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

-- 客户表
CREATE TABLE customers (
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100) NOT NULL,
phone VARCHAR(20),
email VARCHAR(100),
id_card VARCHAR(50),
created_at BIGINT DEFAULT 0
);

-- 订单表
CREATE TABLE orders (
id INT PRIMARY KEY AUTO_INCREMENT,
order_number VARCHAR(50) UNIQUE NOT NULL,
customer_id INT NOT NULL,
room_id INT NOT NULL,
check_in_date DATE NOT NULL,
check_out_date DATE NOT NULL,
total_amount DECIMAL(10,2) NOT NULL,
status ENUM('pending', 'confirmed', 'checked_in', 'checked_out', 'cancelled') DEFAULT 'pending',
created_at BIGINT DEFAULT 0,
FOREIGN KEY (customer_id) REFERENCES customers(id),
FOREIGN KEY (room_id) REFERENCES rooms(id)
);

-- 评价表
CREATE TABLE reviews (
id INT PRIMARY KEY AUTO_INCREMENT,
order_id INT NOT NULL,
customer_id INT NOT NULL,
hotel_id INT NOT NULL,
rating INT CHECK (rating >= 1 AND rating <= 5),
comment TEXT,
reply TEXT,
created_at BIGINT DEFAULT 0,
FOREIGN KEY (order_id) REFERENCES orders(id),
FOREIGN KEY (customer_id) REFERENCES customers(id),
FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

-- 用户表
CREATE TABLE users (
id INT PRIMARY KEY AUTO_INCREMENT,
username VARCHAR(50) UNIQUE NOT NULL,
password VARCHAR(100) NOT NULL,
role ENUM('admin', 'staff') DEFAULT 'staff',
created_at BIGINT DEFAULT 0
);