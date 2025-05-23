package org.univ.strategy.payment;

import org.univ.exception.PaymentException;

public class BankTransferPayment implements PaymentMethod {
    private String bankName;        // 은행 이름
    private String accountNumber;   // 계좌 번호

    // 생성자에서 필수 정보 유효성 검사
    public BankTransferPayment(String bankName, String accountNumber) {
        if (bankName == null || bankName.isBlank()) {
            throw new IllegalArgumentException("은행명은 필수입니다.");
        }
        if (accountNumber == null || accountNumber.length() < 6) {
            throw new IllegalArgumentException("계좌 번호가 올바르지 않습니다.");
        }
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    // 결제 로직: 계좌번호 규칙 등 임의 예시로 처리
    @Override
    public void pay(double amount) {
        if (amount <= 0) {
            throw new PaymentException("결제 금액이 0원 이하입니다.");
        }
        // 예시: 계좌번호가 "999999"로 끝나면 결제 실패
        if (accountNumber.endsWith("999999")) {
            throw new PaymentException("계좌 잔액 부족으로 결제 실패.");
        }
        // 결제 성공 (실제로는 외부 송금 연동 등)
    }
}