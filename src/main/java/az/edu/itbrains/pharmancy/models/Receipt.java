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
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneOperator;  // 010, 012, 050, etc.

    @Column(nullable = false)
    private String phoneNumber;    // 7-digit number



    private Long amount;  // Amount in cents (e.g., $19.99 = 1999)

    @Column(length = 1000)
    private String notes;

    @ElementCollection
    @CollectionTable(name = "receipt_images", joinColumns = @JoinColumn(name = "receipt_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @Enumerated(EnumType.STRING)
    private ReceiptStatus status = ReceiptStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFullPhoneNumber() {
        return phoneOperator + phoneNumber;
    }

    public String getFormattedAmount() {
        if (amount == null) return "N/A";
        return String.format("%.2f AZN", amount / 100.0);
    }

    public int getImageCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }
}