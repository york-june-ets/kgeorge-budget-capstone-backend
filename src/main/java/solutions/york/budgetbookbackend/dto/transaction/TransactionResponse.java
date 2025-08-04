package solutions.york.budgetbookbackend.dto.transaction;

import solutions.york.budgetbookbackend.dto.account.AccountResponse;
import solutions.york.budgetbookbackend.dto.allocation.AllocationResponse;
import solutions.york.budgetbookbackend.model.Account;
import solutions.york.budgetbookbackend.model.Transaction;

import java.util.List;

public class TransactionResponse {
    private Long id;
    private String date;
    private String description;
    private AccountResponse account;
    private String amount;
    private String transactionType;
    private String repeatUnit;
    private String repeatInterval;
    private List<AllocationResponse> allocations;

    public TransactionResponse() {}
    public TransactionResponse(Transaction transaction, List<AllocationResponse> allocations) {
        this.id = transaction.getId();
        this.date = transaction.getDate().toString();
        this.description = transaction.getDescription();
        this.account = new AccountResponse(transaction.getAccount());
        this.amount = transaction.getAmount() + "";
        this.transactionType = transaction.getType().toString();
        this.allocations = allocations;
        if (transaction.getRepeatUnit() != null) {
            this.repeatUnit = transaction.getRepeatUnit().toString();
            this.repeatInterval = transaction.getRepeatInterval().toString();
        } else {
            this.repeatUnit = "";
            this.repeatInterval = "";
        }
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public AccountResponse getAccount() {
        return account;
    }
    public String getAmount() {
        return amount;
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
    public List<AllocationResponse> getAllocations() {
        return allocations;
    }

}
