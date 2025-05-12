package org.univ.domain.product;

public class Electronics extends Product {

    private int warrantyPeriod; // 단위: 개월

    public Electronics(String productId, String name, double price, String baseDescription, int stockQuantity, int warrantyPeriod) {
        super(productId, name, price, baseDescription, stockQuantity);

        if (warrantyPeriod < 0) {
            throw new IllegalArgumentException("보증 기간은 음수가 될 수 없습니다.");
        }

        this.warrantyPeriod = warrantyPeriod;

        // 설명 필드에 보증기간을 추가 설명으로 붙임
        String fullDescription = baseDescription + " | 보증: " + warrantyPeriod + "개월";
        super.setDescription(fullDescription);
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        if (warrantyPeriod < 0) {
            throw new IllegalArgumentException("보증 기간은 음수가 될 수 없습니다.");
        }
        this.warrantyPeriod = warrantyPeriod;

        // 설명도 자동 업데이트
        String base = super.getDescription().split(" \\| ")[0];
        super.setDescription(base + " | 보증: " + warrantyPeriod + "개월");
    }

    // getDiscountedPrice()는 구현하지 않음 (Product 추상 클래스에 그대로 유지)
}