package az.edu.itbrains.pharmancy.controllers;

import az.edu.itbrains.pharmancy.dtos.basket.BasketDto;
import az.edu.itbrains.pharmancy.dtos.checkout.CheckoutDto;
import az.edu.itbrains.pharmancy.models.PaymentMethod;
import az.edu.itbrains.pharmancy.services.BasketService;
import az.edu.itbrains.pharmancy.services.CheckoutService;
import az.edu.itbrains.pharmancy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutController {
    private final BasketService basketService;
    private final CheckoutService checkoutService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/checkout")
    @PreAuthorize("isAuthenticated()")
    public String checkout(Model model) {
        try {
            // Get current authenticated user's email
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = auth.getName();

            // Check if user is actually authenticated (not anonymous)
            if (userEmail.equals("anonymousUser") || !auth.isAuthenticated()) {
                return "redirect:/login?returnUrl=/checkout";
            }

            // Verify user exists in database
            var user = userService.findUserByEmail(userEmail);
            if (user == null) {
                model.addAttribute("error", "User account not found. Please contact support.");
                return "error";
            }

            // Get basket items
            List<BasketDto> basketDtoList = basketService.getUserBaskets();

            // Check if any products require receipts
            boolean hasReceiptRequiredProducts = basketDtoList.stream()
                    .anyMatch(item -> item.getProduct().isRequiresReceipt());

            // Calculate total price
            double totalPrice = 0;
            for (BasketDto basket : basketDtoList) {
                totalPrice += basket.getTotalPrice();
            }

            // Add attributes to model
            model.addAttribute("baskets", basketDtoList);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("hasReceiptRequiredProducts", hasReceiptRequiredProducts);
            model.addAttribute("checkoutDto", new CheckoutDto());
            model.addAttribute("userEmail", userEmail);

            return "checkout";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading checkout page: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/checkout/process")
    @PreAuthorize("isAuthenticated()")
    public String processCheckout(@ModelAttribute CheckoutDto checkoutDto,
                                  @RequestParam(value = "receiptImages", required = false) List<MultipartFile> receiptImages,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Get the authentication object from security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            // Check if user is actually authenticated (not anonymous)
            if (userEmail.equals("anonymousUser") || !authentication.isAuthenticated()) {
                return "redirect:/login?returnUrl=/checkout";
            }

            // Verify user exists before proceeding
            var user = userService.findUserByEmail(userEmail);
            if (user == null) {
                model.addAttribute("error", "User account not found. Please login again.");
                return "redirect:/login";
            }

            // Set receipt images in DTO
            checkoutDto.setReceiptImages(receiptImages);

            // Set user information from database to checkout DTO
            checkoutDto.setEmail(userEmail);
            if (checkoutDto.getFirstName() == null || checkoutDto.getFirstName().isEmpty()) {
                checkoutDto.setFirstName(user.getFirstName());
            }
            if (checkoutDto.getLastName() == null || checkoutDto.getLastName().isEmpty()) {
                checkoutDto.setLastName(user.getLastName());
            }

            // Set default payment method if not selected
            if (checkoutDto.getPaymentMethod() == null) {
                checkoutDto.setPaymentMethod(PaymentMethod.card);
            }

            // Process the checkout
            var orderResponse = checkoutService.processCheckout(userEmail, checkoutDto);

            // Redirect to confirmation page
            redirectAttributes.addAttribute("orderId", orderResponse.getId());
            return "redirect:/thank-you";
        } catch (Exception e) {
            // If error, return to checkout page with error message
            model.addAttribute("error", "Error processing order: " + e.getMessage());
            try {
                List<BasketDto> basketDtoList = basketService.getUserBaskets();
                boolean hasReceiptRequiredProducts = basketDtoList.stream()
                        .anyMatch(item -> item.getProduct().isRequiresReceipt());
                model.addAttribute("baskets", basketDtoList);
                model.addAttribute("hasReceiptRequiredProducts", hasReceiptRequiredProducts);
            } catch (Exception ex) {
                // Do nothing if we can't get baskets
            }
            return "checkout";
        }
    }

    @GetMapping("/thank-you")
    public String thankYou(@RequestParam(required = false) Long orderId, Model model) {
        try {
            if (orderId != null) {
                var orderDetails = checkoutService.getOrderDetails(orderId);
                model.addAttribute("order", orderDetails);
            }
            return "thank-you";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading thank you page: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/checkout/apply-coupon")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String applyCoupon(@RequestParam String couponCode) {
        try {
            // Get the authentication object from security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();

            // Check if user is actually authenticated (not anonymous)
            if (userEmail.equals("anonymousUser") || !authentication.isAuthenticated()) {
                return "Please log in to apply a coupon";
            }

            // Verify user exists
            var user = userService.findUserByEmail(userEmail);
            if (user == null) {
                return "Error: User not found. Please log in again.";
            }

            var response = checkoutService.applyCoupon(userEmail, couponCode);
            return response.isSuccess() ?
                    "Success! Discount: $" + response.getFormattedDiscount() :
                    "Error: " + response.getMessage();
        } catch (Exception e) {
            return "Error applying coupon: " + e.getMessage();
        }
    }
}