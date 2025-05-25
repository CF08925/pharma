package az.edu.itbrains.pharmancy.controllers.dashboard;

import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptDto;
import az.edu.itbrains.pharmancy.models.Order;
import az.edu.itbrains.pharmancy.models.ReceiptStatus;
import az.edu.itbrains.pharmancy.repositories.OrderRepository;
import az.edu.itbrains.pharmancy.services.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminReceiptController {

    private final ReceiptService receiptService;
    private final OrderRepository orderRepository;

    /**
     * Show all receipts in admin panel
     */
    @GetMapping("/receipts")
    public String receipts(@RequestParam(required = false) String status, Model model) {
        List<ReceiptDto> receipts;

        if (status != null && !status.isEmpty()) {
            try {
                ReceiptStatus receiptStatus = ReceiptStatus.valueOf(status.toUpperCase());
                receipts = receiptService.getReceiptsByStatus(receiptStatus);
                model.addAttribute("selectedStatus", status);
            } catch (IllegalArgumentException e) {
                receipts = receiptService.getAllReceipts();
                model.addAttribute("selectedStatus", "");
            }
        } else {
            receipts = receiptService.getAllReceipts();
            model.addAttribute("selectedStatus", "");
        }

        model.addAttribute("receipts", receipts);
        model.addAttribute("statuses", ReceiptStatus.values());

        // Add statistics
        model.addAttribute("pendingCount", receiptService.countReceiptsByStatus(ReceiptStatus.PENDING));
        model.addAttribute("approvedCount", receiptService.countReceiptsByStatus(ReceiptStatus.APPROVED));
        model.addAttribute("rejectedCount", receiptService.countReceiptsByStatus(ReceiptStatus.REJECTED));
        model.addAttribute("processingCount", receiptService.countReceiptsByStatus(ReceiptStatus.PROCESSING));

        return "dashboard/receipts/index.html";
    }

    /**
     * Show receipt details in admin panel
     */
    // Update the receiptDetails method
    @GetMapping("/receipts/{id}")
    public String receiptDetails(@PathVariable Long id, Model model) {
        try {
            ReceiptDto receipt = receiptService.getReceiptById(id);
            model.addAttribute("receipt", receipt);
            model.addAttribute("statuses", ReceiptStatus.values());

            // Find the order associated with this receipt
            Order associatedOrder = orderRepository.findByReceiptId(id);
            if (associatedOrder != null) {
                model.addAttribute("order", associatedOrder);
            }

            return "dashboard/receipts/details.html";
        } catch (Exception e) {
            log.error("Error getting receipt details in admin: " + id, e);
            return "redirect:/admin/receipts?error=Receipt not found";
        }
    }
    /**
     * Update receipt status
     */
    @PostMapping("/receipts/{id}/status")
    public String updateReceiptStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        try {
            ReceiptStatus receiptStatus = ReceiptStatus.valueOf(status.toUpperCase());
            boolean updated = receiptService.updateReceiptStatus(id, receiptStatus);

            if (updated) {
                redirectAttributes.addFlashAttribute("success",
                        "Çek statusu uğurla yeniləndi: " + receiptStatus.getDisplayName());
            } else {
                redirectAttributes.addFlashAttribute("error", "Çek statusu yenilənərkən xəta baş verdi");
            }

        } catch (IllegalArgumentException e) {
            log.error("Invalid receipt status: " + status, e);
            redirectAttributes.addFlashAttribute("error", "Yanlış status dəyəri");
        } catch (Exception e) {
            log.error("Error updating receipt status: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Çek statusu yenilənərkən xəta baş verdi");
        }

        return "redirect:/admin/receipts/" + id;
    }

    /**
     * Delete receipt
     */
    @PostMapping("/receipts/{id}/delete")
    public String deleteReceipt(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = receiptService.deleteReceipt(id);

            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Çek uğurla silindi");
            } else {
                redirectAttributes.addFlashAttribute("error", "Çek silinərkən xəta baş verdi");
            }

        } catch (Exception e) {
            log.error("Error deleting receipt in admin: " + id, e);
            redirectAttributes.addFlashAttribute("error", "Çek silinərkən xəta baş verdi");
        }

        return "redirect:/admin/receipts";
    }

    // REMOVED: searchReceipts method - no longer needed since store name functionality is removed
}