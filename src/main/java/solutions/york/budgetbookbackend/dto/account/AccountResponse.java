package solutions.york.budgetbookbackend.dto.account;

import solutions.york.budgetbookbackend.model.Account;

public class AccountResponse {
    private Long id;
    private String name;
    private String type;

    public AccountResponse() {}
    public AccountResponse(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.type = account.getType().toString();
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
}
