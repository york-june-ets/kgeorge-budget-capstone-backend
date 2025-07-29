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

    public void validateAuthRequest(AuthRequest request) {
        if (request == null) {throw new IllegalArgumentException("Request cannot be null");}
        if (request.getEmail() == null) {throw new IllegalArgumentException("Email cannot be null");}
        if (request.getPassword() == null) {throw new IllegalArgumentException("Password cannot be null");}
    }

    public AuthResponse authenticateCustomer(@RequestBody AuthRequest request) {
        validateAuthRequest(request);
        Customer customer = customerService.findByEmail(request.getEmail());
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
