package az.edu.itbrains.pharmancy.dtos.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBasketDto {


    private Long id;
    private String name;
    private String photoUrl;
    private Double price;
    private Double priceDiscount;

    // Add this field after your existing fields
    private boolean requiresReceipt;

    // Getter and setter (or use Lombok @Data)
    public boolean isRequiresReceipt() {
        return requiresReceipt;
    }

    public void setRequiresReceipt(boolean requiresReceipt) {
        this.requiresReceipt = requiresReceipt;
    }
}
