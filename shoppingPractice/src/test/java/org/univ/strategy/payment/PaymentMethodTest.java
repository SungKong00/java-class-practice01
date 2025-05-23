package org.univ.strategy.payment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.univ.exception.PaymentException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PaymentMethod 구현체 동작 검증")
class PaymentMethodTest {

    @Nested
    @DisplayName("CreditCardPayment 생성자 유효성 검사")
    class CreditCardConstructorValidation {

        @ParameterizedTest
        @ValueSource(strings = {"", "12345678901"})
        @DisplayName("잘못된 카드번호 길이로 생성 시 IllegalArgumentException 발생")
        void throwWhenCardNumberInvalid(String badCardNumber) {
            // given
            String expiryDate = "12/27";

            // when / then
            assertThrows(IllegalArgumentException.class,
                    () -> new CreditCardPayment(badCardNumber, expiryDate),
                    "카드번호가 짧으면 IllegalArgumentException이 발생해야 한다");
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "1227", "ab/cd"})
        @DisplayName("유효기간 형식이 올바르지 않으면 IllegalArgumentException 발생")
        void throwWhenExpiryDateInvalid(String badExpiry) {
            // given
            String cardNumber = "123456789012";

            // when / then
            assertThrows(IllegalArgumentException.class,
                    () -> new CreditCardPayment(cardNumber, badExpiry),
                    "유효기간 형식이 MM/YY가 아니면 IllegalArgumentException이 발생해야 한다");
        }
    }

    @Nested
    @DisplayName("CreditCardPayment.pay 메서드 동작")
    class CreditCardPayTests {

        @Test
        @DisplayName("양수 금액으로 결제 시 예외 없이 통과한다")
        void paySuccess() {
            // given
            CreditCardPayment cc = new CreditCardPayment("123456789012", "12/27");
            double amount = 10000;

            // when / then
            assertDoesNotThrow(() -> cc.pay(amount),
                    "양수 금액으로 pay 호출 시 예외가 발생하면 안 된다");
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, -100.0})
        @DisplayName("0 이하 금액으로 결제 시 PaymentException 발생")
        void throwWhenNonPositiveAmount(double badAmount) {
            // given
            CreditCardPayment cc = new CreditCardPayment("123456789012", "12/27");

            // when / then
            assertThrows(PaymentException.class,
                    () -> cc.pay(badAmount),
                    "0원이하 금액으로 pay 호출 시 PaymentException이 발생해야 한다");
        }

        @Test
        @DisplayName("카드번호가 '00'으로 끝나면 결제 실패로 PaymentException 발생")
        void throwWhenCardEndsWith00() {
            // given
            CreditCardPayment cc = new CreditCardPayment("123456789000", "12/27");
            double amount = 5000;

            // when / then
            assertThrows(PaymentException.class,
                    () -> cc.pay(amount),
                    "카드번호가 00으로 끝나면 PaymentException이 발생해야 한다");
        }
    }

    @Nested
    @DisplayName("BankTransferPayment 생성자 유효성 검사")
    class BankTransferConstructorValidation {

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("빈 은행명으로 생성 시 IllegalArgumentException 발생")
        void throwWhenBankNameInvalid(String badBankName) {
            // given
            String acc = "123456";

            // when / then
            assertThrows(IllegalArgumentException.class,
                    () -> new BankTransferPayment(badBankName, acc),
                    "빈 은행명으로 생성 시 IllegalArgumentException이 발생해야 한다");
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "12345"})
        @DisplayName("계좌번호 길이가 6 미만이면 IllegalArgumentException 발생")
        void throwWhenAccountNumberInvalid(String badAcc) {
            // given
            String bank = "국민은행";

            // when / then
            assertThrows(IllegalArgumentException.class,
                    () -> new BankTransferPayment(bank, badAcc),
                    "계좌번호가 6자리 미만이면 IllegalArgumentException이 발생해야 한다");
        }
    }

    @Nested
    @DisplayName("BankTransferPayment.pay 메서드 동작")
    class BankTransferPayTests {

        @Test
        @DisplayName("양수 금액으로 결제 시 예외 없이 통과한다")
        void paySuccess() {
            // given
            BankTransferPayment bt = new BankTransferPayment("우리은행", "123456");
            double amount = 20000;

            // when / then
            assertDoesNotThrow(() -> bt.pay(amount),
                    "양수 금액으로 pay 호출 시 예외가 발생하면 안 된다");
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, -500.0})
        @DisplayName("0 이하 금액으로 결제 시 PaymentException 발생")
        void throwWhenNonPositiveAmount(double badAmount) {
            // given
            BankTransferPayment bt = new BankTransferPayment("우리은행", "123456");

            // when / then
            assertThrows(PaymentException.class,
                    () -> bt.pay(badAmount),
                    "0원이하 금액으로 pay 호출 시 PaymentException이 발생해야 한다");
        }

        @Test
        @DisplayName("계좌번호가 '999999'로 끝나면 결제 실패로 PaymentException 발생")
        void throwWhenAccountEndsWith999999() {
            // given
            BankTransferPayment bt = new BankTransferPayment("우리은행", "123999999");
            double amount = 10000;

            // when / then
            assertThrows(PaymentException.class,
                    () -> bt.pay(amount),
                    "계좌번호가 999999로 끝나면 PaymentException이 발생해야 한다");
        }
    }
}