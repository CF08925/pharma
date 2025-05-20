package az.edu.itbrains.pharmancy.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String name;
    private String orderingInfo;
    private String specification;
    private int quantity;
    private Date createData;
    private Date updateData;
    private boolean featured;
    private String photoUrl;

    // Changed to Long (cents)
    private Long price;          // Regular price (e.g., $19.99 = 1999)
    private Long priceDiscount;  // Discount price (e.g., $15.99 = 1599)

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders;

    // Price formatting helpers
    public String getFormattedPrice() {
        return formatCents(price);
    }

    public String getFormattedDiscountPrice() {
        return priceDiscount != null ? formatCents(priceDiscount) : "";
    }

    public Long getCurrentPrice() {
        return priceDiscount != null ? priceDiscount : price;
    }

    public boolean hasDiscount() {
        return priceDiscount != null && priceDiscount < price;
    }

    private String formatCents(Long cents) {
        return String.format("%d.%02d", cents / 100, cents % 100);
    }
}
