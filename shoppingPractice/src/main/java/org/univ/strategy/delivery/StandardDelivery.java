package org.univ.strategy.delivery;

import org.univ.exception.DeliveryException;

// 일반 배송 방식 구현체 (StandardDelivery)
// - "주소:" 패턴이 반드시 포함되어야 정상 배송으로 간주
public class StandardDelivery implements DeliveryMethod {

    private static final int AVERAGE_DAYS = 3; // 평균 배송일 수

    @Override
    public void deliver(String productSummary) {
        // 주소 패턴 검증: "주소:" 가 포함되어야만 정상으로 인정
        if (productSummary == null || !productSummary.contains("주소:")) {
            throw new DeliveryException("배송지 주소가 없습니다. 일반 배송 불가.");
        }
        // 유해 또는 취급주의 키워드가 있으면 배송 불가
        if (productSummary.contains("유해") || productSummary.contains("취급주의")) {
            throw new DeliveryException("일반 배송 불가 품목이 포함되어 있습니다.");
        }
        // 정상 흐름: 예외 없이 리턴되면 배송 성공
    }
}