package az.edu.itbrains.pharmancy.dtos.product;


import az.edu.itbrains.pharmancy.dtos.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {

    private String name;
    private double priceDiscount;
    private String specification;
    private String description;
    private Double price;
    private CategoryDto category;
}
