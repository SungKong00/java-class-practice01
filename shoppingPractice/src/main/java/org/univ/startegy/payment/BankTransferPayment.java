package org.univ.startegy.payment;

public class BankTransferPayment implements PaymentMethod {
    private String bankName;
    private String accountNumber;

    public BankTransferPayment(String bankName, String accountNumber) {
        // TODO: 은행명, 계좌번호 유효성 검사
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    @Override
    public void pay(double amount) {
        // TODO: 계좌이체 로직, 실패 시 PaymentException 발생
    }
}