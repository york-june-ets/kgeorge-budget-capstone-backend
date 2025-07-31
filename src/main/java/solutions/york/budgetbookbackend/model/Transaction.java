package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

@Entity
public class Transaction {
    public enum Type {DEPOSIT, WITHDRAWAL}
    public enum RepeatUnit {DAY, WEEK, MONTH, YEAR}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

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


}
