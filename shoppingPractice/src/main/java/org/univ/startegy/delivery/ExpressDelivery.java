package org.univ.startegy.delivery;

import org.univ.exception.DeliveryException;

// 빠른(특급) 배송 방식 구현체
public class ExpressDelivery implements DeliveryMethod {

    private static final int AVERAGE_DAYS = 1; // 특급 배송일 수

    // 주문 요약정보를 받아 특급 배송을 시도한다.
    @Override
    public void deliver(String productSummary) {
        // 예시: 주소가 누락된 경우 배송 실패
        if (productSummary == null || !productSummary.contains("주소")) {
            throw new DeliveryException("배송지 주소가 없습니다. 특급 배송 불가.");
        }
        // 예시: "냉장" 또는 "특수포장" 키워드 없으면 특급 배송 거부
        if (!productSummary.contains("냉장") && !productSummary.contains("특수포장")) {
            throw new DeliveryException("특급 배송은 냉장 또는 특수포장 품목만 가능합니다.");
        }
        // 배송 성공 (실제로는 특급 택배사 연동 등)
        // 아무 예외 없이 종료되면 성공
    }
}
