package az.edu.itbrains.pharmancy.dtos.checkout;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productImage;
    private int quantity;
    private Long unitPrice;
    private Long unitPriceOriginal;

    public Long getSubtotal() {
        return unitPrice * quantity;
    }

    public Long getSavings() {
        return (unitPriceOriginal - unitPrice) * quantity;
    }

    // Formatted strings for display
    public String getFormattedUnitPrice() {
        return String.format("%d.%02d", unitPrice / 100, unitPrice % 100);
    }

    public String getFormattedUnitPriceOriginal() {
        return String.format("%d.%02d", unitPriceOriginal / 100, unitPriceOriginal % 100);
    }

    public String getFormattedSubtotal() {
        return String.format("%d.%02d", getSubtotal() / 100, getSubtotal() % 100);
    }

    public String getFormattedSavings() {
        return String.format("%d.%02d", getSavings() / 100, getSavings() % 100);
    }
}
