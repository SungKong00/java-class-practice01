package org.univ.startegy.payment;

public class CreditCardPayment implements PaymentMethod {
    private String cardNumber;
    private String expiryDate;
    // CVV 필드는 구현하지 않음

    public CreditCardPayment(String cardNumber, String expiryDate) {
        // TODO: 카드번호, 만료일 유효성 검사
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    @Override
    public void pay(double amount) {
        // TODO: 카드 결제 로직, 실패 시 PaymentException 발생
    }
}
