package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.york.budgetbookbackend.dto.customer.CustomerRequest;
import solutions.york.budgetbookbackend.dto.customer.CustomerResponse;
import solutions.york.budgetbookbackend.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest request) {
        CustomerResponse customerResponse = customerService.createCustomer(request);
        return ResponseEntity.ok(customerResponse);
    }

    @PutMapping
    public ResponseEntity<CustomerResponse> updateCustomer(@RequestHeader("Authorization") String token, @RequestBody CustomerRequest request) {
        CustomerResponse customerResponse = customerService.updateCustomer(token, request);
        return ResponseEntity.ok(customerResponse);
    }
}
