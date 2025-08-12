package solutions.york.budgetbookbackend.dto.allocation;

import solutions.york.budgetbookbackend.model.Allocation;

public class AllocationRequest {
    private String category;
    private String amount;

    public AllocationRequest() {}
    public AllocationRequest(Allocation allocation) {
        this.category = allocation.getCategory().getName();
        this.amount = allocation.getAmount() + "";
    }

    // GETTERS
    public String getCategory() {
        return category;
    }
    public String getAmount() {
        return amount;
    }
}
