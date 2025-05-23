package org.univ.startegy.payment;

import org.univ.exception.PaymentException;

public interface PaymentMethod {
    /**
     * 결제를 수행한다.
     * @param amount 결제 금액
     * @throws PaymentException 결제 실패 시 예외 발생
     */
    void pay(double amount); // 결제 성공/실패는 예외로 구분
}
