package solutions.york.budgetbookbackend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.york.budgetbookbackend.dto.transaction.TransactionRequest;
import solutions.york.budgetbookbackend.dto.transaction.TransactionResponse;
import solutions.york.budgetbookbackend.service.TransactionService;

import java.io.IOException;
import java.time.LocalDate;
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
    public ResponseEntity<List<TransactionResponse>> getCustomerTransactions(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) Long categoryId
    ) {
        List<TransactionResponse> transactionResponses = transactionService.getCustomerTransactions(token, accountId, transactionType, dateFrom, dateTo, categoryId);
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

    @GetMapping("/download/csv")
    public void downloadTransactionsAsCsv(
            @RequestHeader("Authorization") String token,
            HttpServletResponse response,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  dateTo,
            @RequestParam(required = false) Long categoryId
            ) throws IOException {
            transactionService.downloadTransactionsAsCsv(token, response, accountId, transactionType, dateFrom, dateTo, categoryId);
    }
}
