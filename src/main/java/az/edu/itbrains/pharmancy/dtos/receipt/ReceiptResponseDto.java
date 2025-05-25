package az.edu.itbrains.pharmancy.dtos.receipt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptResponseDto {
    private boolean success;
    private String message;
    private Long receiptId;

    public static ReceiptResponseDto success(String message, Long receiptId) {
        return new ReceiptResponseDto(true, message, receiptId);
    }

    public static ReceiptResponseDto error(String message) {
        return new ReceiptResponseDto(false, message, null);
    }
}