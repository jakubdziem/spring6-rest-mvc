package com.dziem.spring6restmvc.service;

import com.dziem.spring6restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    Map<UUID,Customer> customersMap;
    public CustomerServiceImpl() {
        this.customersMap = new HashMap<>();
        Customer c1 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Wazowsky")
                .version(909)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        Customer c2 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Manowsu")
                .version(231)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        Customer c3 = Customer.builder()
                .id(UUID.randomUUID())
                .customerName("Pojan")
                .version(567)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customersMap.put(c1.getId(),c1);
        customersMap.put(c2.getId(),c2);
        customersMap.put(c3.getId(),c3);
    }
    @Override
    public List<Customer> listCustomers() {
        return new ArrayList<>(customersMap.values());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return customersMap.get(id);
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .customerName(customer.getCustomerName())
                .version(customer.getVersion())
                .build();
        customersMap.put(savedCustomer.getId(),savedCustomer);

        return savedCustomer;
    }

    @Override
    public void updateCustomerById(UUID customerId, Customer customer) {
        Customer existing = customersMap.get(customerId);
        existing.setCustomerName(customer.getCustomerName());
        existing.setVersion(customer.getVersion());
        existing.setLastModifiedDate(LocalDateTime.now());

        customersMap.put(existing.getId(),existing);
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        customersMap.remove(customerId);
    }

    @Override
    public void patchCustomerById(UUID customerId, Customer customer) {
        Customer existing = customersMap.get(customerId);
        if(StringUtils.hasText(customer.getCustomerName())) {
            existing.setCustomerName(customer.getCustomerName());
        }
        if(customer.getVersion()!=null) {
            existing.setVersion(customer.getVersion());
        }
    }


}
