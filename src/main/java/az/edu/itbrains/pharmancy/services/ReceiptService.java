package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptCreateDto;
import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptDto;
import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptResponseDto;
import az.edu.itbrains.pharmancy.models.Receipt;
import az.edu.itbrains.pharmancy.models.ReceiptStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReceiptService {

    /**
     * Upload a new receipt with images
     */
    ReceiptResponseDto uploadReceipt(String userEmail, ReceiptCreateDto receiptCreateDto, List<MultipartFile> images);

    /**
     * Get all receipts for a specific user
     */
    List<ReceiptDto> getUserReceipts(String userEmail);

    /**
     * Get receipt by ID
     */
    ReceiptDto getReceiptById(Long receiptId);

    /**
     * Get all receipts (for admin)
     */
    List<ReceiptDto> getAllReceipts();

    /**
     * Get receipts by status
     */
    List<ReceiptDto> getReceiptsByStatus(ReceiptStatus status);

    /**
     * Update receipt status (for admin)
     */
    boolean updateReceiptStatus(Long receiptId, ReceiptStatus status);

    /**
     * Delete receipt
     */
    boolean deleteReceipt(Long receiptId);

    /**
     * Get receipt entity by ID
     */
    Receipt findReceiptById(Long receiptId);

    /**
     * Get recent receipts (last 30 days)
     */
    List<ReceiptDto> getRecentReceipts();

    /**
     * Count receipts by status
     */
    long countReceiptsByStatus(ReceiptStatus status);

    // REMOVED: searchReceiptsByStoreName method - no longer needed
}