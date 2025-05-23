package org.univ.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import org.univ.domain.customer.Customer;
import org.univ.domain.product.Product;
import org.univ.startegy.delivery.DeliveryMethod;
import org.univ.startegy.payment.PaymentMethod;


package org.univ.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.univ.domain.customer.Customer;
import org.univ.domain.product.Product;
import org.univ.strategy.payment.PaymentMethod;
import org.univ.strategy.delivery.DeliveryMethod;
import org.univ.exception.StockUnavailableException;
import org.univ.exception.PaymentException;
import org.univ.exception.DeliveryException;

// 주문(Order) 클래스: 주문 생성, 상품 추가, 결제, 배송, 상태 관리 담당
public class Order {
    // 주문 고유 ID
    private String orderId;
    // 주문한 고객 정보
    private Customer customer;
    // 주문한 상품 목록
    private List<Product> productList;
    // 결제 방식(전략 패턴)
    private PaymentMethod paymentMethod;
    // 배송 방식(전략 패턴)
    private DeliveryMethod deliveryMethod;
    // 주문 생성 시각
    private LocalDateTime orderDate;
    // 주문 상태 (예: 결제대기, 결제완료, 배송중 등)
    private OrderStatus status;

    // 생성자: 주문 정보 필수값으로 초기화, 상태/시간 기본값 세팅
    public Order(String orderId, Customer customer, List<Product> productList,
                 PaymentMethod paymentMethod, DeliveryMethod deliveryMethod) {
        // 필수 입력값 유효성 검증
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("주문 ID는 필수입니다.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("고객 정보가 없습니다.");
        }
        if (productList == null || productList.isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 없습니다.");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("결제 방식이 지정되지 않았습니다.");
        }
        if (deliveryMethod == null) {
            throw new IllegalArgumentException("배송 방식이 지정되지 않았습니다.");
        }
        this.orderId = orderId;
        this.customer = customer;
        this.productList = new ArrayList<>(productList);
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING; // 기본값: 결제 대기
    }

    // 주문에 상품을 추가한다.
    public void addProduct(Product product) {
        // null 체크
        if (product == null) {
            throw new IllegalArgumentException("추가할 상품이 null입니다.");
        }
        // 이미 주문에 같은 상품이 있으면 예외 (정책상 허용하려면 이 부분 주석처리)
        if (productList.contains(product)) {
            throw new IllegalArgumentException("이미 주문에 포함된 상품입니다.");
        }
        // 재고 확인 후 차감
        try {
            product.updateStock(-1); // 상품 1개 출고
        } catch (StockUnavailableException e) {
            throw new StockUnavailableException("상품 [" + product.getName() + "] 재고 부족: " + e.getMessage());
        }
        // 상품 목록에 추가
        productList.add(product);
    }

    // 주문 전체 금액(할인 적용 후)을 계산한다.
    public double calculateTotalPrice() {
        if (productList == null || productList.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Product product : productList) {
            total += product.getDiscountedPrice(); // 각 상품의 할인 적용 가격 합산
        }
        return total;
    }

    // 결제 처리를 수행한다.
    public void processPayment() {
        // 이미 결제된 상태면 중복 결제 방지
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("결제 가능한 상태가 아닙니다. 현재 상태: " + status.getDescription());
        }
        double totalAmount = calculateTotalPrice();
        if (totalAmount <= 0) {
            throw new IllegalStateException("결제할 금액이 0원 이하입니다.");
        }
        try {
            paymentMethod.pay(totalAmount); // 결제 전략에 실제 결제 요청
        } catch (PaymentException e) {
            throw new PaymentException("결제 실패: " + e.getMessage());
        }
        // 결제 성공 시 상태 변경
        status = OrderStatus.PAID;
    }

    // 배송 처리를 수행한다.
    public void startDelivery() {
        // 결제가 안된 상태면 배송 불가
        if (status != OrderStatus.PAID) {
            throw new IllegalStateException("결제 완료 상태에서만 배송이 가능합니다. 현재 상태: " + status.getDescription());
        }
        // 배송 요청 정보(간단히 주문 요약) 생성
        String summary = getOrderSummary();
        try {
            deliveryMethod.deliver(summary); // 배송 전략에 실제 배송 요청
        } catch (DeliveryException e) {
            throw new DeliveryException("배송 실패: " + e.getMessage());
        }
        // 배송 성공 시 상태 변경
        status = OrderStatus.SHIPPED; // 단순화: 실제로는 배송 완료 등 더 세분화 가능
    }

    // 주문 요약 정보를 문자열로 반환한다.
    public String getOrderSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("주문ID: ").append(orderId).append(", 고객: ").append(customer.getName()).append("\n");
        sb.append("상품목록: ");
        for (Product product : productList) {
            sb.append(product.getName()).append("(").append(product.getDescription()).append("), ");
        }
        sb.append("\n주문상태: ").append(status.getDescription());
        return sb.toString();
    }

    // Getter 메서드들
    public String getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<Product> getProductList() { return new ArrayList<>(productList); }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public DeliveryMethod getDeliveryMethod() { return deliveryMethod; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public OrderStatus getStatus() { return status; }
}