package az.edu.itbrains.pharmancy.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDate;
    private String number;

    // Add these missing fields
    private String shippingAddress;
    private String paymentMethod;
    private Long totalAmount;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Long totalSavings; // in cents (e.g., $5.00 savings = 500)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public String getFormattedTotal() {
        return String.format("%d.%02d", totalAmount / 100, totalAmount % 100);
    }


    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;



    // Add these methods
    public Long getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(Long totalSavings) {
        this.totalSavings = totalSavings;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    // Add this field after your existing fields
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    // Add these getter and setter methods
    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}
