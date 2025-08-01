package solutions.york.budgetbookbackend.dto.budget;

import solutions.york.budgetbookbackend.model.Budget;

public class BudgetResponse {
    private Long id;
    private String category;
    private double budgetLimit;
    private String timePeriod;

    public BudgetResponse() {}
    public BudgetResponse(Budget budget) {
        this.id = budget.getId();
        this.category = budget.getCategory().getName();
        this.budgetLimit = budget.getBudgetLimit();
        this.timePeriod = budget.getTimePeriod().toString();
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }
    public double getBudgetLimit() {
        return budgetLimit;
    }
    public String getTimePeriod() {
        return timePeriod;
    }
}
