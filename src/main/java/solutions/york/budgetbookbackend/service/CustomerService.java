package solutions.york.budgetbookbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import solutions.york.budgetbookbackend.dto.customer.CustomerRequest;
import solutions.york.budgetbookbackend.dto.customer.CustomerResponse;
import solutions.york.budgetbookbackend.model.Customer;
import solutions.york.budgetbookbackend.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void validateCustomerRequest(CustomerRequest request) {
        if (request == null) {throw new IllegalArgumentException("Request cannot be null");}
        if (request.getFirstName() == null || request.getFirstName().isBlank()) {throw new IllegalArgumentException("First name cannot be null");}
        if (request.getLastName() == null || request.getLastName().isBlank()) {throw new IllegalArgumentException("Last name cannot be null");}
        if (request.getEmail() == null || request.getEmail().isBlank()) {throw new IllegalArgumentException("Email cannot be null");}
        if (request.getPassword() == null || request.getPassword().isBlank()) {throw new IllegalArgumentException("Password cannot be null");}
    }

    public CustomerResponse createCustomer(@RequestBody CustomerRequest request) {
        validateCustomerRequest(request);
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {throw new IllegalArgumentException("Email already exists");}
        Customer customer = customerRepository.save(new Customer(request));
        return new CustomerResponse(customer);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Email not found"));
    }

}
