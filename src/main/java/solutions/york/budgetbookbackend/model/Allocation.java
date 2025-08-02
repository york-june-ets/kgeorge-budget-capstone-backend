package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

@Entity
public class Allocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Transaction transaction;

    @ManyToOne
    private Category category;

    @Column(nullable = false)
    private double amount;

    public Allocation() {}
    public Allocation(Transaction transaction, Category category, double amount) {
        this.transaction = transaction;
        this.category = category;
        this.amount = amount;
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public Transaction getTransaction() {
        return transaction;
    }
    public Category getCategory() {
        return category;
    }
    public double getAmount() {
        return amount;
    }

    // SETTERS
    public void setId(Long id) {
        this.id = id;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
