package com.cmi.cmipaymentintegration.repository;

import com.cmi.cmipaymentintegration.entity.Transaction;
import com.cmi.cmipaymentintegration.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);
    Optional<Transaction> findByCmiTransactionId(String cmiTransactionId);
    List<Transaction> findByStatusAndCreatedAtBefore(TransactionStatus status, LocalDateTime createdAt);
    @Query("SELECT t FROM Transaction t WHERE t.customerEmail = :email AND t.createdAt >= :fromDate ORDER BY t.createdAt DESC")
    List<Transaction> findByCustomerEmailAndDateRange(
            @Param("email") String email,
            @Param("fromDate") LocalDateTime fromDate);
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = :status AND t.createdAt >= :fromDate")
    Long countByStatusAndDateRange(
            @Param("status") TransactionStatus status,
            @Param("fromDate") LocalDateTime fromDate
    );
}
