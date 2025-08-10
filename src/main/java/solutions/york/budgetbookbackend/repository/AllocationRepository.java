package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.york.budgetbookbackend.model.Allocation;
import solutions.york.budgetbookbackend.model.Transaction;

import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    void deleteByTransaction(Transaction transaction);
    List<Allocation> findByTransaction(Transaction transaction);
}
