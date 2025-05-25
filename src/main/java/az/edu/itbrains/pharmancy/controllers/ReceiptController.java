package az.edu.itbrains.pharmancy.controllers;

import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptCreateDto;
import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptResponseDto;
import az.edu.itbrains.pharmancy.services.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping("/receipt")
    public String showReceiptPage(Model model) {
        model.addAttribute("receiptCreateDto", new ReceiptCreateDto());
        return "receipt.html";
    }

    @PostMapping("/receipt/upload")
    public String uploadReceipt(
            @ModelAttribute ReceiptCreateDto receiptCreateDto,
            @RequestParam(value = "receiptImages", required = false) List<MultipartFile> images,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            // Get user email from authentication
            String userEmail = authentication.getName();

            log.info("Receipt upload attempt by user: {} - Customer: {} {}",
                    userEmail, receiptCreateDto.getFirstName(), receiptCreateDto.getLastName());

            // Use the actual receipt service to upload
            ReceiptResponseDto response = receiptService.uploadReceipt(userEmail, receiptCreateDto, images);

            if (response.isSuccess()) {
                redirectAttributes.addFlashAttribute("success", response.getMessage());
                log.info("Receipt uploaded successfully: ID {}", response.getReceiptId());
            } else {
                redirectAttributes.addFlashAttribute("error", response.getMessage());
                log.warn("Receipt upload failed: {}", response.getMessage());
            }

        } catch (Exception e) {
            log.error("Error uploading receipt for user: " + authentication.getName(), e);
            redirectAttributes.addFlashAttribute("error", "Çek yükləmə zamanı xəta baş verdi: " + e.getMessage());
        }

        return "redirect:/receipt";
    }
}