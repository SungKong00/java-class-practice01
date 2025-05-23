package org.univ.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.univ.domain.customer.Customer;
import org.univ.domain.product.Clothing;
import org.univ.domain.product.Product;
import org.univ.exception.DeliveryException;
import org.univ.exception.PaymentException;
import org.univ.exception.StockUnavailableException;
import org.univ.strategy.delivery.DeliveryMethod;
import org.univ.strategy.payment.PaymentMethod;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Order 클래스 주요 메서드 동작 검증")
class OrderTest {

    // 더미 고객 & 전략 객체 준비
    Customer dummyCustomer = new Customer(
            "C100", "테스터", "test@univ.org", "010-0000-0000", "주소: 서울시"
    );
    PaymentMethod successPay = amt -> { /* 언제나 성공 */ };
    PaymentMethod failPay    = amt -> { throw new PaymentException("결제 실패"); };
    DeliveryMethod successDel = sum -> { /* 언제나 성공 */ };
    DeliveryMethod failDel    = sum -> { throw new DeliveryException("배송 실패"); };

    // 더미 상품 (재고 5개)
    Product dummyProduct = new Clothing(
            "P100", "티셔츠", 10000, "테스트", 5, "M", "면"
    );

    @Nested
    @DisplayName("addProduct 메서드 검증")
    class AddProductTests {

        @Test
        @DisplayName("null 상품을 추가하면 IllegalArgumentException 발생")
        void throwWhenProductNull() {
            // given: 빈 상품 목록으로 Order 생성
            Order o = new Order("O100", dummyCustomer, List.of(), successPay, successDel);

            // when / then
            assertThrows(IllegalArgumentException.class,
                    () -> o.addProduct(null),
                    "null 상품 추가 시 IllegalArgumentException 발생");
        }

        @Test
        @DisplayName("재고 부족 시 StockUnavailableException 발생")
        void throwWhenStockInsufficient() {
            // given: 재고 1개짜리 상품, 빈 리스트로 시작
            Product p = new Clothing("P101", "바지", 20000, "테스트", 1, "L", "면");
            Order o = new Order("O101", dummyCustomer, List.of(), successPay, successDel);

            // when: 첫 출고 정상 처리
            o.addProduct(p);

            // then: 두 번째 출고 시 재고 부족 예외
            assertThrows(StockUnavailableException.class,
                    () -> o.addProduct(p),
                    "재고 부족 시 StockUnavailableException 발생");
        }

        @Test
        @DisplayName("정상 상품 추가 시 productList에 포함된다")
        void addProductSuccessfully() {
            // given: 빈 상품 목록으로 시작
            Order o = new Order("O102", dummyCustomer, List.of(), successPay, successDel);

            // when: 상품 추가
            o.addProduct(dummyProduct);

            // then: 목록에 추가된 상품이 포함되어야 한다
            assertTrue(o.getProductList().contains(dummyProduct),
                    "addProduct 후 상품이 목록에 포함되어야 한다");
        }
    }

    @Nested
    @DisplayName("calculateTotalPrice 메서드 검증")
    class CalculateTotalPriceTests {

        @Test
        @DisplayName("상품이 없으면 0을 반환한다")
        void returnZeroWhenEmpty() {
            // given: 빈 상품 목록으로 생성
            Order o = new Order("O200", dummyCustomer, List.of(), successPay, successDel);

            // when: 금액 계산
            double total = o.calculateTotalPrice();

            // then: 0 반환
            assertEquals(0.0, total, "상품이 없으면 총 금액은 0이어야 한다");
        }

        @Test
        @DisplayName("여러 상품이 있으면 할인된 가격 합계를 반환한다")
        void sumDiscountedPrices() {
            // given: 같은 상품 두 개로 생성
            Order o = new Order("O201", dummyCustomer,
                    List.of(dummyProduct, dummyProduct), successPay, successDel);

            // when: 금액 계산
            double total = o.calculateTotalPrice();

            // then: 2 * 10000 = 20000 반환
            assertEquals(20000.0, total, "두 개 상품 가격 합산이 정확해야 한다");
        }
    }

    @Nested
    @DisplayName("processPayment 메서드 검증")
    class ProcessPaymentTests {

        @Test
        @DisplayName("주문 대기 상태에서 정상 결제 시 상태가 PAID로 변경된다")
        void changeStatusToPaidOnSuccess() {
            Order o = new Order("O300", dummyCustomer, List.of(dummyProduct), successPay, successDel);

            // when
            o.processPayment();

            // then
            assertEquals(OrderStatus.PAID, o.getStatus(),
                    "정상 결제 시 상태가 PAID로 변경되어야 한다");
        }

        @Test
        @DisplayName("결제 실패 시 PaymentException이 발생하고 상태는 PENDING 유지")
        void throwAndRemainPendingOnFailure() {
            Order o = new Order("O301", dummyCustomer, List.of(dummyProduct), failPay, successDel);

            // when / then
            assertThrows(PaymentException.class,
                    o::processPayment,
                    "결제 실패 시 PaymentException이 발생해야 한다");
            assertEquals(OrderStatus.PENDING, o.getStatus(),
                    "결제 실패 후 상태는 여전히 PENDING이어야 한다");
        }

        @ParameterizedTest
        @ValueSource(strings = { "O302", "O303", "O304" })
        @DisplayName("PAID 상태에서 processPayment 호출 시 IllegalStateException 발생")
        void throwWhenNotPending(String validId) {
            // given: 정상 order 생성 후 결제하여 PAID 상태로 변경
            Order o = new Order(validId, dummyCustomer, List.of(dummyProduct), successPay, successDel);
            o.processPayment();

            // when / then
            assertThrows(IllegalStateException.class,
                    o::processPayment,
                    "PAID 상태에서 processPayment 호출 시 IllegalStateException 발생");
        }
    }

    @Nested
    @DisplayName("startDelivery 메서드 검증")
    class StartDeliveryTests {

        @Test
        @DisplayName("PAID 상태에서 정상 배송 시 상태가 SHIPPED로 변경된다")
        void changeStatusToShippedOnSuccess() {
            Order o = new Order("O400", dummyCustomer, List.of(dummyProduct), successPay, successDel);
            o.processPayment();

            // when
            o.startDelivery();

            // then
            assertEquals(OrderStatus.SHIPPED, o.getStatus(),
                    "정상 배송 시 상태가 SHIPPED로 변경되어야 한다");
        }

        @Test
        @DisplayName("결제 전 배송 시 IllegalStateException 발생")
        void throwWhenNotPaid() {
            Order o = new Order("O401", dummyCustomer, List.of(dummyProduct), successPay, successDel);

            // when / then
            assertThrows(IllegalStateException.class,
                    o::startDelivery,
                    "PENDING 상태에서 startDelivery 호출 시 IllegalStateException 발생");
        }

        @Test
        @DisplayName("배송 실패 시 DeliveryException 발생하고 상태는 PAID 유지")
        void throwAndRemainPaidOnDeliveryFailure() {
            Order o = new Order("O402", dummyCustomer, List.of(dummyProduct), successPay, failDel);
            o.processPayment();

            // when / then
            assertThrows(DeliveryException.class,
                    o::startDelivery,
                    "배송 실패 시 DeliveryException이 발생해야 한다");
            assertEquals(OrderStatus.PAID, o.getStatus(),
                    "배송 실패 후 상태는 여전히 PAID이어야 한다");
        }
    }
}