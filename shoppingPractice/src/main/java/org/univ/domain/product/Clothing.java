package org.univ.domain.product;

public class Clothing extends Product {

    private String size;
    private String material;

    public Clothing(
            String productId,
            String name,
            double price,
            String baseDescription,
            int stockQuantity,
            String size,
            String material) {
        super(productId, name, price, baseDescription, stockQuantity);

        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("사이즈는 필수 값입니다.");
        }
        if (material == null || material.isBlank()) {
            throw new IllegalArgumentException("소재는 필수 값입니다.");
        }

        this.size = size;
        this.material = material;

        // 설명 필드에 의류 정보(사이즈, 소재) 추가
        String fullDescription = baseDescription + " | 사이즈: " + size + ", 소재: " + material;
        super.setDescription(fullDescription);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("사이즈는 필수 값입니다.");
        }
        this.size = size;

        // 설명도 자동 업데이트 (기존 소재 정보는 유지)
        String materialPart = super.getDescription().replaceAll("^.*\\| 소재: ", "");
        String base = super.getDescription().split(" \\| ")[0];
        super.setDescription(base + " | 사이즈: " + size + ", 소재: " + materialPart);
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        if (material == null || material.isBlank()) {
            throw new IllegalArgumentException("소재는 필수 값입니다.");
        }
        this.material = material;

        // 설명도 자동 업데이트 (기존 사이즈 정보는 유지)
        String sizePart = super.getDescription().split("\\| 사이즈: ")[1].split(",")[0].trim();
        String base = super.getDescription().split(" \\| ")[0];
        super.setDescription(base + " | 사이즈: " + sizePart + ", 소재: " + material);
    }

    // getDiscountedPrice()는 여전히 구현하지 않고, 하위 클래스에서 필요 시 오버라이딩하도록 둠
}