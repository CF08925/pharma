package az.edu.itbrains.pharmancy.dtos.basket;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketAddDto {

    private int quantity;
    private Long productId;
}
