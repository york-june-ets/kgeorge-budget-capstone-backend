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

    @Column(nullable = false)
    private Type type;

    public Account() {}
    public Account(Customer customer, String name, Type type) {
        this.customer = customer;
        this.name = name;
        this.type = type;
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
}
