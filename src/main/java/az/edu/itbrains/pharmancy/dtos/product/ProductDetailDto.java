package az.edu.itbrains.pharmancy.dtos.product;


import az.edu.itbrains.pharmancy.dtos.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto {
    private Long id;
    private String name;
    private String photoUrl;
    private String orderingInfo;
    private double priceDiscount;
    private String specification;
    private String description;
    private Double price;
    private CategoryDto category;

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
