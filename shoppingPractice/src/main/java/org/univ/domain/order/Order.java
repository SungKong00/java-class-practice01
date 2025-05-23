package org.univ.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import org.univ.domain.customer.Customer;
import org.univ.domain.product.Product;
import org.univ.strategy.delivery.DeliveryMethod;
import org.univ.strategy.payment.PaymentMethod;
import java.util.ArrayList;
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
    public Order(String orderId,
                 Customer customer,
                 List<Product> productList,
                 PaymentMethod paymentMethod,
                 DeliveryMethod deliveryMethod) {
        // 필수값 null/blank 체크
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("주문 ID는 필수입니다.");
        }
        if (customer == null) {
            throw new IllegalArgumentException("고객 정보가 없습니다.");
        }
        if (productList == null) {               // null만 막고, isEmpty()는 제거!
            throw new IllegalArgumentException("주문 상품 목록이 null입니다.");
        }
        if (paymentMethod == null) {
            throw new IllegalArgumentException("결제 방식이 지정되지 않았습니다.");
        }
        if (deliveryMethod == null) {
            throw new IllegalArgumentException("배송 방식이 지정되지 않았습니다.");
        }

        this.orderId = orderId;
        this.customer = customer;
        this.productList = new ArrayList<>(productList); // 빈 리스트도 허용됨
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }

    // 주문에 상품을 추가한다.
    public void addProduct(Product product) {
        // null 체크
        if (product == null) {
            throw new IllegalArgumentException("상품이 null입니다.");
        }
        // 상품의 재고를 1개 차감 (음수 요청)
        product.updateStock(-1);
        // 상품을 주문 리스트에 추가
        this.productList.add(product);
    }

    // 주문 전체 금액(할인 적용 후)을 계산한다.
    public double calculateTotalPrice() {
        // 빈 목록일 때 0.0 바로 리턴
        if (productList == null || productList.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        // 각 상품의 할인 가격을 합산
        for (Product product : productList) {
            total += product.getDiscountedPrice();
        }
        return total;
    }

    // 결제 처리를 수행한다.
    public void processPayment() {
        // 주문 상태가 결제 대기(PENDING)가 아니면 중복 결제 방지
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "결제 가능한 상태가 아닙니다. 현재 상태: " + status.getDescription()
            );
        }

        // 총 결제 금액 계산
        double totalAmount = calculateTotalPrice();

        // 총 금액이 0원이거나 음수이면 결제 불가
        if (totalAmount <= 0.0) {
            throw new IllegalStateException("결제할 금액이 0원 이하입니다.");
        }

        // 실제 결제 시도
        try {
            paymentMethod.pay(totalAmount);
        } catch (PaymentException e) {
            // 결제 실패 시 예외 메시지 보강 후 재던짐
            throw new PaymentException("결제 실패: " + e.getMessage());
        }

        // 결제 성공 시 주문 상태를 PAID로 변경
        status = OrderStatus.PAID;
    }

    // 배송 처리를 수행한다.
    public void startDelivery() {
        // 배송 가능한 상태인지 확인 (결제 완료 PAID 상태여야 함)
        if (status != OrderStatus.PAID) {
            throw new IllegalStateException(
                    "결제 완료된 주문만 배송 가능. 현재 상태: " + status.getDescription()
            );
        }

        // 배송 요청에 사용할 주문 요약 정보 생성
        String summary = getOrderSummary();

        // 실제 배송 시도
        try {
            deliveryMethod.deliver(summary);
        } catch (DeliveryException e) {
            // 배송 실패 시 예외 메시지 보강 후 재던짐
            throw new DeliveryException("배송 실패: " + e.getMessage());
        }

        // 배송 성공 시 주문 상태를 SHIPPED로 변경
        status = OrderStatus.SHIPPED;
    }

    // 주문 요약 정보를 문자열로 반환한다.
    public String getOrderSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("주문ID: ").append(orderId)
                .append(", 고객: ").append(customer.getName())
                .append(", 주소: ").append(customer.getAddress());

        if (productList != null && !productList.isEmpty()) {
            sb.append(", 상품: ");
            for (Product p : productList) {
                sb.append(p.getName()).append(" ");
            }
        }

        return sb.toString().trim();
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