package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

@Entity
public class Account {
    public enum Type {CHECKING, SAVINGS, CASH, OTHER}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private double balance;

    public Account() {}
    public Account(Customer customer, String name, Type type, double balance) {
        this.customer = customer;
        this.name = name;
        this.type = type;
        this.balance = balance;
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public Customer getCustomer() {
        return customer;
    }
    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }
    public double getBalance() {
        return balance;
    }

    // SETTERS
    public void setId(Long id) {
        this.id = id;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
