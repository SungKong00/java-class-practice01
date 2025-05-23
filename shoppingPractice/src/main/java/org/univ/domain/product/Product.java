package org.univ.domain.product;

// 상품(Product) 추상 클래스: 의류, 전자제품 등 모든 상품의 공통 정보를 정의
public abstract class Product {
    private String productId;      // 상품 고유 ID
    private String name;           // 상품명
    private double price;          // 가격(원)
    private String description;    // 설명(옵션)
    private int stockQuantity;     // 재고 수량

    // 생성자: 필수 정보로 상품 객체를 초기화한다
    public Product(String productId, String name, double price, String description, int stockQuantity) {
        // 상품 ID는 필수
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("상품 ID는 필수입니다.");
        }
        // 상품명은 필수
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
        // 가격은 0원 이상만 허용
        if (price < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
        // 재고는 0 이상만 허용
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("재고 수량은 음수가 될 수 없습니다.");
        }
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.description = description == null ? "" : description;
        this.stockQuantity = stockQuantity;
    }

    // 상품 ID 반환
    public String getProductId() {
        return productId;
    }

    // 상품명 반환
    public String getName() {
        return name;
    }

    // 가격 반환
    public double getPrice() {
        return price;
    }

    // 설명 반환
    public String getDescription() {
        return description;
    }

    // 설명 변경
    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    // 재고 반환
    public int getStockQuantity() {
        return stockQuantity;
    }

    // 재고 수량 변경: 양수(입고), 음수(출고)
    public void updateStock(int change) {
        int updated = this.stockQuantity + change;
        if (updated < 0) {
            throw new org.univ.exception.StockUnavailableException(
                    "재고 부족: 현재 재고 " + this.stockQuantity + ", 출고 요청 " + (-change)
            );
        }
        this.stockQuantity = updated;
    }

    // 상품별 할인 가격을 반환: 반드시 하위 클래스에서 구현(오버라이딩)
    public abstract double getDiscountedPrice();
}