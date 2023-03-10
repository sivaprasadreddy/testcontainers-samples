package com.sivalabs.tcdemo.rest;

import com.sivalabs.tcdemo.domain.Customer;
import com.sivalabs.tcdemo.domain.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/api/customers")
	public List<Customer> getAllCustomers() {
		return customerService.getAllCustomers();
	}

}
