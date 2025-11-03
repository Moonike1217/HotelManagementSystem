package org.example.hotelmanagementsystem.service;

import org.example.hotelmanagementsystem.entity.Customer;
import org.example.hotelmanagementsystem.dto.CustomerDto;
import org.example.hotelmanagementsystem.dto.CustomerQueryDto;
import java.util.List;

public interface CustomerService {
    
    /**
     * 查询客户列表
     * @param query 查询条件
     * @return 客户列表
     */
    List<Customer> findCustomers(CustomerQueryDto query);
    
    /**
     * 根据ID获取客户详情
     * @param id 客户ID
     * @return 客户详情
     */
    CustomerDto getCustomerById(Integer id);
    
    /**
     * 根据身份证号获取客户详情
     * @param idCard 身份证号
     * @return 客户详情
     */
    CustomerDto getCustomerByIdCard(String idCard);
    
    /**
     * 添加客户
     * @param customer 客户信息
     * @return 是否添加成功
     */
    boolean addCustomer(Customer customer);
    
    /**
     * 更新客户信息
     * @param customer 客户信息
     * @return 是否更新成功
     */
    boolean updateCustomer(Customer customer);
}