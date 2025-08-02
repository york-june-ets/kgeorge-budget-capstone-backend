package solutions.york.budgetbookbackend.dto.transaction;

import solutions.york.budgetbookbackend.model.Account;
import solutions.york.budgetbookbackend.model.Transaction;

public class TransactionResponse {
    private Long id;
    private String description;
    private Account account;
    private String amount;
    private String transactionType;
    private String repeatUnit;
    private String repeatInterval;

    public TransactionResponse() {}
    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.description = transaction.getDescription();
        this.account = transaction.getAccount();
        this.amount = transaction.getAmount() + "";
        this.transactionType = transaction.getType().toString();
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
    public String getDescription() {
        return description;
    }
    public Account getAccount() {
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

}
