package org.univ.domain.product;

// 의류(Clothing) 상품 클래스
public class Clothing extends Product {

    private String size;       // 의류 사이즈 (예: S, M, L)
    private String material;   // 의류 소재 (예: 면, 폴리에스터)

    // 생성자: 공통 필드 + 의류 전용 필드 초기화
    public Clothing(String productId,
                    String name,
                    double price,
                    String baseDescription,
                    int stockQuantity,
                    String size,
                    String material) {
        super(productId, name, price, baseDescription, stockQuantity);
        // 사이즈 유효성 검사: null 또는 빈 문자열 불가
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("의류 사이즈는 필수이며 빈 값일 수 없습니다.");
        }
        // 소재 유효성 검사: null 또는 빈 문자열 불가
        if (material == null || material.isBlank()) {
            throw new IllegalArgumentException("의류 소재는 필수이며 빈 값일 수 없습니다.");
        }
        this.size = size;
        this.material = material;
        // 설명(description)에 의류 정보를 추가
        String fullDesc = super.getDescription()
                + " | 사이즈: " + size
                + ", 소재: " + material;
        super.setDescription(fullDesc);
    }

    // 사이즈 반환
    public String getSize() {
        return size;
    }

    // 사이즈 변경 (유효성 검사 + 설명 업데이트)
    public void setSize(String size) {
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("의류 사이즈는 빈 값일 수 없습니다.");
        }
        this.size = size;
        // 기존 설명에서 '사이즈: ...' 부분을 제거하고 재구성
        String base = super.getDescription().split(" \\| ")[0];
        String materialPart = this.material;
        super.setDescription(base + " | 사이즈: " + size + ", 소재: " + materialPart);
    }

    // 소재 반환
    public String getMaterial() {
        return material;
    }

    // 소재 변경 (유효성 검사 + 설명 업데이트)
    public void setMaterial(String material) {
        if (material == null || material.isBlank()) {
            throw new IllegalArgumentException("의류 소재는 빈 값일 수 없습니다.");
        }
        this.material = material;
        // 기존 설명에서 '소재: ...' 부분을 제거하고 재구성
        String base = super.getDescription().split(" \\| ")[0];
        String sizePart = this.size;
        super.setDescription(base + " | 사이즈: " + sizePart + ", 소재: " + material);
    }

    // 할인 로직 미구현 상태: 기본 가격 그대로 반환
    @Override
    public double getDiscountedPrice() {
        return getPrice(); // 할인 정책이 없으므로 원가 반환
    }
}