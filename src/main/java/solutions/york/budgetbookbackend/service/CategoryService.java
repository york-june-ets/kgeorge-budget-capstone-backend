package solutions.york.budgetbookbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import solutions.york.budgetbookbackend.dto.category.CategoryRequest;
import solutions.york.budgetbookbackend.dto.category.CategoryResponse;
import solutions.york.budgetbookbackend.model.Auth;
import solutions.york.budgetbookbackend.model.Budget;
import solutions.york.budgetbookbackend.model.Category;
import solutions.york.budgetbookbackend.model.Customer;
import solutions.york.budgetbookbackend.repository.BudgetRepository;
import solutions.york.budgetbookbackend.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AuthService authService;
    private final BudgetRepository budgetRepository;
    public CategoryService(CategoryRepository categoryRepository, AuthService authService, BudgetRepository budgetRepository) {
        this.categoryRepository = categoryRepository;
        this.authService = authService;
        this.budgetRepository = budgetRepository;
    }

    public void validateCategoryRequest(CategoryRequest request) {
        if (request == null) {throw new IllegalArgumentException("Request cannot be null");}
        if (request.getName() == null || request.getName().isBlank()) {throw new IllegalArgumentException("Name cannot be null");}
    }

    public void validateBelongs(Category category, Auth auth) {
        if (category.getCustomer().getId() != auth.getCustomer().getId()) {
            throw new IllegalArgumentException("Account does not belong to customer");
        }
    }

    public CategoryResponse createCategory(@RequestHeader("Authorization") String token, @RequestBody CategoryRequest request) {
        validateCategoryRequest(request);
        Auth auth = authService.validateToken(token);
        Category existingCategory = categoryRepository.findByCustomerAndName(auth.getCustomer(), request.getName()).orElse(null);
        if (existingCategory != null) {
            if (existingCategory.getArchived() == true) {
                existingCategory.setArchived(false);
                categoryRepository.save(existingCategory);
                return new CategoryResponse(existingCategory);
            } else {
                throw new IllegalArgumentException("Category already exists");
            }
        }
        Category category = new Category(auth.getCustomer(), request.getName());
        categoryRepository.save(category);
        return new CategoryResponse(category);
    }

    public List<CategoryResponse> getCustomerCategories(@RequestHeader("Authorization") String token) {
        Auth auth = authService.validateToken(token);
        return categoryRepository.findByCustomer(auth.getCustomer()).stream()
                .filter(category -> !category.getArchived())
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    public void archiveCategory(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Auth auth = authService.validateToken(token);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        validateBelongs(category, auth);
        category.setArchived(true);
        Budget budget = budgetRepository.findByCustomerAndCategory(auth.getCustomer(), category).orElse(null);
        if (budget != null) {
            budget.setArchived(true);
            budgetRepository.save(budget);
        }
        categoryRepository.save(category);
    }

    public CategoryResponse updateCategory(@PathVariable Long id, @RequestHeader("Authorization") String token, @RequestBody CategoryRequest request) {
        validateCategoryRequest(request);
        Auth auth = authService.validateToken(token);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        validateBelongs(category, auth);
        category.setName(request.getName());
        categoryRepository.save(category);
        return new CategoryResponse(category);
    }

    public Category findByCustomerAndName(Customer customer, String name) {
        return categoryRepository.findByCustomerAndName(customer, name).orElse(null);
    }
}
