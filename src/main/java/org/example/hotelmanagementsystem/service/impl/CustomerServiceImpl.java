package org.example.hotelmanagementsystem.service.impl;

import org.example.hotelmanagementsystem.entity.Customer;
import org.example.hotelmanagementsystem.dto.CustomerDto;
import org.example.hotelmanagementsystem.dto.CustomerQueryDto;
import org.example.hotelmanagementsystem.dto.OrderDto;
import org.example.hotelmanagementsystem.mapper.CustomerMapper;
import org.example.hotelmanagementsystem.service.CustomerService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    
    @Autowired
    private CustomerMapper customerMapper;
    
    @Override
    public List<Customer> findCustomers(CustomerQueryDto query) {
        return customerMapper.findCustomers(query);
    }
    
    @Override
    public CustomerDto getCustomerById(Integer id) {
        Customer customer = customerMapper.getCustomerById(id);
        if (customer == null) {
            return null;
        }
        
        // 获取客户的历史订单
        List<OrderDto> orderHistory = customerMapper.getCustomerOrderHistory(id);
        
        // 组装客户详情
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        customerDto.setOrderHistory(orderHistory);
        
        return customerDto;
    }
    
    @Override
    public CustomerDto getCustomerByIdCard(String idCard) {
        Customer customer = customerMapper.getCustomerByIdCard(idCard);
        if (customer == null) {
            return null;
        }
        
        // 获取客户的历史订单
        List<OrderDto> orderHistory = customerMapper.getCustomerOrderHistory(customer.getId());
        
        // 组装客户详情
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        customerDto.setOrderHistory(orderHistory);
        
        return customerDto;
    }
    
    @Override
    public boolean addCustomer(Customer customer) {
        // 检查身份证号是否已存在
        if (customer.getIdCard() != null && !customer.getIdCard().isEmpty()) {
            Customer existingCustomer = customerMapper.getCustomerByIdCard(customer.getIdCard());
            if (existingCustomer != null) {
                throw new RuntimeException("身份证号已存在");
            }
        }
        
        // 设置创建时间
        if (customer.getCreatedAt() == null) {
            customer.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        }
        
        int result = customerMapper.insertCustomer(customer);
        return result > 0;
    }
    
    @Override
    public boolean updateCustomer(Customer customer) {
        // 设置更新时间
        customer.setCreatedAt(TimestampUtil.getCurrentTimestamp());
        int result = customerMapper.updateCustomer(customer);
        return result > 0;
    }
}