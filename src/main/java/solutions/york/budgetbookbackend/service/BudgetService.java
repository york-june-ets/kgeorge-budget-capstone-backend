package solutions.york.budgetbookbackend.service;

import org.springframework.stereotype.Service;
import solutions.york.budgetbookbackend.dto.budget.BudgetRequest;
import solutions.york.budgetbookbackend.dto.budget.BudgetResponse;
import solutions.york.budgetbookbackend.model.*;
import solutions.york.budgetbookbackend.repository.BudgetRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final AuthService authService;
    private final CategoryService categoryService;

    public BudgetService(BudgetRepository budgetRepository, AuthService authService, CategoryService categoryService) {
        this.budgetRepository = budgetRepository;
        this.authService = authService;
        this.categoryService = categoryService;
    }

    public void validateBudgetRequest(BudgetRequest request) {
        if (request == null) {throw new IllegalArgumentException("Request cannot be null");}
        if (request.getCategory() == null || request.getCategory().isBlank()) {throw new IllegalArgumentException("Category cannot be null");}
        if (request.getBudgetLimit() == null || request.getBudgetLimit().isBlank()) {throw new IllegalArgumentException("Limit cannot be null");}
        if (request.getTimePeriod() == null || request.getTimePeriod().isBlank()) {throw new IllegalArgumentException("Time period cannot be null");}
    }

    public Category validateCategory(String categoryName, Customer customer) {
        Category category = categoryService.findByCustomerAndName(customer, categoryName);
        if (category == null) {
            throw new IllegalArgumentException("Category does not exist");
        }
        if (category.getArchived() == true) {
            throw new IllegalArgumentException("Category is archived");
        }
        Budget budget = budgetRepository.findByCustomerAndCategory(customer, category).orElse(null);
        if (budget != null && budget.getArchived() == false) {
            throw new IllegalArgumentException("Category already has a budget");
        }
        return category;
    }

    public Budget.TimePeriod validateBudgetTimePeriod(String timePeriod) {
        try {
            return Budget.TimePeriod.valueOf(timePeriod);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid time period");
        }
    }

    public double validateBudgetLimit(String budgetLimit) {
        try {
            double doubleBudgetLimit = Double.parseDouble(budgetLimit);
            if (doubleBudgetLimit < 0) {throw new IllegalArgumentException("Limit cannot be negative");}
            return doubleBudgetLimit;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Limit is not a number");
        }
    }

    public void validateBelongs(Budget budget, Auth auth) {
        if (budget.getCustomer().getId() != auth.getCustomer().getId()) {
            throw new IllegalArgumentException("Account does not belong to customer");
        }
    }

    public BudgetResponse createBudget(String token, BudgetRequest request) {
        validateBudgetRequest(request);
        Auth auth = authService.validateToken(token);
        Category category = validateCategory(request.getCategory(), auth.getCustomer());
        Budget.TimePeriod timePeriod = validateBudgetTimePeriod(request.getTimePeriod());
        double budgetLimit = validateBudgetLimit(request.getBudgetLimit());
        Budget existingBudget = budgetRepository.findByCustomerAndCategory(auth.getCustomer(), category).orElse(null);
        if (existingBudget != null) {
            if (existingBudget.getArchived() == true) {
                existingBudget.setArchived(false);
                existingBudget.update(budgetLimit, timePeriod);
                budgetRepository.save(existingBudget);
                return new BudgetResponse(existingBudget);
            } else {
                throw new IllegalArgumentException("Category already has a budget");
            }
        }
        Budget budget = new Budget(auth.getCustomer(), category, budgetLimit, timePeriod);
        budgetRepository.save(budget);
        return new BudgetResponse(budget);
    }

    public List<BudgetResponse> getCustomerBudgets(String token) {
        Auth auth = authService.validateToken(token);
        return budgetRepository.findByCustomer(auth.getCustomer()).stream()
                .filter(budget -> !budget.getArchived())
                .map(BudgetResponse::new)
                .collect(Collectors.toList());
    }

    public BudgetResponse updateBudget(Long id, String token, BudgetRequest request) {
        validateBudgetRequest(request);
        Auth auth = authService.validateToken(token);
        Budget.TimePeriod timePeriod = validateBudgetTimePeriod(request.getTimePeriod());
        double budgetLimit = validateBudgetLimit(request.getBudgetLimit());
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Budget not found"));
        validateBelongs(budget, auth);
        budget.update(budgetLimit, timePeriod);
        budgetRepository.save(budget);
        return new BudgetResponse(budget);
    }

    public void archiveBudget(Long id, String token) {
        Auth auth = authService.validateToken(token);
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Budget not found"));
        validateBelongs(budget, auth);
        budget.setArchived(true);
        budgetRepository.save(budget);
    }
}
