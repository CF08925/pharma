package az.edu.itbrains.pharmancy.dtos.checkout;

import az.edu.itbrains.pharmancy.models.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutDto {
    // Customer information
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    // Address information
    private String street;
    private String apartment;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    // Payment information
    private PaymentMethod paymentMethod;

    // Order information
    private String couponCode;
    private String orderNotes;
    private boolean createAccount;
    private String password; // Only if createAccount is true

    // Add these fields after your existing fields
    private boolean hasReceiptRequiredProducts;
    private List<MultipartFile> receiptImages;
    private String receiptNotes;
    private String receiptFirstName;
    private String receiptLastName;
    private String receiptPhoneOperator;
    private String receiptPhoneNumber;

    // Add getters and setters (or use Lombok @Data)
    public boolean isHasReceiptRequiredProducts() {
        return hasReceiptRequiredProducts;
    }

    public void setHasReceiptRequiredProducts(boolean hasReceiptRequiredProducts) {
        this.hasReceiptRequiredProducts = hasReceiptRequiredProducts;
    }

    public List<MultipartFile> getReceiptImages() {
        return receiptImages;
    }

    public void setReceiptImages(List<MultipartFile> receiptImages) {
        this.receiptImages = receiptImages;
    }

    public String getReceiptNotes() {
        return receiptNotes;
    }

    public void setReceiptNotes(String receiptNotes) {
        this.receiptNotes = receiptNotes;
    }

    public String getReceiptFirstName() {
        return receiptFirstName;
    }

    public void setReceiptFirstName(String receiptFirstName) {
        this.receiptFirstName = receiptFirstName;
    }

    public String getReceiptLastName() {
        return receiptLastName;
    }

    public void setReceiptLastName(String receiptLastName) {
        this.receiptLastName = receiptLastName;
    }

    public String getReceiptPhoneOperator() {
        return receiptPhoneOperator;
    }

    public void setReceiptPhoneOperator(String receiptPhoneOperator) {
        this.receiptPhoneOperator = receiptPhoneOperator;
    }

    public String getReceiptPhoneNumber() {
        return receiptPhoneNumber;
    }

    public void setReceiptPhoneNumber(String receiptPhoneNumber) {
        this.receiptPhoneNumber = receiptPhoneNumber;
    }
}