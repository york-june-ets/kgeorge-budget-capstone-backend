package solutions.york.budgetbookbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import solutions.york.budgetbookbackend.dto.allocation.AllocationRequest;
import solutions.york.budgetbookbackend.dto.budget.BudgetResponse;
import solutions.york.budgetbookbackend.dto.transaction.TransactionRequest;
import solutions.york.budgetbookbackend.dto.transaction.TransactionResponse;
import solutions.york.budgetbookbackend.model.*;
import solutions.york.budgetbookbackend.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final AllocationService allocationService;

    public TransactionService(TransactionRepository transactionRepository, AuthService authService, CategoryService categoryService, AccountService accountService, AllocationService allocationService) {
        this.transactionRepository = transactionRepository;
        this.authService = authService;
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.allocationService = allocationService;

    }

    public void validateTransactionRequest(@RequestBody TransactionRequest request) {
        if (request == null) {throw new IllegalArgumentException("Request cannot be null");}
        if (request.getAmount() == null || request.getAmount().isBlank()) {throw new IllegalArgumentException("Amount cannot be null");}
        if (request.getDescription() == null || request.getDescription().isBlank()) {throw new IllegalArgumentException("Description cannot be null");}
        if (request.getDate() == null || request.getDate().isBlank()) {throw new IllegalArgumentException("Date cannot be null");}
        if (request.getAccountId() == null) {throw new IllegalArgumentException("Account ID cannot be null");}
        if (request.getTransactionType() == null || request.getTransactionType().isBlank()) {throw new IllegalArgumentException("Transaction type cannot be null");}
        if (request.getRepeatUnit().isBlank() && !request.getRepeatInterval().isBlank()) {throw new IllegalArgumentException("Repeat interval cannot be empty if repeat unit is set");}
        if (!request.getRepeatInterval().isBlank() && request.getRepeatUnit().isBlank()) {throw new IllegalArgumentException("Repeat unit must be selected if repeat interval is set");}
    }

    public Account validateAccount(Long accountId, Customer customer) {
        Account account = accountService.findByCustomerAndId(customer, accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }
        if (account.getArchived() == true) {
            throw new IllegalArgumentException("Account is archived");
        }
        return account;
    }

    public Transaction.Type validateTransactionType(String transactionType) {
        try {
            return Transaction.Type.valueOf(transactionType);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid transaction type");
        }
    }

    public Transaction.RepeatUnit validateRepeatUnit(String repeatUnit) {
        if (repeatUnit.isBlank()) {return null;}
        try {
            return Transaction.RepeatUnit.valueOf(repeatUnit);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid repeat unit");
        }
    }

    public Integer validateRepeatInterval(String repeatInterval) {
        if (repeatInterval.isBlank()) {return null;}
        try {
            Integer repeatUnit = Integer.parseInt(repeatInterval);
            if (repeatUnit < 0) {throw new IllegalArgumentException("Repeat interval cannot be negative");}
            return repeatUnit;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Repeat interval is not a number");
        }
    }
    public double validateAmount(String amount) {
        try {
            return Double.parseDouble(amount);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Amount is not a number");
        }
    }

    public LocalDate validateDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Date is not a valid date");
        }
    }

    @Transactional
    public TransactionResponse createTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) {
        validateTransactionRequest(request);
        Auth auth = authService.validateToken(token);
        Customer customer = auth.getCustomer();
        authService.validateCustomer(customer);
        Account account = validateAccount(request.getAccountId(), auth.getCustomer());
        Transaction.Type transactionType = validateTransactionType(request.getTransactionType());
        Transaction.RepeatUnit repeatUnit = validateRepeatUnit(request.getRepeatUnit());
        Integer repeatInterval = validateRepeatInterval(request.getRepeatInterval());
        double amount = validateAmount(request.getAmount());
        LocalDate date = validateDate(request.getDate());

        Transaction transaction = new Transaction(customer, account, date, request.getDescription(), amount, transactionType, repeatUnit, repeatInterval);
        transactionRepository.save(transaction);

        if (transactionType == Transaction.Type.WITHDRAWAL) {
            if (request.getAllocations().length > 0) {
                for(AllocationRequest allocationRequest : request.getAllocations()) {
                    allocationService.createAllocation(customer,transaction, allocationRequest);
                }
            }
        } else {
            if (request.getAllocations().length > 0) {
                throw new IllegalArgumentException("Allocations are not supported for deposit transactions");
            }
        }

        accountService.updateBalance(account, transaction);
        return new TransactionResponse(transaction);
    }

    public List<TransactionResponse> getCustomerTransactions(@RequestHeader("Authorization") String token) {
        Auth auth = authService.validateToken(token);
        Customer customer = auth.getCustomer();
        authService.validateCustomer(customer);
        return transactionRepository.findByCustomer(customer).stream()
                .filter(transaction -> !transaction.getArchived())
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

}
