package org.univ.strategy.delivery;

// 배송 전략 패턴 인터페이스: 다양한 배송 방식에 대해 동일한 deliver() 호출 가능
public interface DeliveryMethod {
    // 주문 정보를 받아 배송을 처리한다. 실패 시 예외 발생
    void deliver(String productSummary);
}