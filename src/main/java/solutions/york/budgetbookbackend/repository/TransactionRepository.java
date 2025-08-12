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
    @Query(value =
        """
        SELECT * FROM (
            SELECT DISTINCT
                t.id,
                t.date,
                t.description,
                t.account_id,
                t.type,
                t.amount,
                t.repeat_unit,
                t.repeat_interval,
                t.archived,
                t.customer_id
            FROM transaction t
            LEFT JOIN allocation a ON a.transaction_id = t.id
            WHERE t.customer_id = :customerId
            AND t.archived = false
            AND (:accountId IS NULL OR t.account_id = :accountId)
            AND (:transactionType IS NULL OR t.type = CAST(:transactionType AS VARCHAR(255)))
            AND (:dateFrom IS NULL OR t.date >= CAST(:dateFrom AS DATE))
            AND (:dateTo IS NULL OR t.date <= CAST(:dateTo AS DATE))
            AND (:categoryId IS NULL OR a.category_id = :categoryId)
   
            UNION ALL
    
            SELECT DISTINCT
                -1 * (t.id * 10000 + ROW_NUMBER() OVER (PARTITION BY t.id ORDER BY future_date.date)),
                future_date.date AS date,
                t.description,
                t.account_id,
                t.type,
                t.amount,
                t.repeat_unit,
                t.repeat_interval,
                t.archived,
                t.customer_id
            FROM transaction t
            JOIN generate_series(
                CAST(:dateFrom AS DATE),
                CAST(:dateTo AS DATE),
                (t.repeat_interval || ' ' || t.repeat_unit)::interval
            ) AS future_date(date) ON future_date.date > t.date
            LEFT JOIN allocation a ON a.transaction_id = t.id
            WHERE t.repeat_unit IS NOT NULL
            AND t.repeat_interval IS NOT NULL
            AND t.customer_id = :customerId
            AND t.archived = false
            AND (:accountId IS NULL OR t.account_id = :accountId)
            AND (:transactionType IS NULL OR t.type = CAST(:transactionType AS VARCHAR))
            AND (:categoryId IS NULL OR a.category_id = :categoryId)
        ) AS transaction
    
        ORDER BY transaction.date DESC, transaction.id DESC
        """,
        countQuery=
        """
        SELECT COUNT (*) (
            SELECT DISTINCT t.id
            FROM allocation a
            JOIN transaction t ON a.transaction_id = t.id
            WHERE t.customer_id = :customerId
            AND t.archived = false
            AND (:accountId IS NULL OR t.account_id = :accountId)
            AND (:transactionType IS NULL OR t.type = CAST(:transactionType AS VARCHAR))
            AND (:dateFrom IS NULL OR t.date >= CAST(:dateFrom AS DATE))
            AND (:dateTo IS NULL OR t.date <= CAST(:dateTo AS DATE))
            AND (:categoryId IS NULL OR a.category_id = :categoryId)
    
            UNION ALL
    
            SELECT DISTINCT
                -1 * (t.id * 10000 + ROW_NUMBER() OVER (PARTITION BY t.id ORDER BY future_date.date)),
                future_date.date AS date,
                t.description,
                t.account_id,
                t.type,
                t.amount,
                t.repeat_unit,
                t.repeat_interval,
                t.archived,
                t.customer_id
            FROM transaction t
            JOIN generate_series(
                CAST(:dateFrom AS DATE),
                CAST(:dateTo AS DATE),
                (t.repeat_interval || ' ' || t.repeat_unit)::interval
            ) AS future_date(date) ON future_date.date > t.date
            LEFT JOIN allocation a ON a.transaction_id = t.id
            WHERE t.repeat_unit IS NOT NULL
            AND t.repeat_interval IS NOT NULL
            AND t.customer_id = :customerId
            AND t.archived = false
            AND (:accountId IS NULL OR t.account_id = :accountId)
            AND (:transactionType IS NULL OR t.type = CAST(:transactionType AS VARCHAR))
            AND (:categoryId IS NULL OR a.category_id = :categoryId)
        ) AS transactions
        """,
    nativeQuery = true)
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

