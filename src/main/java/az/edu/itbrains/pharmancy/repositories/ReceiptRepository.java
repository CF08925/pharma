package az.edu.itbrains.pharmancy.repositories;

import az.edu.itbrains.pharmancy.models.Receipt;
import az.edu.itbrains.pharmancy.models.ReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    // Find receipts by user
    List<Receipt> findByUserId(Long userId);
    List<Receipt> findByUserIdOrderByUploadDateDesc(Long userId);

    // Find receipts by status
    List<Receipt> findByStatus(ReceiptStatus status);
    List<Receipt> findByStatusOrderByUploadDateDesc(ReceiptStatus status);

    // Find receipts by phone number
    List<Receipt> findByPhoneOperatorAndPhoneNumber(String phoneOperator, String phoneNumber);

    // Find receipts by date range
    List<Receipt> findByUploadDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find receipts by user and status
    List<Receipt> findByUserIdAndStatus(Long userId, ReceiptStatus status);

    // Count receipts by status
    long countByStatus(ReceiptStatus status);

    // Count receipts by user
    long countByUserId(Long userId);

    // Custom queries
    @Query("SELECT r FROM Receipt r WHERE r.user.email = :email ORDER BY r.uploadDate DESC")
    List<Receipt> findByUserEmailOrderByUploadDateDesc(@Param("email") String email);


    @Query("SELECT r FROM Receipt r WHERE r.amount >= :minAmount AND r.amount <= :maxAmount ORDER BY r.uploadDate DESC")
    List<Receipt> findByAmountRange(@Param("minAmount") Long minAmount, @Param("maxAmount") Long maxAmount);

    // Find recent receipts (last 30 days)
    @Query("SELECT r FROM Receipt r WHERE r.uploadDate >= :thirtyDaysAgo ORDER BY r.uploadDate DESC")
    List<Receipt> findRecentReceipts(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    // Find all receipts ordered by upload date (for admin)
    @Query("SELECT r FROM Receipt r ORDER BY r.uploadDate DESC")
    List<Receipt> findAllOrderByUploadDateDesc();
}
