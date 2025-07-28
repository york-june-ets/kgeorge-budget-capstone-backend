package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.york.budgetbookbackend.dto.auth.AuthRequest;
import solutions.york.budgetbookbackend.dto.auth.AuthResponse;
import solutions.york.budgetbookbackend.dto.customer.CustomerResponse;
import solutions.york.budgetbookbackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateCustomer(@RequestBody AuthRequest request) {
        AuthResponse authData = authService.authenticateCustomer(request);
        return ResponseEntity.ok(authData);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> endSession(@RequestHeader("Authorization") String token) {
        authService.endSession(token);
        return ResponseEntity.ok("Session ended");
    }
}
