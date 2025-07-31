package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private Boolean archived;

    public Category() {}
    public Category(String name) {
        this.name = name;
        this.archived = false;
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public Account getAccount() {
        return account;
    }
    public String getName() {
        return name;
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
    public void setName(String name) {
        this.name = name;
    }
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}
