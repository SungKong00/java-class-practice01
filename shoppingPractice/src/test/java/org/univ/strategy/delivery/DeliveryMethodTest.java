package org.univ.strategy.delivery;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.univ.exception.DeliveryException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DeliveryMethod 구현체 동작 검증")
class DeliveryMethodTest {

    @Nested
    @DisplayName("StandardDelivery.deliver 메서드 동작")
    class StandardDeliveryTests {

        @Test
        @DisplayName("올바른 요약 정보가 주어지면 예외 없이 통과한다")
        void deliverSuccess() {
            // given
            StandardDelivery sd = new StandardDelivery();
            String summary = "주문ID: O1, 고객: 홍길동, 주소: 서울시 강남구";

            // when / then
            assertDoesNotThrow(() -> sd.deliver(summary),
                    "주소 정보가 포함된 정상 요약 정보면 예외 없이 통과해야 한다");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "",                          // 빈 문자열
                "상품명만 있음",              // 주소 키워드 없음
                "주소 없음"                   // 키워드는 있으나 실제 주소 태그 아님
        })
        @DisplayName("주소 정보가 없으면 DeliveryException 발생")
        void throwWhenNoAddress(String badSummary) {
            // given
            StandardDelivery sd = new StandardDelivery();

            // when / then
            assertThrows(DeliveryException.class,
                    () -> sd.deliver(badSummary),
                    "주소 정보가 없으면 DeliveryException이 발생해야 한다");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "주문ID: O1, 고객: A, 주소: 서울시, 유해품목 포함",
                "주소: 부산, 취급주의 물품"
        })
        @DisplayName("유해 또는 취급주의 키워드 포함 시 DeliveryException 발생")
        void throwWhenProhibitedItem(String summaryWithProhibited) {
            // given
            StandardDelivery sd = new StandardDelivery();

            // when / then
            assertThrows(DeliveryException.class,
                    () -> sd.deliver(summaryWithProhibited),
                    "유해 또는 취급주의 키워드가 있으면 DeliveryException이 발생해야 한다");
        }
    }

    @Nested
    @DisplayName("ExpressDelivery.deliver 메서드 동작")
    class ExpressDeliveryTests {

        @Test
        @DisplayName("냉장 또는 특수포장 키워드가 포함된 요약 정보면 예외 없이 통과한다")
        void deliverSuccessForExpress() {
            // given
            ExpressDelivery ed = new ExpressDelivery();
            String summary = "주문ID: O2, 고객: 김철수, 주소: 서울시, 냉장";

            // when / then
            assertDoesNotThrow(() -> ed.deliver(summary),
                    "냉장 키워드가 포함되면 특급 배송 통과해야 한다");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "",                        // 빈 문자열
                "주문ID만 있음 주소 없음",  // 주소 키워드 없음
                "주소: 서울시"              // 주소만 있고 특수포장/냉장 없음
        })
        @DisplayName("주소 누락 또는 특급 배송 요건 미충족 시 DeliveryException 발생")
        void throwWhenInvalidForExpress(String badSummary) {
            // given
            ExpressDelivery ed = new ExpressDelivery();

            // when / then
            assertThrows(DeliveryException.class,
                    () -> ed.deliver(badSummary),
                    "주소 누락 또는 특급 배송 키워드 없으면 DeliveryException 발생해야 한다");
        }
    }
}