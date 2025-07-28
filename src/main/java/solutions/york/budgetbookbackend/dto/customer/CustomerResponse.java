package solutions.york.budgetbookbackend.dto.customer;

import solutions.york.budgetbookbackend.model.Customer;

public class CustomerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public CustomerResponse() {}
    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
    }

    // SETTERS
    public void setId(Long id) {
        this.id = id;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
