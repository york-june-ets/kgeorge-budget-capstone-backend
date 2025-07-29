package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @OneToOne
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime expiredAt;

    public Auth() {}
    public Auth(String token, Customer customer) {
        this.token = token;
        this.customer = customer;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = null;
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getExpiredAt() {
        return expiredAt;
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
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
}
