package com.dziem.spring6restmvc.controller;

import com.dziem.spring6restmvc.entities.Customer;
import com.dziem.spring6restmvc.mappers.CustomerMapper;
import com.dziem.spring6restmvc.model.CustomerDTO;
import com.dziem.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    CustomerMapper customerMapper;
    @Test
    void deleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.deleteCustomerById(UUID.randomUUID()));
    }
    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        Customer customer = customerRepository.findAll().get(0);
        long countBefore = customerRepository.count();

        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());

        long countAfter = customerRepository.count();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(countBefore).isNotEqualTo(countAfter);
    }

    @Rollback
    @Transactional
    @Test
    void testPatchCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        final String customerName = "PATCHED";
        customerDTO.setCustomerName(customerName);

        long countBefore = customerRepository.count();
        ResponseEntity responseEntity = customerController.updateCustomerPatchById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        long countAfter = customerRepository.count();
        Customer patchedCustomer = customerRepository.getReferenceById(customer.getId());
        assertThat(patchedCustomer.getCustomerName()).isEqualTo(customerName);
        assertThat(patchedCustomer.getVersion()).isEqualTo(customer.getVersion());
        assertThat(countBefore).isEqualTo(countAfter);

    }
    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class,  () -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }
    @Rollback
    @Transactional
    @Test
    void testUpdateCustomer() {
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        final String customerName = "UPDATED";
        customerDTO.setCustomerName(customerName);
        ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer updatedCustomer = customerRepository.getReferenceById(customer.getId());
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
    }
    @Rollback
    @Transactional
    @Test
    void testSaveCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerName("New Customer")
                .build();
        ResponseEntity responseEntity = customerController.handlePost(customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] locationUUID = responseEntity.getHeaders().getLocation().toString().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[3]);

        Customer customer = customerRepository.findById(savedUUID).get();
        assertThat(customer).isNotNull();
    }
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
