package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
