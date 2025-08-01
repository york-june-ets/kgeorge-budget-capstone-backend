package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

@Entity
public class Budget {
    public enum TimePeriod {MONTH, QUARTER, YEAR}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @OneToOne
    private Category category;

    @Column(nullable = false)
    private double budgetLimit;

    @Enumerated(EnumType.STRING)
    private TimePeriod timePeriod;

    @Column(nullable = false)
    private Boolean archived;

    public Budget() {}
    public Budget(Customer customer, Category category, double budgetLimit, TimePeriod timePeriod) {
        this.customer = customer;
        this.category = category;
        this.budgetLimit = budgetLimit;
        this.timePeriod = timePeriod;
        this.archived = false;
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public Customer getCustomer() {
        return customer;
    }
    public Category getCategory() {
        return category;
    }
    public double getBudgetLimit() {
        return budgetLimit;
    }
    public TimePeriod getTimePeriod() {
        return timePeriod;
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
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setBudgetLimit(double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }
    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}
