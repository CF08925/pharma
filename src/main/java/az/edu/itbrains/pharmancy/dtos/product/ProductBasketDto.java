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
}
