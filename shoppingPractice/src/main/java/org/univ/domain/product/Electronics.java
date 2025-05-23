package org.univ.domain.product;

// 전자제품(Electronics) 상품 클래스
public class Electronics extends Product {

    private int warrantyPeriod; // 보증 기간(개월 단위)

    // 생성자: 공통 필드 + 전자제품 전용 보증기간 초기화
    public Electronics(String productId,
                       String name,
                       double price,
                       String baseDescription,
                       int stockQuantity,
                       int warrantyPeriod) {
        super(productId, name, price, baseDescription, stockQuantity);
        // 보증기간 유효성 검사: 음수 불가
        if (warrantyPeriod < 0) {
            throw new IllegalArgumentException("보증 기간은 음수일 수 없습니다.");
        }
        this.warrantyPeriod = warrantyPeriod;
        // 설명(description)에 보증기간 정보를 추가
        String fullDesc = super.getDescription()
                + " | 보증: " + warrantyPeriod + "개월";
        super.setDescription(fullDesc);
    }

    // 보증기간 반환
    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    // 보증기간 변경 (유효성 검사 + 설명 업데이트)
    public void setWarrantyPeriod(int warrantyPeriod) {
        if (warrantyPeriod < 0) {
            throw new IllegalArgumentException("보증 기간은 음수일 수 없습니다.");
        }
        this.warrantyPeriod = warrantyPeriod;
        // 기존 설명에서 '보증: ...' 부분을 제거하고 재구성
        String base = super.getDescription().split(" \\| ")[0];
        super.setDescription(base + " | 보증: " + warrantyPeriod + "개월");
    }

    // 할인 로직 미구현 상태: 기본 가격 그대로 반환
    @Override
    public double getDiscountedPrice() {
        return getPrice(); // 할인 정책이 없으므로 원가 반환
    }
}