package solutions.york.budgetbookbackend.model;

import jakarta.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private Boolean archived;

    public Category() {}
    public Category(Customer customer, String name) {
        this.customer = customer;
        this.name = name;
        this.archived = false;
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
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }
}
