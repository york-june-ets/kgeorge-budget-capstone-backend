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

    public Allocation() {}
    public Allocation(Transaction transaction, Category category) {
        this.transaction = transaction;
        this.category = category;
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
}
