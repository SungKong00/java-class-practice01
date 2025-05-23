package org.univ.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.univ.domain.customer.Customer;
import org.univ.domain.product.Clothing;
import org.univ.domain.product.Product;
import org.univ.domain.order.Order;
import org.univ.domain.order.OrderStatus;
import org.univ.exception.DeliveryException;
import org.univ.exception.PaymentException;
import org.univ.exception.IllegalOrderStateException;
import org.univ.strategy.delivery.DeliveryMethod;
import org.univ.strategy.payment.PaymentMethod;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderService 통합 테스트")
class OrderServiceTest {

    private final OrderService orderService = new OrderService();

    // 공통 테스트 데이터
    private final Customer customer = new Customer(
            "C100", "테스터", "test@univ.org", "010-0000-0000", "주소: 서울시"
    );
    private final Product dummyProduct = new Clothing(
            "P100", "티셔츠", 10000, "테스트", 5, "M", "면"
    );

    @Nested
    @DisplayName("placeOrder 성공 시")
    class PlaceOrderSuccess {

        @Test
        @DisplayName("유효한 요청일 때 주문 생성·결제·배송 후 상태가 SHIPPED여야 한다")
        void placeOrderSuccess() {
            // given: 하나의 상품 리스트와 정상 전략
            List<Product> products = List.of(dummyProduct, dummyProduct);
            PaymentMethod pm = amount -> { /* 결제 성공 */ };
            DeliveryMethod dm = summary -> { /* 배송 성공 */ };

            // when
            Order order = orderService.placeOrder(customer, products, pm, dm);

            // then
            assertNotNull(order, "placeOrder는 null을 반환하면 안 된다");
            assertEquals(OrderStatus.SHIPPED, order.getStatus(),
                    "정상 흐름 후 상태가 SHIPPED여야 한다");
            assertEquals(products.size(), order.getProductList().size(),
                    "주문된 상품 개수가 일치해야 한다");
        }
    }

    @Nested
    @DisplayName("placeOrder 실패 시")
    class PlaceOrderFailure {

        static Stream<org.junit.jupiter.params.provider.Arguments> failureScenarios() {
            return Stream.of(
                    // 결제 실패
                    org.junit.jupiter.params.provider.Arguments.of(
                            (PaymentMethod)(amt -> { throw new PaymentException("카드 오류"); }),
                            (DeliveryMethod)(sum -> { /* 배송 성공 */ }),
                            "결제 과정 실패"
                    ),
                    // 배송 실패
                    org.junit.jupiter.params.provider.Arguments.of(
                            (PaymentMethod)(amt -> { /* 결제 성공 */ }),
                            (DeliveryMethod)(sum -> { throw new DeliveryException("주소 오류"); }),
                            "배송 과정 실패"
                    )
            );
        }

        @ParameterizedTest(name = "{index} {2}")
        @MethodSource("failureScenarios")
        @DisplayName("결제 또는 배송 실패 시 IllegalOrderStateException이 발생한다")
        void placeOrderFailure(PaymentMethod pm,
                               DeliveryMethod dm,
                               String expectedMessagePrefix) {
            // given: 하나의 상품 리스트
            List<Product> products = List.of(dummyProduct);

            // when / then
            IllegalOrderStateException ex = assertThrows(
                    IllegalOrderStateException.class,
                    () -> orderService.placeOrder(customer, products, pm, dm),
                    "실패 시 IllegalOrderStateException이 발생해야 한다"
            );
            assertTrue(
                    ex.getMessage().startsWith(expectedMessagePrefix),
                    "예외 메시지는 '" + expectedMessagePrefix + "'로 시작해야 한다"
            );
        }
    }
}