package org.univ.startegy.delivery;

import org.univ.exception.DeliveryException;

// 일반 배송 방식 구현체
public class StandardDelivery implements DeliveryMethod {

    private static final int AVERAGE_DAYS = 3; // 평균 배송일 수

    // 주문 요약정보를 받아 배송을 시도한다.
    @Override
    public void deliver(String productSummary) {
        // 예시: 주소 정보가 빠진 경우 배송 실패
        if (productSummary == null || !productSummary.contains("주소")) {
            throw new DeliveryException("배송지 주소가 없습니다. 일반 배송 불가.");
        }
        // 예시: 상품 요약에 "유해" 또는 "취급주의"가 포함되어 있으면 배송 불가
        if (productSummary.contains("유해") || productSummary.contains("취급주의")) {
            throw new DeliveryException("일반 배송 불가 품목이 포함되어 있습니다.");
        }
        // 배송 성공 (실제 시스템이라면 물류 연동 로직)
        // 아무 예외 없이 종료되면 성공
    }
}
