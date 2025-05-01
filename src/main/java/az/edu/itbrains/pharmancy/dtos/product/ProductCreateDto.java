package az.edu.itbrains.pharmancy.dtos.product;


import az.edu.itbrains.pharmancy.dtos.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {

    private String description;
    private boolean featured;
    private String specification;
    private String name;
    private Double price;
    private Double priceDiscount;
    private String photoUrl;
}
