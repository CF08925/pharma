package az.edu.itbrains.pharmancy.dtos.product;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String description;
    private boolean featured;
    private String specification;
    private Long id;
    private String name;
    private Double price;
    private Double priceDiscount;
    private String photoUrl;

    private boolean requiresReceipt;

    // Getter and setter (or use Lombok @Data)
    public boolean isRequiresReceipt() {
        return requiresReceipt;
    }

    public void setRequiresReceipt(boolean requiresReceipt) {
        this.requiresReceipt = requiresReceipt;
    }
}
