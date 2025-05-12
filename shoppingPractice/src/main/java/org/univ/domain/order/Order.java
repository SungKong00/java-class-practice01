package org.univ.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import org.univ.domain.customer.Customer;
import org.univ.domain.product.Product;
import org.univ.strategy.payment.PaymentMethod;
import org.univ.strategy.delivery.DeliveryMethod;

public class Order {

    // 필드
    private String orderId;
    private Customer customer;
    private List<Product> productList;
    private PaymentMethod paymentMethod;
    private DeliveryMethod deliveryMethod;
    private LocalDateTime orderDate;
    private OrderStatus status;

    // 생성자
    public Order(String orderId, Customer customer, List<Product> productList,
                 PaymentMethod paymentMethod, DeliveryMethod deliveryMethod) {
        // TODO: null 검증, 빈 productList 검사, 초기 상태 설정
        this.orderId = orderId;
        this.customer = customer;
        this.productList = productList;
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.orderDate = LocalDateTime.now(); // 기본값
        this.status = OrderStatus.PENDING;   // 기본값
    }

    // Getter / Setter
    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    // 메서드: 상품 추가
    public void addProduct(Product product) {
        // TODO: null 검사, 중복 검사
    }

    // 메서드: 총 주문 금액 계산
    public double calculateTotalPrice() {
        // TODO: getDiscountedPrice() 기준으로 총합 계산
        return 0;
    }

    // 메서드: 결제 수행
    public void processPayment() {
        // TODO: paymentMethod.pay(...) 호출, 예외 발생 시 처리
    }

    // 메서드: 배송 수행
    public void startDelivery() {
        // TODO: deliveryMethod.deliver(...) 호출, 예외 발생 시 처리
    }

    // 메서드: 주문 요약 문자열 반환
    public String getOrderSummary() {
        // TODO: 요약 정보 문자열로 반환
        return null;
    }
}