package org.example.hotelmanagementsystem.mapper;

import org.apache.ibatis.annotations.*;
import org.example.hotelmanagementsystem.entity.Customer;
import org.example.hotelmanagementsystem.dto.CustomerDto;
import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.dto.CustomerQueryDto;
import java.util.List;

@Mapper
public interface CustomerMapper {
    
    /**
     * 根据条件查询客户列表
     * @param query 查询条件
     * @return 客户列表
     */
    List<Customer> findCustomers(CustomerQueryDto query);
    
    /**
     * 根据ID查询客户详情
     * @param id 客户ID
     * @return 客户信息
     */
    Customer getCustomerById(Integer id);
    
    /**
     * 根据身份证号查询客户
     * @param idCard 身份证号
     * @return 客户信息
     */
    Customer getCustomerByIdCard(String idCard);
    
    /**
     * 根据客户ID查询历史订单
     * @param customerId 客户ID
     * @return 订单列表
     */
    List<OrderDto> getCustomerOrderHistory(Integer customerId);
    
    /**
     * 插入客户信息
     * @param customer 客户信息
     * @return 影响行数
     */
    int insertCustomer(Customer customer);
    
    /**
     * 更新客户信息
     * @param customer 客户信息
     * @return 影响行数
     */
    int updateCustomer(Customer customer);
}