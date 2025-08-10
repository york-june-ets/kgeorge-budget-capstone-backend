package solutions.york.budgetbookbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import solutions.york.budgetbookbackend.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = """
        SELECT DISTINCT t.* FROM transaction t
        LEFT JOIN allocation a ON a.transaction_id = t.id
        WHERE t.customer_id = :customerId
        AND t.archived = false
        AND (:accountId IS NULL OR t.account_id = :accountId)
        AND (:transactionType IS NULL OR t.type = CAST(:transactionType AS VARCHAR(255)))
        AND (:dateFrom IS NULL OR t.date >= CAST(:dateFrom AS DATE))
        AND (:dateTo IS NULL OR t.date <= CAST(:dateTo AS DATE))
        AND (:categoryId IS NULL OR a.category_id = :categoryId)
        ORDER BY t.date DESC, t.id DESC
    """, countQuery= """
        SELECT COUNT (DISTINCT t.id) FROM allocation a
        JOIN transaction t ON a.transaction_id = t.id
        WHERE t.customer_id = :customerId
        AND (:accountId IS NULL OR t.account_id = :accountId)
        AND (:transactionType IS NULL OR t.type = CAST(:transactionType AS VARCHAR))
        AND (:dateFrom IS NULL OR t.date >= CAST(:dateFrom AS DATE))
        AND (:dateTo IS NULL OR t.date <= CAST(:dateTo AS DATE))
        AND (:categoryId IS NULL OR a.category_id = :categoryId)
    """, nativeQuery = true)
    Page<Transaction> findTransactionsWithFilters(
            @Param("customerId") Long customerId,
            @Param("accountId") Long accountId,
            @Param("transactionType") String transactionType,
            @Param("dateFrom") String dateFrom,
            @Param("dateTo") String dateTo,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}

