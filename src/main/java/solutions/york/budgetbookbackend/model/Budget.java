package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

@Entity
public class Budget {
    public enum TimePeriod {MONTH, QUARTER, YEAR}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @OneToOne
    private Category category;

    @Column(nullable = false)
    private double spent;

    @Column(nullable = false)
    private double limit;

    @Enumerated(EnumType.STRING)
    private TimePeriod period;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Boolean archived;

    public Budget() {}
    public Budget(Account account, Category category, double limit, TimePeriod period) {
        this.account = account;
        this.category = category;
        this.spent = 0;
        this.limit = limit;
        this.period = period;
        this.enabled = true;
        this.archived = false;
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public Account getAccount() {
        return account;
    }
    public Category getCategory() {
        return category;
    }
    public double getSpent() {
        return spent;
    }
    public double getLimit() {
        return limit;
    }
    public TimePeriod getPeriod() {
        return period;
    }
    public Boolean getEnabled() {
        return enabled;
    }
    public Boolean getArchived() {
        return archived;
    }


    // SETTERS
    public void setId(Long id) {
        this.id = id;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setSpent(double spent) {
        this.spent = spent;
    }
    public void setLimit(double limit) {
        this.limit = limit;
    }
    public void setPeriod(TimePeriod period) {
        this.period = period;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }



}
