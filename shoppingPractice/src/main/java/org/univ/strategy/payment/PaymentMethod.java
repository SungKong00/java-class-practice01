package org.univ.strategy.payment;

public interface PaymentMethod {
    // 주어진 금액을 결제한다. 실패 시 예외 발생
    void pay(double amount);
}
