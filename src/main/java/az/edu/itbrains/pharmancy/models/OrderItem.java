package az.edu.itbrains.pharmancy.models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private Long unitPrice;       // Price at time of purchase
    private Long unitPriceOriginal; // Original price before discount

    // Calculates final price for this line item
    public Long getSubtotal() {
        return unitPrice * quantity;
    }

    // Calculates total savings for this line item
    public Long getSavings() {
        return (unitPriceOriginal - unitPrice) * quantity;
    }
}
