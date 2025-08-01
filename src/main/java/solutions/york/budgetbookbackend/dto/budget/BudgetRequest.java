package solutions.york.budgetbookbackend.dto.budget;

public class BudgetRequest {
    private String category;
    private String budgetLimit;
    private String timePeriod;

    // GETTERS
    public String getCategory() {
        return category;
    }
    public String getBudgetLimit() {
        return budgetLimit;
    }
    public String getTimePeriod() {
        return timePeriod;
    }
}
