package org.example.hotelmanagementsystem.controller;

import org.example.hotelmanagementsystem.entity.Customer;
import org.example.hotelmanagementsystem.dto.CustomerDto;
import org.example.hotelmanagementsystem.dto.CustomerQueryDto;
import org.example.hotelmanagementsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * 查询客户列表
     * @param query 查询条件
     * @return 客户列表
     */
    @PostMapping("/search")
    public List<Customer> findCustomers(@RequestBody CustomerQueryDto query) {
        return customerService.findCustomers(query);
    }
    
    /**
     * 根据ID获取客户详情
     * @param id 客户ID
     * @return 客户详情
     */
    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }
    
    /**
     * 根据身份证号获取客户详情
     * @param idCard 身份证号
     * @return 客户详情
     */
    @GetMapping("/id-card/{idCard}")
    public CustomerDto getCustomerByIdCard(@PathVariable String idCard) {
        return customerService.getCustomerByIdCard(idCard);
    }
    
    /**
     * 添加客户
     * @param customer 客户信息
     * @return 是否添加成功
     */
    @PostMapping
    public boolean addCustomer(@RequestBody Customer customer) {
        try {
            return customerService.addCustomer(customer);
        } catch (Exception e) {
            throw new RuntimeException("添加客户失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新客户信息
     * @param customer 客户信息
     * @return 是否更新成功
     */
    @PutMapping
    public boolean updateCustomer(@RequestBody Customer customer) {
        try {
            return customerService.updateCustomer(customer);
        } catch (Exception e) {
            throw new RuntimeException("更新客户失败: " + e.getMessage());
        }
    }
}