package az.edu.itbrains.pharmancy.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRelatedDto {
    private Long id;
    private String name;
    private Double price;
    private Double priceDiscount;
    private String photoUrl;
}
