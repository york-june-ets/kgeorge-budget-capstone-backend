package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solutions.york.budgetbookbackend.model.Customer;
import solutions.york.budgetbookbackend.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomer(Customer customer);
}

