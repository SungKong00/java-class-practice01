package org.univ.exception;

// 주문 상태 전이 로직 오류 시(예: 결제 전 배송 시도) 던지는 예외
public class IllegalOrderStateException extends RuntimeException {
    public IllegalOrderStateException(String message) {
        super(message);
    }
}