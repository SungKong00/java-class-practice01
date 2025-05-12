package org.univ.domain.customer;

import java.util.ArrayList;
import java.util.List;
import org.univ.domain.order.Order;

public class Customer {

    // 필드
    private String customerId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private List<Order> orders;

    // 생성자
    public Customer(String customerId, String name, String email, String phoneNumber, String address) {
        // TODO: 필수 입력값 검증 로직 추가 예정
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orders = new ArrayList<>();
    }

    // Getter
    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public List<Order> getOrders() {
        return orders;
    }

    // 메서드: 주소 변경
    public void updateAddress(String newAddress) {
        // TODO: newAddress 유효성 검증 필요
        this.address = newAddress;
    }

    // 메서드: 주문 추가
    public void addOrder(Order order) {
        // TODO: null 검사, 중복 검사 등 유효성 검증 고려
        this.orders.add(order);
    }

    // 메서드: 주문 내역 조회
    public List<Order> getOrderHistory() {
        return new ArrayList<>(orders); // 방어적 복사
    }
}