package solutions.york.budgetbookbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import solutions.york.budgetbookbackend.model.Allocation;
import solutions.york.budgetbookbackend.model.Transaction;

import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
    void deleteByTransaction(Transaction transaction);
    List<Allocation> findByTransaction(Transaction transaction);

    @Query(value = """
        SELECT DISTINCT t.* FROM allocation a
        JOIN transaction t ON a.transaction_id = t.id
        WHERE t.customer_id = :customerId
        AND (:accountId IS NULL OR t.account_id = :accountId)
        AND (:transactionType IS NULL OR t.type = CAST(:transactionType AS VARCHAR))
        AND (:dateFrom IS NULL OR t.date >= CAST(:dateFrom AS DATE))
        AND (:dateTo IS NULL OR t.date <= CAST(:dateTo AS DATE))
        AND (:categoryId IS NULL OR a.category_id = :categoryId)
    """, nativeQuery = true)
    List<Transaction> findTransactionsWithFilters(
        @Param("customerId") Long customerId,
        @Param("accountId") Long accountId,
        @Param("transactionType") String transactionType,
        @Param("dateFrom") String dateFrom,
        @Param("dateTo") String dateTo,
        @Param("categoryId") Long categoryId
    );

}
