package org.univ.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Food 클래스 동작 검증 (냉장 여부 변경 로직 제거된 버전)")
class FoodTest {

    @Nested
    @DisplayName("생성자 및 설명 검증")
    class ConstructionTests {

        @Test
        @DisplayName("refrigerated=true 이면 설명에 '냉장 보관 필요'가 포함된다")
        void descriptionIncludesRefrigerationWhenTrue() {
            // given
            Food food = new Food(
                    "F100", "냉장치킨", 15000, "신선한 치킨", 5, true
            );

            // when
            String desc = food.getDescription();

            // then
            assertTrue(food.isRefrigerated(), "isRefrigerated()가 true여야 한다");
            assertTrue(desc.contains("냉장 보관 필요"),
                    "refrigerated=true일 때 설명에 '냉장 보관 필요'가 포함되어야 한다");
        }

        @Test
        @DisplayName("refrigerated=false 이면 설명에 '냉장 보관 필요'가 포함되지 않는다")
        void descriptionExcludesRefrigerationWhenFalse() {
            // given
            Food food = new Food(
                    "F101", "상온간식", 5000, "간편 간식", 10, false
            );

            // when
            String desc = food.getDescription();

            // then
            assertFalse(food.isRefrigerated(), "isRefrigerated()가 false여야 한다");
            assertFalse(desc.contains("냉장 보관 필요"),
                    "refrigerated=false일 때 설명에 '냉장 보관 필요'가 없어야 한다");
        }
    }

    @Nested
    @DisplayName("getDiscountedPrice 메서드 검증")
    class DiscountTests {

        @ParameterizedTest
        @ValueSource(doubles = { 0.0, 1000.0, 12345.67 })
        @DisplayName("할인 로직 미구현: getDiscountedPrice()는 getPrice()와 같다")
        void discountedPriceEqualsPrice(double price) {
            // given
            Food food = new Food("F200", "테스트상품", price, "기본설명", 1, true);

            // when
            double discounted = food.getDiscountedPrice();

            // then
            assertEquals(price, discounted,
                    "할인 미구현이므로 getDiscountedPrice()는 getPrice()와 동일해야 한다");
        }
    }
}