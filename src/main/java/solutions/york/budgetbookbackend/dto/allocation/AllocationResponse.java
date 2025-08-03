package solutions.york.budgetbookbackend.dto.allocation;

import solutions.york.budgetbookbackend.model.Allocation;

public class AllocationResponse {
    private Long id;
    private String category;
    private double amount;

    public AllocationResponse() {}
    public AllocationResponse(Allocation allocation) {
        this.id = allocation.getId();
        this.category = allocation.getCategory().getName();
        this.amount = allocation.getAmount();
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }
    public double getAmount() {
        return amount;
    }
}
