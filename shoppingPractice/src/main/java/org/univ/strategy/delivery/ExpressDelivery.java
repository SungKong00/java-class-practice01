package org.univ.strategy.delivery;

import org.univ.exception.DeliveryException;

// 빠른(특급) 배송 방식 구현체 (ExpressDelivery)
// - "주소:" 패턴이 반드시 포함되어야 배송 요청이 가능
// - "냉장" 또는 "특수포장" 키워드가 없으면 특급 배송 거부
public class ExpressDelivery implements DeliveryMethod {

    private static final int AVERAGE_DAYS = 1; // 특급 배송일 수(예시)

    @Override
    public void deliver(String productSummary) {
        // 주소 패턴 검증: "주소:"가 포함되어야만 정상으로 인정
        if (productSummary == null || !productSummary.contains("주소:")) {
            throw new DeliveryException("배송지 주소가 없습니다. 특급 배송 불가.");
        }

        // 특급 배송 대상 여부 검사: "냉장" 또는 "특수포장" 키워드가 반드시 있어야 함
        if (!productSummary.contains("냉장") && !productSummary.contains("특수포장")) {
            throw new DeliveryException("특급 배송은 냉장 또는 특수포장 품목만 가능합니다.");
        }

        // 정상 흐름: 예외 없이 리턴되면 배송 성공
    }
}
