package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.york.budgetbookbackend.dto.account.AccountRequest;
import solutions.york.budgetbookbackend.dto.account.AccountResponse;
import solutions.york.budgetbookbackend.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestHeader("Authorization") String token, @RequestBody AccountRequest request) {
        AccountResponse accountResponse = accountService.createAccount(token, request);
        return ResponseEntity.ok(accountResponse);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getCustomerAccounts(@RequestHeader("Authorization") String token) {
        List<AccountResponse> accountResponses = accountService.getCustomerAccounts(token);
        return ResponseEntity.ok(accountResponses);
    }
}
