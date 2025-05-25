package az.edu.itbrains.pharmancy.dtos.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private Long totalAmount;
    private Long totalSavings;
    private String paymentMethod;
    private String shippingAddress;
    private String status;

    private List<OrderItemDto> items;

    // Formatted string for display
    public String getFormattedTotal() {
        return String.format("%d.%02d", totalAmount / 100, totalAmount % 100);
    }

    public String getFormattedSavings() {
        return String.format("%d.%02d", totalSavings / 100, totalSavings % 100);
    }
}