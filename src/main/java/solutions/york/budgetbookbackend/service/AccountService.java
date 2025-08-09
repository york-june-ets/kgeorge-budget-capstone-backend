package solutions.york.budgetbookbackend.service;

import org.springframework.stereotype.Service;
import solutions.york.budgetbookbackend.dto.account.AccountRequest;
import solutions.york.budgetbookbackend.dto.account.AccountResponse;
import solutions.york.budgetbookbackend.model.Account;
import solutions.york.budgetbookbackend.model.Auth;
import solutions.york.budgetbookbackend.model.Customer;
import solutions.york.budgetbookbackend.model.Transaction;
import solutions.york.budgetbookbackend.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AuthService authService;

    public AccountService(AccountRepository accountRepository, AuthService authService) {
        this.accountRepository = accountRepository;
        this.authService = authService;
    }

    public void validateAccountRequest(AccountRequest request) {
        if (request == null) {throw new IllegalArgumentException("Request cannot be null");}
        if (request.getName() == null || request.getName().isBlank()) {throw new IllegalArgumentException("Name cannot be null");}
        if (request.getType() == null || request.getType().isBlank()) {throw new IllegalArgumentException("Type cannot be null");}
        if (request.getBalance() == null || request.getBalance().isBlank()) {throw new IllegalArgumentException("Balance cannot be null");}
    }

    public void validateBelongs(Account account, Auth auth) {
        if (account.getCustomer().getId() != auth.getCustomer().getId()) {
            throw new IllegalArgumentException("Account does not belong to customer");
        }
    }

    public Account.Type validateAccountType(String type) {
        try {
            return Account.Type.valueOf(type);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid account type");
        }
    }

    public double validateBalance(String balance) {
        try {
            return Double.parseDouble(balance);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Balance is not a number");
        }
    }

    public AccountResponse createAccount(String token, AccountRequest request) {
        validateAccountRequest(request);
        Auth auth = authService.validateToken(token);
        Account.Type accountType = validateAccountType(request.getType());
        double balance = validateBalance(request.getBalance());
        Account account = new Account(auth.getCustomer(), request.getName(), accountType, balance);
        accountRepository.save(account);
        return new AccountResponse(account);
    }

    public List<AccountResponse> getCustomerAccounts(String token) {
        Auth auth = authService.validateToken(token);
        return accountRepository.findByCustomer(auth.getCustomer()).stream()
                .filter(account -> !account.getArchived())
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    public AccountResponse updateAccount(Long id, String token, AccountRequest request) {
        validateAccountRequest(request);
        Auth auth = authService.validateToken(token);
        validateAccountType(request.getType());
        validateBalance(request.getBalance());
        Account account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        validateBelongs(account, auth);
        account.update(request);
        accountRepository.save(account);
        return new AccountResponse(account);
    }

    public void archiveAccount(Long id, String token) {
        Auth auth = authService.validateToken(token);
        Account account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        validateBelongs(account, auth);
        account.setArchived(true);
        accountRepository.save(account);
    }

    public Account findByCustomerAndId(Customer customer, Long id) {
        return accountRepository.findByCustomerAndId(customer, id).orElse(null);
    }

    public void updateBalance(Account account, Transaction transaction) {
        if (transaction.getType() == Transaction.Type.DEPOSIT) {
            account.setBalance(account.getBalance() + transaction.getAmount());
        }
        if (transaction.getType() == Transaction.Type.WITHDRAWAL) {
            account.setBalance(account.getBalance() - transaction.getAmount());
        }
        accountRepository.save(account);
    }

    public void removePreviousBalance(Account account, Transaction transaction) {
        if (transaction.getType() == Transaction.Type.DEPOSIT) {
            account.setBalance(account.getBalance() - transaction.getAmount());
        }
        if (transaction.getType() == Transaction.Type.WITHDRAWAL) {
            account.setBalance(account.getBalance() + transaction.getAmount());
        }
        accountRepository.save(account);
    }
}
