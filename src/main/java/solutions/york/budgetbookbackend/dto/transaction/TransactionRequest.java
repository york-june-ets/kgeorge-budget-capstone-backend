package solutions.york.budgetbookbackend.dto.transaction;

import solutions.york.budgetbookbackend.dto.allocation.AllocationRequest;

public class TransactionRequest {
    private String date;
    private String description;
    private String amount;
    private Long accountId;
    private String transactionType;
    private String repeatUnit;
    private String repeatInterval;
    private AllocationRequest[] allocations;

    // GETTERS
    public String getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public String getAmount() {
        return amount;
    }
    public Long getAccountId() {
        return accountId;
    }
    public String getTransactionType() {
        return transactionType;
    }
    public String getRepeatUnit() {
        return repeatUnit;
    }
    public String getRepeatInterval() {
        return repeatInterval;
    }
    public AllocationRequest[] getAllocations() {
        return allocations;
    }
}
