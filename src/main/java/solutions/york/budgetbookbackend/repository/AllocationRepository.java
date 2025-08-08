package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solutions.york.budgetbookbackend.model.Allocation;
import solutions.york.budgetbookbackend.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    void deleteByTransaction(Transaction transaction);
    List<Allocation> findByTransaction(Transaction transaction);

    @Query("SELECT a.transaction FROM Allocation a " +
            "WHERE (a.transaction.customer.id = :customerId) AND " +
            "(:accountId IS NULL OR a.transaction.account.id = :accountId) AND " +
            "(:transactionType IS NULL OR a.transaction.type = :transactionType) AND " +
            "(:dateFrom IS NULL OR a.transaction.date >= :dateFrom) AND " +
            "(:dateTo IS NULL OR a.transaction.date <= :dateTo) AND " +
            "(:categoryId IS NULL OR a.category.id = :categoryId)")
    List<Transaction> findTransactionsWithFilters(
            @Param("customerId") Long customerId,
            @Param("accountId") Long accountId,
            @Param("transactionType") Transaction.Type transactionType,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("categoryId") Long categoryId
    );

}
