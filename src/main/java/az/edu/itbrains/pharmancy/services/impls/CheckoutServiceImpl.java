package az.edu.itbrains.pharmancy.services.impls;

import az.edu.itbrains.pharmancy.dtos.basket.BasketDto;
import az.edu.itbrains.pharmancy.dtos.checkout.CheckoutDto;
import az.edu.itbrains.pharmancy.dtos.checkout.CouponApplyDto;
import az.edu.itbrains.pharmancy.dtos.checkout.OrderItemDto;
import az.edu.itbrains.pharmancy.dtos.checkout.OrderResponseDto;
import az.edu.itbrains.pharmancy.models.*;
import az.edu.itbrains.pharmancy.repositories.OrderItemRepository;
import az.edu.itbrains.pharmancy.repositories.OrderRepository;
import az.edu.itbrains.pharmancy.repositories.ShippingAddressRepository;
import az.edu.itbrains.pharmancy.services.BasketService;
import az.edu.itbrains.pharmancy.services.CheckoutService;
import az.edu.itbrains.pharmancy.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptCreateDto;
import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptResponseDto;
import az.edu.itbrains.pharmancy.services.ReceiptService;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final BasketService basketService;
    private final UserService userService;
    private final ReceiptService receiptService; // ADD THIS LINE
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderResponseDto processCheckout(String userEmail, CheckoutDto checkoutDto) {
        try {
            // Find the user
            User user = userService.findUserByEmail(userEmail);
            if (user == null) {
                throw new EntityNotFoundException("User not found");
            }

            // Get user's basket items
            List<BasketDto> basketItems = basketService.getUserBaskets();
            if (basketItems.isEmpty()) {
                throw new IllegalStateException("Cannot checkout with empty basket");
            }

            // Check if any products require receipts
            boolean hasReceiptRequiredProducts = basketItems.stream()
                    .anyMatch(item -> item.getProduct().isRequiresReceipt());

            // If receipt is required but not provided, throw error
            if (hasReceiptRequiredProducts &&
                    (checkoutDto.getReceiptImages() == null || checkoutDto.getReceiptImages().isEmpty())) {
                throw new IllegalStateException("Receipt is required for some products in your cart");
            }

            // Create or update shipping address
            ShippingAddress shippingAddress = createShippingAddress(user, checkoutDto);

            // Create the order
            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setNumber(generateOrderNumber());
            order.setStatus(OrderStatus.PENDING);

            // Set address information
            order.setAddress(shippingAddress.getStreet());
            order.setCity(shippingAddress.getCity());
            order.setState(shippingAddress.getState());
            order.setZipCode(shippingAddress.getZipCode());
            order.setShippingAddress(
                    shippingAddress.getStreet() + ", " +
                            shippingAddress.getCity() + ", " +
                            shippingAddress.getState() + " " +
                            shippingAddress.getZipCode()
            );
            order.setPaymentMethod(checkoutDto.getPaymentMethod().toString());

            // Handle receipt upload if needed
            if (hasReceiptRequiredProducts && checkoutDto.getReceiptImages() != null &&
                    !checkoutDto.getReceiptImages().isEmpty()) {

                ReceiptCreateDto receiptCreateDto = new ReceiptCreateDto();
                receiptCreateDto.setFirstName(checkoutDto.getReceiptFirstName() != null ?
                        checkoutDto.getReceiptFirstName() : checkoutDto.getFirstName());
                receiptCreateDto.setLastName(checkoutDto.getReceiptLastName() != null ?
                        checkoutDto.getReceiptLastName() : checkoutDto.getLastName());
                receiptCreateDto.setPhoneOperator(checkoutDto.getReceiptPhoneOperator());
                receiptCreateDto.setPhoneNumber(checkoutDto.getReceiptPhoneNumber());
                receiptCreateDto.setNotes(checkoutDto.getReceiptNotes());

                ReceiptResponseDto receiptResponse = receiptService.uploadReceipt(
                        userEmail, receiptCreateDto, checkoutDto.getReceiptImages());

                if (receiptResponse.isSuccess()) {
                    Receipt receipt = receiptService.findReceiptById(receiptResponse.getReceiptId());
                    order.setReceipt(receipt);
                }
            }

            // Calculate total amount - convert double to Long (cents)
            Long totalAmount = 0L;

            // Save the order to get an ID
            order = orderRepository.save(order);

            // Create order items from basket
            List<OrderItem> orderItems = new ArrayList<>();
            for (BasketDto basketItem : basketItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(modelMapper.map(basketItem.getProduct(), Product.class));
                orderItem.setQuantity(basketItem.getQuantity());

                // Convert double price to cents (long)
                Long unitPrice = (long)(basketItem.getProduct().getPrice() * 100);

                orderItem.setUnitPrice(unitPrice);
                orderItem.setUnitPriceOriginal(unitPrice); // Same as unit price if no discount

                orderItems.add(orderItem);

                // Calculate totals
                totalAmount += (unitPrice * basketItem.getQuantity());
            }

            // Save order items
            orderItemRepository.saveAll(orderItems);

            // Update order with totals
            order.setTotalAmount(totalAmount);
            order.setTotalSavings(0L); // No savings if no discounts
            order = orderRepository.save(order);

            // Clear basket after successful checkout
            basketService.clearUserBasket(userEmail);

            // Return order response
            return convertToOrderResponseDto(order, orderItems);
        } catch (Exception e) {
            // Log the error or handle it as needed
            throw new RuntimeException("Error processing checkout: " + e.getMessage(), e);
        }
    }

    @Override
    public OrderResponseDto getOrderDetails(Long orderId) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(orderId);

            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
                return convertToOrderResponseDto(order, orderItems);
            } else {
                throw new EntityNotFoundException("Order not found with ID: " + orderId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error getting order details: " + e.getMessage(), e);
        }
    }

    @Override
    public CouponApplyDto applyCoupon(String userEmail, String couponCode) {
        try {
            // This is a simplified implementation - in a real app, you would check against a database
            CouponApplyDto response = new CouponApplyDto();
            response.setCouponCode(couponCode);

            // Example validation (replace with actual validation logic)
            if (couponCode.equals("WELCOME10")) {
                response.setSuccess(true);
                response.setMessage("10% discount applied successfully");
                response.setDiscountAmount(1000L); // $10.00
            } else {
                response.setSuccess(false);
                response.setMessage("Invalid coupon code");
                response.setDiscountAmount(0L);
            }

            return response;
        } catch (Exception e) {
            CouponApplyDto errorResponse = new CouponApplyDto();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Error applying coupon: " + e.getMessage());
            errorResponse.setDiscountAmount(0L);
            return errorResponse;
        }
    }

    @Override
    public Order findOrderById(Long orderId) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(orderId);

            if (orderOptional.isPresent()) {
                return orderOptional.get();
            } else {
                throw new EntityNotFoundException("Order not found with ID: " + orderId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding order: " + e.getMessage(), e);
        }
    }

    private ShippingAddress createShippingAddress(User user, CheckoutDto checkoutDto) {
        try {
            // Create a new shipping address
            ShippingAddress shippingAddress = new ShippingAddress();
            shippingAddress.setUser(user);
            shippingAddress.setStreet(checkoutDto.getStreet());
            shippingAddress.setApartment(checkoutDto.getApartment());
            shippingAddress.setCity(checkoutDto.getCity());
            shippingAddress.setState(checkoutDto.getState());
            shippingAddress.setZipCode(checkoutDto.getZipCode());
            shippingAddress.setCountry(checkoutDto.getCountry());

            return shippingAddressRepository.save(shippingAddress);
        } catch (Exception e) {
            throw new RuntimeException("Error creating shipping address: " + e.getMessage(), e);
        }
    }

    private String generateOrderNumber() {
        // Generate a unique order number (prefix + timestamp + random)
        return "ORD-" + System.currentTimeMillis() + "-"
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderResponseDto convertToOrderResponseDto(Order order, List<OrderItem> orderItems) {
        try {
            OrderResponseDto responseDto = new OrderResponseDto();
            responseDto.setId(order.getId());
            responseDto.setOrderNumber(order.getNumber());
            responseDto.setOrderDate(order.getOrderDate());
            responseDto.setTotalAmount(order.getTotalAmount());
            responseDto.setTotalSavings(order.getTotalSavings());
            responseDto.setPaymentMethod(order.getPaymentMethod());
            responseDto.setShippingAddress(order.getShippingAddress());
            responseDto.setStatus(order.getStatus().toString());

            // Convert order items to DTOs
            List<OrderItemDto> itemDtos = orderItems.stream()
                    .map(item -> {
                        OrderItemDto dto = new OrderItemDto();
                        dto.setProductId(item.getProduct().getId());
                        dto.setProductName(item.getProduct().getName());
                        dto.setQuantity(item.getQuantity());
                        dto.setUnitPrice(item.getUnitPrice());
                        dto.setUnitPriceOriginal(item.getUnitPriceOriginal());
                        return dto;
                    })
                    .collect(Collectors.toList());

            responseDto.setItems(itemDtos);

            return responseDto;
        } catch (Exception e) {
            throw new RuntimeException("Error converting order to DTO: " + e.getMessage(), e);
        }
    }
}