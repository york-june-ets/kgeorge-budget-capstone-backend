package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutions.york.budgetbookbackend.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

