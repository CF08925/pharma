package az.edu.itbrains.pharmancy.dtos.cart;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateDto {


    private Long basketId;
    private int quantity;
}
