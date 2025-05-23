package org.univ.service;

import java.util.List;
import org.univ.domain.customer.Customer;
import org.univ.domain.product.Product;
import org.univ.domain.order.Order;
import org.univ.strategy.payment.PaymentMethod;
import org.univ.strategy.delivery.DeliveryMethod;
import org.univ.exception.IllegalOrderStateException;


public class OrderService {

    // 주문 생성 및 결제/배송 처리
    public Order placeOrder(Customer customer,
                            List<Product> products,
                            PaymentMethod paymentMethod,
                            DeliveryMethod deliveryMethod) {
        // 1. 주문 객체 생성 (빈 상품 리스트 허용)
        Order order = new Order(
                generateOrderId(),   // 주문 ID 생성 로직 (예: UUID)
                customer,
                List.of(),           // 일단 빈 리스트로 생성
                paymentMethod,
                deliveryMethod
        );

        // 2. 상품 모두 추가 (재고 차감 및 목록에 포함)
        for (Product p : products) {
            // TODO: 상품 단위 예외 처리 필요 시 개별 try/catch 구성
            order.addProduct(p);
        }

        // 3. 결제 처리
        try {
            order.processPayment();
        } catch (Exception e) {
            // 결제 실패 시 프로세스 중단
            throw new IllegalOrderStateException("결제 과정 실패: " + e.getMessage());
        }

        // 4. 배송 처리
        try {
            order.startDelivery();
        } catch (Exception e) {
            // 배송 실패 시 프로세스 중단 (필요 시 롤백 로직 추가)
            throw new IllegalOrderStateException("배송 과정 실패: " + e.getMessage());
        }

        // 최종적으로 배송 요청까지 성공한 주문 반환
        return order;
    }

    // 주문 ID 생성 로직 (예: UUID, 시퀀스 등)
    private String generateOrderId() {
        return "ORD-" + System.currentTimeMillis();
    }
}