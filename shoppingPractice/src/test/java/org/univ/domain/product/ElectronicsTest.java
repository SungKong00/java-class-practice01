package org.univ.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Electronics 클래스 생성 및 동작 검증")
class ElectronicsTest {

    @Nested
    @DisplayName("정상 생성 시")
    class ValidConstruction {

        @Test
        @DisplayName("모든 필드를 올바르게 넘기면 객체가 정상 생성되고 설명에 보증정보가 포함된다")
        void createValidElectronics() {
            // given
            String productId       = "E100";
            String name            = "헤드폰";
            double price           = 50000;
            String baseDescription = "무선 헤드폰";
            int stockQuantity      = 20;
            int warrantyPeriod     = 12;

            // when
            Electronics e = new Electronics(
                    productId, name, price, baseDescription, stockQuantity, warrantyPeriod
            );

            // then
            assertAll("필드 및 설명 확인",
                    () -> assertEquals(productId,   e.getProductId()),
                    () -> assertEquals(name,        e.getName()),
                    () -> assertEquals(price,       e.getPrice()),
                    () -> assertEquals(stockQuantity, e.getStockQuantity()),
                    () -> assertEquals(warrantyPeriod, e.getWarrantyPeriod()),
                    () -> assertTrue(e.getDescription().contains("보증: " + warrantyPeriod + "개월"),
                            "설명에 보증기간 정보가 포함되어야 한다")
            );
        }
    }

    @Nested
    @DisplayName("유효하지 않은 보증기간 입력 시 예외 발생")
    class InvalidWarranty {

        @ParameterizedTest
        @ValueSource(ints = { -1, -5, -100 })
        @DisplayName("보증기간이 음수이면 IllegalArgumentException을 던진다")
        void throwWhenWarrantyNegative(int invalidWarranty) {
            // given
            String productId       = "E200";
            String name            = "스마트폰";
            double price           = 800000;
            String baseDescription = "최신형 스마트폰";
            int stockQuantity      = 5;

            // when / then
            assertThrows(IllegalArgumentException.class,
                    () -> new Electronics(
                            productId, name, price, baseDescription, stockQuantity, invalidWarranty
                    ),
                    "보증기간이 음수일 때 IllegalArgumentException이 발생해야 한다"
            );
        }
    }
}