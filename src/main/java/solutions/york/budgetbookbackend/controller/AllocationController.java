package solutions.york.budgetbookbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solutions.york.budgetbookbackend.dto.allocation.AllocationResponse;
import solutions.york.budgetbookbackend.service.AllocationService;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
public class AllocationController {
    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<List<AllocationResponse>> getAllocationsForTransaction(@PathVariable Long transactionId, @RequestHeader("Authorization") String token) {
        List<AllocationResponse> allocationResponses = allocationService.getAllocationsByTransactionId(transactionId, token);
        return ResponseEntity.ok(allocationResponses);
    }
}
