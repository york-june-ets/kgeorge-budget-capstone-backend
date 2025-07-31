package solutions.york.budgetbookbackend.dto.category;

import solutions.york.budgetbookbackend.model.Category;

public class CategoryResponse {
    private Long id;
    private String name;

    public CategoryResponse() {}
    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    // GETTERS
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
