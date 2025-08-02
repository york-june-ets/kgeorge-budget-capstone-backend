package solutions.york.budgetbookbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import solutions.york.budgetbookbackend.dto.allocation.AllocationRequest;
import solutions.york.budgetbookbackend.dto.allocation.AllocationResponse;
import solutions.york.budgetbookbackend.model.*;
import solutions.york.budgetbookbackend.repository.AllocationRepository;

@Service
public class AllocationService {
    private final AllocationRepository allocationRepository;
    private final CategoryService categoryService;
    private final AuthService authService;

    public AllocationService(AllocationRepository allocationRepository, CategoryService categoryService, AuthService authService) {
        this.allocationRepository = allocationRepository;
        this.categoryService = categoryService;
        this.authService = authService;
    }

    public void validateAllocationRequest(AllocationRequest request) {
        if (request == null) {throw new IllegalArgumentException("Request cannot be null");}
        if (request.getAmount() == null || request.getAmount().isBlank()) {throw new IllegalArgumentException("Amount cannot be null");}
        if (request.getCategory() == null || request.getCategory().isBlank()) {throw new IllegalArgumentException("Category ID cannot be null");}
    }

    public double validateAmount(String amount) {
        try {
            return Double.parseDouble(amount);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Amount is not a number");
        }
    }

    public Category validateCategory(String categoryName, Customer customer) {
        Category category = categoryService.findByCustomerAndName(customer, categoryName);
        if (category == null) {
            throw new IllegalArgumentException("Category does not exist");
        }
        if (category.getArchived() == true) {
            throw new IllegalArgumentException("Category is archived");
        }
        return category;
    }

    // called in transaction service
    public AllocationResponse createAllocation(Customer customer, Transaction transaction, AllocationRequest request) {
        validateAllocationRequest(request);
        double amount = validateAmount(request.getAmount());
        Category category = validateCategory(request.getCategory(), customer);
        Allocation allocation = new Allocation(transaction, category, amount);
        allocationRepository.save(allocation);
        return new AllocationResponse(allocation);
    }
}
