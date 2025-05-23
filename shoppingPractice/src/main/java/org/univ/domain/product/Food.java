package org.univ.domain.product;

// 식품(Food) 상품 클래스: 신선 식품 등 냉장·냉동이 필요한 품목
public class Food extends Product {

    private boolean refrigerated;  // 냉장(냉동) 보관 여부

    /**
     * @param productId       상품 고유 ID
     * @param name            상품명
     * @param price           가격
     * @param baseDescription 기본 설명
     * @param stockQuantity   재고 수량
     * @param refrigerated    냉장 보관이 필요한지 여부
     */
    public Food(String productId,
                String name,
                double price,
                String baseDescription,
                int stockQuantity,
                boolean refrigerated) {
        super(productId, name, price, baseDescription, stockQuantity);
        this.refrigerated = refrigerated;

        // 설명에 냉장 정보 자동 추가
        if (refrigerated) {
            super.setDescription(super.getDescription() + " | 냉장 보관 필요");
        }
    }

    // 냉장 여부 반환
    public boolean isRefrigerated() {
        return refrigerated;
    }

    // 할인 로직 미구현: 기본 가격 그대로 반환
    @Override
    public double getDiscountedPrice() {
        return getPrice();
    }
}
