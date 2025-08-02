package solutions.york.budgetbookbackend.dto.transaction;

import solutions.york.budgetbookbackend.model.Transaction;

public class TransactionResponse {
    private Long id;
    private String description;
    private Long accountId;
    private String amount;
    private String transactionType;
    private String repeatUnit;
    private Integer repeatInterval;

    public TransactionResponse() {}
    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.description = transaction.getDescription();
        this.accountId = transaction.getAccount().getId();
        this.amount = transaction.getAmount() + "";
        this.transactionType = transaction.getType().toString();
        if (transaction.getRepeatUnit() != null) {
            this.repeatUnit = transaction.getRepeatUnit().toString();
            this.repeatInterval = transaction.getRepeatInterval();
        } else {
            this.repeatUnit = null;
            this.repeatInterval = null;
        }
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public Long getAccountId() {
        return accountId;
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
    public Integer getRepeatInterval() {
        return repeatInterval;
    }

}
