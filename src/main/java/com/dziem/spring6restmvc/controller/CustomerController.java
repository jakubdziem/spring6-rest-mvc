package com.dziem.spring6restmvc.controller;

import com.dziem.spring6restmvc.model.Beer;
import com.dziem.spring6restmvc.model.Customer;
import com.dziem.spring6restmvc.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerPatchById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {

        customerService.patchCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteCustomerById(@PathVariable("customerId") UUID customerId) {

        customerService.deleteCustomerById(customerId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {

        customerService.updateCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @RequestMapping(value = CUSTOMER_PATH, method = RequestMethod.POST)
    public ResponseEntity handlePost(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "api/v1/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
    @RequestMapping(value = CUSTOMER_PATH,method = RequestMethod.GET)
    public List<Customer> listCustomers() {
        return customerService.listCustomers();
    }
    @RequestMapping(value = CUSTOMER_PATH_ID, method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId") UUID customerId) {
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

}
