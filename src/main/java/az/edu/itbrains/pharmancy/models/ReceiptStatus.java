package az.edu.itbrains.pharmancy.models;

public enum ReceiptStatus {
    PENDING("Gözləyir"),
    APPROVED("Təsdiqlənib"),
    REJECTED("Rədd edilib"),
    PROCESSING("İşlənir");

    private final String displayName;

    ReceiptStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}