package org.univ.strategy.payment;

import org.univ.exception.PaymentException;

// 신용카드 결제 방식 구현체
public class CreditCardPayment implements PaymentMethod {
    private String cardNumber;   // 카드 번호
    private String expiryDate;   // 유효기간(예: "12/27")

    // 생성자에서 카드 정보 유효성 검사
    public CreditCardPayment(String cardNumber, String expiryDate) {
        if (cardNumber == null || cardNumber.length() < 12) {
            throw new IllegalArgumentException("카드 번호가 올바르지 않습니다.");
        }
        if (expiryDate == null || !expiryDate.matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("카드 유효기간 형식이 올바르지 않습니다. (예: 12/27)");
        }
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    // 결제 로직: 잔액 등은 실제 구현 대신 임의로 실패/성공 예시
    @Override
    public void pay(double amount) {
        if (amount <= 0) {
            throw new PaymentException("결제 금액이 0원 이하입니다.");
        }
        // 예시: 카드번호 뒷자리가 '00'이면 결제 실패로 가정
        if (cardNumber.endsWith("00")) {
            throw new PaymentException("카드 잔액 부족 또는 한도 초과로 결제 실패.");
        }
        // 결제 성공 (실제로는 외부 결제 연동 등)
        // 여기서는 별도 출력이나 반환 없이 정상 흐름이면 성공
    }
}
