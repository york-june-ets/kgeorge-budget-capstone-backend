package solutions.york.budgetbookbackend.service;

import org.springframework.stereotype.Service;
import solutions.york.budgetbookbackend.dto.customer.CustomerRequest;
import solutions.york.budgetbookbackend.dto.customer.CustomerResponse;
import solutions.york.budgetbookbackend.model.Auth;
import solutions.york.budgetbookbackend.model.Category;
import solutions.york.budgetbookbackend.model.Customer;
import solutions.york.budgetbookbackend.repository.AuthRepository;
import solutions.york.budgetbookbackend.repository.CategoryRepository;
import solutions.york.budgetbookbackend.repository.CustomerRepository;

import java.time.LocalDateTime;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CategoryRepository categoryRepository;
    private final AuthRepository authRepository;

    public CustomerService(CustomerRepository customerRepository, CategoryRepository categoryRepository, AuthRepository authRepository) {
        this.customerRepository = customerRepository;
        this.categoryRepository = categoryRepository;
        this.authRepository = authRepository;
    }

    public void validateCustomerRequest(CustomerRequest request) {
        if (request == null) {throw new IllegalArgumentException("Request cannot be null");}
        if (request.getFirstName() == null || request.getFirstName().isBlank()) {throw new IllegalArgumentException("First name cannot be null");}
        if (request.getLastName() == null || request.getLastName().isBlank()) {throw new IllegalArgumentException("Last name cannot be null");}
        if (request.getEmail() == null || request.getEmail().isBlank()) {throw new IllegalArgumentException("Email cannot be null");}
        if (request.getPassword() == null || request.getPassword().isBlank()) {throw new IllegalArgumentException("Password cannot be null");}
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        validateCustomerRequest(request);
        Customer existingCustomer = findByEmail(request.getEmail());
        if (existingCustomer != null) {
            if (existingCustomer.getArchived() == true) {
                existingCustomer.update(request);
                customerRepository.save(existingCustomer);
                return new CustomerResponse(existingCustomer);
            } else {
                throw new IllegalArgumentException("Email is already in use");
            }
        }
        Customer customer = customerRepository.save(new Customer(request));
        createDefaultCategories(customer);
        return new CustomerResponse(customer);
    }

    public Auth validateToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Auth auth = authRepository.findByToken(token);
        if (auth != null) {
            if (auth.getExpiredAt() != null && auth.getExpiredAt().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Session has expired");
            }
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
        return auth;
    }

    public CustomerResponse updateCustomer(String token, CustomerRequest request) {
        Auth auth = validateToken(token);
        Customer customer = customerRepository.findById(auth.getCustomer().getId()).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.update(request);
        customerRepository.save(customer);
        return new CustomerResponse(customer);
    }

    public void createDefaultCategories(Customer customer) {
        categoryRepository.save(new Category(customer, "Housing"));
        categoryRepository.save(new Category(customer, "Utilities & Bills"));
        categoryRepository.save(new Category(customer, "Groceries"));
        categoryRepository.save(new Category(customer, "Dining & Takeout"));
        categoryRepository.save(new Category(customer, "Clothing & Accessories"));
        categoryRepository.save(new Category(customer, "Beauty & Personal Care"));
        categoryRepository.save(new Category(customer, "Health & Insurance"));
        categoryRepository.save(new Category(customer, "Education"));
        categoryRepository.save(new Category(customer, "Automotive & Transportation"));
        categoryRepository.save(new Category(customer, "Home & Garden"));
        categoryRepository.save(new Category(customer, "Computers & Electronics"));
        categoryRepository.save(new Category(customer, "Sports & Outdoors"));
        categoryRepository.save(new Category(customer, "Toys & Games"));
        categoryRepository.save(new Category(customer, "Baby & Kids"));
        categoryRepository.save(new Category(customer, "Pet Care"));
        categoryRepository.save(new Category(customer, "Entertainment & Activities"));
        categoryRepository.save(new Category(customer, "Travel"));
        categoryRepository.save(new Category(customer, "Financial"));
        categoryRepository.save(new Category(customer, "Work Expenses"));
        categoryRepository.save(new Category(customer, "Donations"));
        categoryRepository.save(new Category(customer, "Miscellaneous"));
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

}
