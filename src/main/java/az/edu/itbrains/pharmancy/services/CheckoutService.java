package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.checkout.CheckoutDto;
import az.edu.itbrains.pharmancy.dtos.checkout.CouponApplyDto;
import az.edu.itbrains.pharmancy.dtos.checkout.OrderResponseDto;
import az.edu.itbrains.pharmancy.models.Order;

public interface CheckoutService {
    OrderResponseDto processCheckout(String userEmail, CheckoutDto checkoutDto);
    OrderResponseDto getOrderDetails(Long orderId);
    CouponApplyDto applyCoupon(String userEmail, String couponCode);
    Order findOrderById(Long orderId);
}
