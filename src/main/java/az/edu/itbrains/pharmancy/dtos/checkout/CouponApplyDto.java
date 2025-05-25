package az.edu.itbrains.pharmancy.dtos.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponApplyDto {
    private String couponCode;
    private boolean success;
    private String message;
    private Long discountAmount;

    // Formatted string for display
    public String getFormattedDiscount() {
        return String.format("%d.%02d", discountAmount / 100, discountAmount % 100);
    }
}
