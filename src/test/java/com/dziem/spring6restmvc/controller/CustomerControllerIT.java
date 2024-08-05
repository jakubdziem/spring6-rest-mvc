package com.dziem.spring6restmvc.controller;

import com.dziem.spring6restmvc.entities.Customer;
import com.dziem.spring6restmvc.model.CustomerDTO;
import com.dziem.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CustomerControllerIT {
    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;
    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());

        assertThat(customerDTO).isNotNull();

    }
    @Test
    void testGetCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            CustomerDTO customerDTO = customerController.getCustomerById(UUID.randomUUID());
        });

    }
    @Test
    void testListCustomers() {
        List<CustomerDTO> all = customerController.listCustomers();

        assertThat(all.size()).isEqualTo(3);
    }
    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> all = customerController.listCustomers();

        assertThat(all.size()).isEqualTo(0);
    }
}
