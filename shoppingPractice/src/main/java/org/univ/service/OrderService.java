package org.univ.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.univ.domain.customer.Customer;
import org.univ.domain.product.Product;
import org.univ.domain.order.Order;
import org.univ.exception.IllegalOrderStateException;
import org.univ.strategy.payment.PaymentMethod;
import org.univ.strategy.delivery.DeliveryMethod;

public class OrderService {

    /**
     * 주문 생성 → 상품 예약(재고 차감) → 결제 → 배송
     * 중간에 실패하면 예약된 재고를 모두 복구(롤백)
     */
    public Order placeOrder(Customer customer,
                            List<Product> products,
                            PaymentMethod paymentMethod,
                            DeliveryMethod deliveryMethod) {
        // 1. 주문 객체 생성 (빈 상품 리스트 허용)
        Order order = new Order(
                generateOrderId(),
                customer,
                List.of(),
                paymentMethod,
                deliveryMethod
        );

        // 예약된 상품 추적
        List<Product> reserved = new ArrayList<>();

        try {
            // 2. 상품 예약(addProduct가 내부에서 updateStock(-1) 호출)
            for (Product p : products) {
                order.addProduct(p);
                reserved.add(p);
            }

            // 3. 결제 처리
            order.processPayment();

            // 4. 배송 처리
            order.startDelivery();

            // 5. 최종 주문 반환 (상태가 SHIPPED)
            return order;

        } catch (Exception e) {
            // 실패 시: 예약된 재고 모두 복구
            for (Product p : reserved) {
                try {
                    p.updateStock(+1); // 복구
                } catch (Exception ignore) {}
            }
            throw new IllegalOrderStateException("주문 처리 실패: " + e.getMessage());
        }
    }

    // 주문 ID 생성 (UUID 기반)
    private String generateOrderId() {
        return "ORD-" + UUID.randomUUID();
    }
}