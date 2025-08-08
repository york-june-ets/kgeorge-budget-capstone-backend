package solutions.york.budgetbookbackend.service;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import solutions.york.budgetbookbackend.dto.allocation.AllocationRequest;
import solutions.york.budgetbookbackend.dto.allocation.AllocationResponse;
import solutions.york.budgetbookbackend.dto.transaction.TransactionResponse;
import solutions.york.budgetbookbackend.model.*;
import solutions.york.budgetbookbackend.repository.AllocationRepository;
import solutions.york.budgetbookbackend.repository.TransactionRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllocationService {
    private final AllocationRepository allocationRepository;
    private final CategoryService categoryService;
    private final AuthService authService;
    private final TransactionRepository transactionRepository;

    public AllocationService(AllocationRepository allocationRepository, CategoryService categoryService, AuthService authService, TransactionRepository transactionRepository) {
        this.allocationRepository = allocationRepository;
        this.categoryService = categoryService;
        this.authService = authService;
        this.transactionRepository = transactionRepository;
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

    public void validateTransaction(Transaction transaction, Customer customer) {
        if (transaction == null) {throw new IllegalArgumentException("Transaction cannot be null");}
        if (transaction.getArchived() == true) {throw new IllegalArgumentException("Transaction is archived");}
        if (transaction.getCustomer() != customer) {throw new IllegalArgumentException("Transaction does not belong to customer");}
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

    public void deleteAllocations(Transaction transaction) {
        allocationRepository.deleteByTransaction(transaction);
    }

    public List<AllocationResponse> getAllocationsByTransactionId(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Auth auth = authService.validateToken(token);
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        validateTransaction(transaction, auth.getCustomer());
        return allocationRepository.findByTransaction(transaction).stream()
                .map(AllocationResponse::new)
                .collect(Collectors.toList());

    }

    public List<Transaction> findTransactionsWithFilters(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false)Long accountId,
            @RequestParam(required = false) Transaction.Type transactionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) Long categoryId
        ) {
        return allocationRepository.findTransactionsWithFilters(customerId, accountId, transactionType, dateFrom, dateTo, categoryId);
    }
}
