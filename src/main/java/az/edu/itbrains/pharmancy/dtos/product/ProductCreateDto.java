package az.edu.itbrains.pharmancy.dtos.product;


import az.edu.itbrains.pharmancy.dtos.category.CategoryDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {

    private String name;
    private String description;
    private String specification;
    private double price;
    private double priceDiscount;
    private boolean featured;
    private String photoUrl;

    @NotNull(message = "Category is required")  // Added validation
    private Long categoryId;  // Changed from Category to Long

    // Getters & Setters (make sure to update for categoryId)
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

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
