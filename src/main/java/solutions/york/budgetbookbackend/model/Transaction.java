package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Transaction {
    public enum Type {DEPOSIT, WITHDRAWAL}
    public enum RepeatUnit {DAY, WEEK, MONTH, YEAR}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Account account;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private RepeatUnit repeatUnit;

    @Column(nullable = true)
    private Integer repeatInterval;

    @Column(nullable = false)
    private Boolean archived;

    public Transaction() {}
    public Transaction(Customer customer, Account account, LocalDate date, String description, double amount, Type type, RepeatUnit repeatUnit, Integer repeatInterval) {
        this.customer = customer;
        this.account = account;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.repeatUnit = repeatUnit;
        this.repeatInterval = repeatInterval;
        this.archived = false;
    }
    public void update(LocalDate date, String description, double amount, Type type, Integer repeatInterval, RepeatUnit repeatUnit) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.repeatUnit = repeatUnit;
        this.repeatInterval = repeatInterval;
        this.archived = false;
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public Customer getCustomer() {
        return customer;
    }
    public Account getAccount() {
        return account;
    }
    public LocalDate getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public double getAmount() {
        return amount;
    }
    public Type getType() {
        return type;
    }
    public RepeatUnit getRepeatUnit() {
        return repeatUnit;
    }
    public Integer getRepeatInterval() {
        return repeatInterval;
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
    public void setAccount(Account account) {
        this.account = account;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public void setRepeatUnit(RepeatUnit repeatUnit) {
        this.repeatUnit = repeatUnit;
    }
    public void setRepeatInterval(Integer repeatInterval) {
        this.repeatInterval = repeatInterval;
    }
    public void setArchived(boolean archived) {
        this.archived = archived;
    }

}
