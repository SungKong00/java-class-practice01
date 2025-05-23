package org.univ.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Clothing 클래스 생성자 동작 검증")
class ClothingTest {

    @Nested
    @DisplayName("정상 생성 시")
    class ValidCreation {

        @Test
        @DisplayName("모든 필드를 올바르게 넘기면 객체가 정상 생성된다")
        void createWithValidInputs() {
            // given
            String productId      = "C001";
            String name           = "반팔티";
            double price          = 15000;
            String baseDescription= "여름용 반팔";
            int stockQuantity     = 50;
            String size           = "M";
            String material       = "면";

            // when
            Clothing clothing = new Clothing(
                    productId, name, price, baseDescription, stockQuantity, size, material
            );

            // then
            assertAll("필드 값 확인",
                    () -> assertEquals(productId,      clothing.getProductId()),
                    () -> assertEquals(name,           clothing.getName()),
                    () -> assertEquals(price,          clothing.getPrice()),
                    () -> assertEquals(stockQuantity,  clothing.getStockQuantity()),
                    () -> assertEquals(size,           clothing.getSize()),
                    () -> assertEquals(material,       clothing.getMaterial()),
                    () -> assertTrue(clothing.getDescription().contains(size)),
                    () -> assertTrue(clothing.getDescription().contains(material))
            );
        }
    }

    @Nested
    @DisplayName("잘못된 입력 시 예외 발생")
    class InvalidCreation {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("사이즈가 null 또는 빈 문자열이면 IllegalArgumentException을 던진다")
        void throwWhenSizeInvalid(String invalidSize) {
            // given
            String productId      = "C002";
            String name           = "셔츠";
            double price          = 12000;
            String baseDescription= "기본 셔츠";
            int stockQuantity     = 10;
            String material       = "면";

            // when / then
            assertThrows(IllegalArgumentException.class,
                    () -> new Clothing(
                            productId, name, price, baseDescription, stockQuantity,
                            invalidSize, material
                    ),
                    "사이즈가 null 혹은 빈 값일 때 IllegalArgumentException이 발생해야 한다"
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("소재가 null 또는 빈 문자열이면 IllegalArgumentException을 던진다")
        void throwWhenMaterialInvalid(String invalidMaterial) {
            // given
            String productId      = "C003";
            String name           = "셔츠";
            double price          = 12000;
            String baseDescription= "기본 셔츠";
            int stockQuantity     = 10;
            String size           = "L";

            // when / then
            assertThrows(IllegalArgumentException.class,
                    () -> new Clothing(
                            productId, name, price, baseDescription, stockQuantity,
                            size, invalidMaterial
                    ),
                    "소재가 null 혹은 빈 값일 때 IllegalArgumentException이 발생해야 한다"
            );
        }
    }
}