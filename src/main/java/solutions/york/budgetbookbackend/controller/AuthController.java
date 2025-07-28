package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solutions.york.budgetbookbackend.dto.auth.LoginRequest;
import solutions.york.budgetbookbackend.dto.customer.CustomerResponse;
import solutions.york.budgetbookbackend.service.CustomerService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final CustomerService customerService;

    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerResponse> authenticateCustomer(@RequestBody LoginRequest request) {
        CustomerResponse customer = customerService.authenticateCustomer(request);
        return ResponseEntity.ok(customer);
    }
}
