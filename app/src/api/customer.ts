import { apiClient } from './client';
import type { Customer, CustomerDto, CustomerQueryDto } from '../types';

export const customerApi = {
  // 查询客户列表
  findCustomers: (query: CustomerQueryDto) => {
    return apiClient.post<Customer[]>('/customers/search', query);
  },

  // 根据ID获取客户详情
  getCustomerById: (id: number) => {
    return apiClient.get<CustomerDto>(`/customers/${id}`);
  },

  // 根据身份证号获取客户详情
  getCustomerByIdCard: (idCard: string) => {
    return apiClient.get<CustomerDto>(`/customers/id-card/${idCard}`);
  },

  // 添加客户
  addCustomer: (customer: Customer) => {
    return apiClient.post<boolean>('/customers', customer);
  },

  // 更新客户信息
  updateCustomer: (customer: Customer) => {
    return apiClient.put<boolean>('/customers', customer);
  },
};

