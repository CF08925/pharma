package az.edu.itbrains.pharmancy.dtos.receipt;

import az.edu.itbrains.pharmancy.models.ReceiptStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneOperator;
    private String phoneNumber;

    private Long amount;
    private String notes;
    private List<String> imageUrls;
    private LocalDateTime uploadDate;
    private ReceiptStatus status;
    private String userEmail;

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

    public String getFormattedUploadDate() {
        if (uploadDate == null) return "";
        return uploadDate.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : "N/A";
    }

    public int getImageCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }
}
