package solutions.york.budgetbookbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import solutions.york.budgetbookbackend.dto.auth.AuthRequest;
import solutions.york.budgetbookbackend.dto.auth.AuthResponse;
import solutions.york.budgetbookbackend.dto.customer.CustomerResponse;
import solutions.york.budgetbookbackend.model.Auth;
import solutions.york.budgetbookbackend.model.Customer;
import solutions.york.budgetbookbackend.repository.AuthRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final CustomerService customerService;

    public AuthService(AuthRepository authRepository, CustomerService customerService) {
        this.authRepository = authRepository;
        this.customerService = customerService;
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

    public void validateAuthRequest(AuthRequest request) {
        if (request == null) {throw new IllegalArgumentException("Request cannot be null");}
        if (request.getEmail() == null || request.getEmail().isBlank()) {throw new IllegalArgumentException("Email cannot be null");}
        if (request.getPassword() == null || request.getPassword().isBlank()) {throw new IllegalArgumentException("Password cannot be null");}
    }
    public void validateCustomer(Customer customer) {
        if (customer == null) {throw new IllegalArgumentException("Customer cannot be null");}
        if (customer.getArchived() == true) {throw new IllegalArgumentException("Customer is archived");}
    }

    public AuthResponse authenticateCustomer(@RequestBody AuthRequest request) {
        validateAuthRequest(request);
        Customer customer = customerService.findByEmail(request.getEmail());
        validateCustomer(customer);
        if (!customer.getPassword().equals(request.getPassword())) {throw new IllegalArgumentException("Invalid credentials");}
        Auth auth = new Auth(UUID.randomUUID().toString(), customer);
        authRepository.save(auth);
        return new AuthResponse(auth.getToken(), new CustomerResponse(customer));
    }

    public void endSession(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Auth auth = authRepository.findByToken(token);
        if (auth != null) {
            auth.setExpiredAt(LocalDateTime.now());
            authRepository.save(auth);
        }
    }
}
