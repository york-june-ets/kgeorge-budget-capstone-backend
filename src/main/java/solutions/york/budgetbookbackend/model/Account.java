package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;
import solutions.york.budgetbookbackend.dto.account.AccountRequest;

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

    @Column(nullable = false)
    private Boolean archived;

    public Account() {}
    public Account(Customer customer, String name, Type type, double balance) {
        this.customer = customer;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.archived = false;
    }
    public void update(AccountRequest request) {
        this.name = request.getName();
        this.type = Type.valueOf(request.getType());
        this.balance = Double.parseDouble(request.getBalance());
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
    public Boolean getArchived() {
        return archived;
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
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}
