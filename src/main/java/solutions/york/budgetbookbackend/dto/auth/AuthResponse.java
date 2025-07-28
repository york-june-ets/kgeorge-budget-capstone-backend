package solutions.york.budgetbookbackend.dto.auth;

import solutions.york.budgetbookbackend.dto.customer.CustomerResponse;

public class AuthResponse {
    private String token;
    private CustomerResponse customer;

    public AuthResponse() {}
    public AuthResponse(String token, CustomerResponse customer) {
        this.token = token;
        this.customer = customer;
    }

    // GETTERS
    public String getToken() {
        return token;
    }
    public CustomerResponse getCustomer() {
        return customer;
    }
}
