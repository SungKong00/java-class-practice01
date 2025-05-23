package org.univ.domain.customer;

import java.util.ArrayList;
import java.util.List;
import org.univ.domain.order.Order;

// 고객(Customer) 클래스: 쇼핑몰 사용자의 정보를 관리하고, 주문 내역을 보유
public class Customer {
    private String customerId;      // 고객 고유 ID
    private String name;            // 이름
    private String email;           // 이메일 주소
    private String phoneNumber;     // 연락처
    private String address;         // 배송 주소
    private List<Order> orders;     // 고객의 주문 목록

    // 생성자: 필수 정보로 고객 객체 생성
    public Customer(String customerId, String name, String email, String phoneNumber, String address) {
        // 고객 ID는 필수
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("고객 ID는 필수입니다.");
        }
        // 이름은 필수
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        // 이메일 유효성(간단히 '@' 포함만 체크, 실제로는 정규식 추천)
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        // 연락처 필수(간단 체크)
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("연락처는 필수입니다.");
        }
        // 주소 필수
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("주소는 필수입니다.");
        }

        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.orders = new ArrayList<>();
    }

    // 고객 ID 반환
    public String getCustomerId() {
        return customerId;
    }

    // 이름 반환
    public String getName() {
        return name;
    }

    // 이메일 반환
    public String getEmail() {
        return email;
    }

    // 연락처 반환
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // 주소 반환
    public String getAddress() {
        return address;
    }

    // 주문 내역 반환 (복사본)
    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    // 주소 변경 (유효성 체크)
    public void updateAddress(String newAddress) {
        if (newAddress == null || newAddress.isBlank()) {
            throw new IllegalArgumentException("새 주소는 필수입니다.");
        }
        this.address = newAddress;
    }

    // 주문 추가 (null/중복 방지)
    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("추가할 주문이 null입니다.");
        }
        if (orders.contains(order)) {
            throw new IllegalArgumentException("이미 등록된 주문입니다.");
        }
        orders.add(order);
    }

    // 주문 내역 조회(복사본 반환)
    public List<Order> getOrderHistory() {
        return new ArrayList<>(orders);
    }
}