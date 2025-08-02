package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.york.budgetbookbackend.dto.transaction.TransactionRequest;
import solutions.york.budgetbookbackend.dto.transaction.TransactionResponse;
import solutions.york.budgetbookbackend.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.createTransaction(token, request);
        return ResponseEntity.ok(transactionResponse);
    }
}
