package org.univ.exception;

// 배송 불가, 주소 오류 등 모든 배송 관련 예외 처리용
public class DeliveryException extends RuntimeException {
    public DeliveryException(String message) {
        super(message);
    }
}
