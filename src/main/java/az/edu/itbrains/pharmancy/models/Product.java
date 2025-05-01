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
    private double priceDiscount;
    private String specification;
    private double price;
    private  int quantity;
    private Date createData;
    private Date updateData;
    private boolean featured;
    private String imageUrl;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders;
}
