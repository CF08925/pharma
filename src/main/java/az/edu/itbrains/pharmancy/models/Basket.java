package az.edu.itbrains.pharmancy.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="baskets")
public class Basket{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int  quantity;
    @ManyToOne
    private Product product;
    @ManyToOne
    private User user;
}
