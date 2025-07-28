package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

@Entity
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @OneToOne
    private Customer customer;

    public Auth() {}
    public Auth(String token, Customer customer) {
        this.token = token;
        this.customer = customer;
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public String getToken() {
        return token;
    }
    public Customer getCustomer() {
        return customer;
    }

    // SETTERS
    public void setId(Long id) {
        this.id = id;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
