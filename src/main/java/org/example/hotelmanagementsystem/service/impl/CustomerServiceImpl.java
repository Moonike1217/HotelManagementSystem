package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.entity.Customer;
import org.example.hotelmanagementsystem.dto.CustomerDto;
import org.example.hotelmanagementsystem.dto.CustomerQueryDto;
import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.mapper.CustomerMapper;
import org.example.hotelmanagementsystem.service.CustomerService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    
    @Autowired
    private CustomerMapper customerMapper;
    
    @Override
    public List<Customer> findCustomers(CustomerQueryDto query) {
        logger.debug("查询客户列表，查询条件: {}", query);
        List<Customer> customers = customerMapper.findCustomers(query);
        logger.debug("查询到 {} 个客户", customers.size());
        return customers;
    }
    
    @Override
    public CustomerDto getCustomerById(Integer id) {
        logger.debug("根据ID查询客户详情，客户ID: {}", id);
        Customer customer = customerMapper.getCustomerById(id);
        if (customer == null) {
            logger.warn("未找到ID为 {} 的客户", id);
            return null;
        }
        
        // 获取客户的历史订单
        List<OrderDto> orderHistory = customerMapper.getCustomerOrderHistory(id);
        
        // 组装客户详情
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        customerDto.setOrderHistory(orderHistory);
        
        logger.debug("成功获取客户详情，客户ID: {}，历史订单数: {}", id, orderHistory.size());
        return customerDto;
    }
    
    @Override
    public CustomerDto getCustomerByIdCard(String idCard) {
        logger.debug("根据身份证号查询客户详情，身份证号: {}", idCard);
        Customer customer = customerMapper.getCustomerByIdCard(idCard);
        if (customer == null) {
            logger.warn("未找到身份证号为 {} 的客户", idCard);
            return null;
        }
        
        // 获取客户的历史订单
        List<OrderDto> orderHistory = customerMapper.getCustomerOrderHistory(customer.getId());
        
        // 组装客户详情
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        customerDto.setOrderHistory(orderHistory);
        
        logger.debug("成功获取客户详情，客户ID: {}，身份证号: {}，历史订单数: {}", customer.getId(), idCard, orderHistory.size());
        return customerDto;
    }
    
    @Override
    public boolean addCustomer(Customer customer) {
        logger.info("添加新客户，客户姓名: {}", customer.getName());
        
        // 检查身份证号是否已存在
        if (customer.getIdCard() != null && !customer.getIdCard().isEmpty()) {
            Customer existingCustomer = customerMapper.getCustomerByIdCard(customer.getIdCard());
            if (existingCustomer != null) {
                logger.warn("身份证号 {} 已存在", customer.getIdCard());
                throw new RuntimeException("身份证号已存在");
            }
        }
        
        // 设置创建时间
        if (customer.getCreatedAt() == null) {
            customer.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        }
        
        int result = customerMapper.insertCustomer(customer);
        boolean success = result > 0;
        if (success) {
            logger.info("客户添加成功，客户ID: {}", customer.getId());
        } else {
            logger.error("客户添加失败，客户信息: {}", customer);
        }
        return success;
    }
    
    @Override
    public boolean updateCustomer(Customer customer) {
        logger.info("更新客户信息，客户ID: {}", customer.getId());
        
        // 设置更新时间
        customer.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = customerMapper.updateCustomer(customer);
        boolean success = result > 0;
        if (success) {
            logger.info("客户信息更新成功，客户ID: {}", customer.getId());
        } else {
            logger.error("客户信息更新失败，客户ID: {}", customer.getId());
        }
        return success;
    }
}