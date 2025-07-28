package solutions.york.budgetbookbackend.dto.customer;

public class CustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    // GETTERS
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPassword() {
        return password;
    }
}
