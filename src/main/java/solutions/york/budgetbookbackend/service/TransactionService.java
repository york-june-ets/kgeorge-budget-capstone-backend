package solutions.york.budgetbookbackend.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import solutions.york.budgetbookbackend.dto.allocation.AllocationRequest;
import solutions.york.budgetbookbackend.dto.allocation.AllocationResponse;
import solutions.york.budgetbookbackend.dto.transaction.TransactionRequest;
import solutions.york.budgetbookbackend.dto.transaction.TransactionResponse;
import solutions.york.budgetbookbackend.model.*;
import solutions.york.budgetbookbackend.repository.TransactionRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final AccountService accountService;
    private final AllocationService allocationService;

    public TransactionService(TransactionRepository transactionRepository, AuthService authService, CategoryService categoryService, AccountService accountService, AllocationService allocationService) {
        this.transactionRepository = transactionRepository;
        this.authService = authService;
        this.accountService = accountService;
        this.allocationService = allocationService;
    }

    public void validateTransactionRequest(TransactionRequest request) {
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

    @Scheduled(cron = "0 0 3 * * *")
    public void createRecurringTransaction() {
        System.out.println("Creating recurring transactions");
        LocalDate today = LocalDate.now();
        List<Transaction> recurringTransactions = transactionRepository.findActiveRecurringTransactionsBeforeDate(today.toString());

        for (Transaction transaction : recurringTransactions) {
            LocalDate startDate = transaction.getDate();
            LocalDate nextDate = startDate;

            Transaction.RepeatUnit unit = transaction.getRepeatUnit();
            int interval = transaction.getRepeatInterval();

            if (startDate.isAfter(today)) {
                continue;
            }

            while (!nextDate.isAfter(today)) {
                if (nextDate.equals(today)) {
                    createRecurringTransaction(transaction);

                    break;
                }
                switch (unit) {
                    case DAY:
                        nextDate = nextDate.plusDays(interval);
                        break;
                    case WEEK:
                        nextDate = nextDate.plusWeeks(interval);
                        break;
                    case MONTH:
                        nextDate = nextDate.plusMonths(interval);
                        break;
                    case YEAR:
                        nextDate = nextDate.plusYears(interval);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown repeat unit");
                }
            }
        }
    }

    @Transactional
    public void createRecurringTransaction(Transaction transaction) {
        Transaction recurringTransaction = new Transaction(transaction);
        transactionRepository.save(recurringTransaction);

        List<Allocation> allocations = allocationService.findByTransaction(transaction);
        if (transaction.getType() == Transaction.Type.WITHDRAWAL) {
            if (allocations.size() > 0) {
                for(Allocation allocation : allocations) {
                    allocationService.createAllocation(recurringTransaction.getCustomer(),recurringTransaction, new AllocationRequest(allocation));
                }
            }
        } else {
            if (allocations.size() > 0) {
                throw new IllegalArgumentException("Allocations are not supported for deposit transactions");
            }
        }

        accountService.updateBalance(recurringTransaction.getAccount(), recurringTransaction);
    }


    @Transactional
    public TransactionResponse createTransaction(String token, TransactionRequest request) {
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
        List<AllocationResponse> allocationResponses = allocationService.getAllocationsByTransactionId(transaction.getId(), token);
        return new TransactionResponse(transaction, allocationResponses);
    }

    @Transactional
    public TransactionResponse updateTransaction(Long id, String token , TransactionRequest request) {
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

        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        accountService.removePreviousBalance(transaction.getAccount(), transaction);
        transaction.update(account, date, request.getDescription(), amount, transactionType, repeatInterval, repeatUnit);
        transactionRepository.save(transaction);
        allocationService.deleteAllocations(transaction);
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
        List<AllocationResponse> allocationResponses = allocationService.getAllocationsByTransactionId(id, token);
        return new TransactionResponse(transaction, allocationResponses);

    }

    public TransactionResponse archiveTransaction(Long id, String token) {
        Auth auth = authService.validateToken(token);
        Customer customer = auth.getCustomer();
        authService.validateCustomer(customer);
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        transaction.setArchived(true);
        transactionRepository.save(transaction);
        List<AllocationResponse> allocationResponses = allocationService.getAllocationsByTransactionId(id, token);
        return new TransactionResponse(transaction, allocationResponses);
    }

    public boolean validateDateNoThrow(String date) {
        if (date == null || date.isBlank()) {return false;}
        try {
            LocalDate.parse(date);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean validateTypeNoThrow(String type) {
        if (type == null || type.isBlank()) {return false;}
        try {
            Transaction.Type.valueOf(type);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Page<TransactionResponse> getCustomerTransactions(String token, Long accountId, String transactionType, String dateFrom, String dateTo, Long categoryId, Integer page) {
        Auth auth = authService.validateToken(token);
        Customer customer = auth.getCustomer();
        authService.validateCustomer(customer);

        Long customerId = customer.getId();

        Pageable pageable = page != null ? PageRequest.of(page, 10) : Pageable.unpaged();

        return transactionRepository.findTransactionsWithFilters(
            customerId,
            accountId,
            validateTypeNoThrow(transactionType) ? transactionType : null,
            validateDateNoThrow(dateFrom) ? dateFrom : null,
            validateDateNoThrow(dateTo) ? dateTo : null,
            categoryId,
            pageable
        )
        .map(transaction -> {
            Long id = transaction.getId();
            if (id < 0) {
                int intId = id.intValue();
                intId = (intId / 10000) * -1;
                id = Long.valueOf(intId);
            }
            List<AllocationResponse> allocationResponses = allocationService.getAllocationsByTransactionId(id, token);
            return new TransactionResponse(transaction, allocationResponses);
        });
    }

    public void downloadTransactionsAsCsv(String token, HttpServletResponse response, Long accountId, String transactionType, String dateFrom, String dateTo, Long categoryId, Integer page) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.csv");

        Auth auth = authService.validateToken(token);
        Customer customer = auth.getCustomer();
        authService.validateCustomer(customer);

        List<TransactionResponse> transactionResponses = getCustomerTransactions(token, accountId, transactionType, dateFrom, dateTo, categoryId, page).getContent();

        PrintWriter writer = response.getWriter();

        writer.println("Date,Description,Account,TransactionType,Amount,Allocations,Repeat Unit,Repeat Interval");

        for (TransactionResponse t: transactionResponses) {
            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                t.getDate(),
                t.getDescription(),
                t.getAccount().getName(),
                t.getTransactionType(),
                "$" + t.getAmount(),
                "[" + t.getAllocations()
                        .stream()
                        .map(a -> a.getCategory() + ": " + a.getAmount())
                        .collect(Collectors.joining(", ")) +
                "]",
                t.getRepeatUnit(),
                t.getRepeatInterval()
            );
        }

        writer.flush();
    }

}
