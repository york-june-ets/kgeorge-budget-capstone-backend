package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.york.budgetbookbackend.dto.transaction.TransactionRequest;
import solutions.york.budgetbookbackend.dto.transaction.TransactionResponse;
import solutions.york.budgetbookbackend.service.TransactionService;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getCustomerTransactions(@RequestHeader("Authorization") String token) {
        List<TransactionResponse> transactionResponses = transactionService.getCustomerTransactions(token);
        return ResponseEntity.ok(transactionResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestHeader("Authorization") String token, @RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.updateTransaction(id, token, request);
        return ResponseEntity.ok(transactionResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionResponse> deleteTransaction(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        TransactionResponse transactionResponse = transactionService.archiveTransaction(id, token);
        return ResponseEntity.ok(transactionResponse);
    }
}
