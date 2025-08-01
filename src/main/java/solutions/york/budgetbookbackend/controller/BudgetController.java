package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.york.budgetbookbackend.dto.budget.BudgetRequest;
import solutions.york.budgetbookbackend.dto.budget.BudgetResponse;
import solutions.york.budgetbookbackend.service.BudgetService;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(@RequestHeader("Authorization") String token, @RequestBody BudgetRequest request) {
        BudgetResponse budgetResponse = budgetService.createBudget(token, request);
        return ResponseEntity.ok(budgetResponse);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getCustomerBudgets(@RequestHeader("Authorization") String token) {
        List<BudgetResponse> budgetResponses = budgetService.getCustomerBudgets(token);
        return ResponseEntity.ok(budgetResponses);
    }
}
