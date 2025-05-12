package org.univ.domain.order;

public enum OrderStatus {
    PENDING("결제 대기"),
    PAID("결제 완료"),
    SHIPPED("배송 중"),
    DELIVERED("배송 완료"),
    CANCELLED("취소됨");

    private final String description;  // 각 상수에 부여할 설명 메시지

    // 생성자 - 각 enum 상수 생성 시 실행됨
    OrderStatus(String description) {
        this.description = description;
    }

    // 설명을 반환하는 메서드
    public String getDescription() {
        return description;
    }
}

