package org.univ.startegy.delivery;


import org.univ.exception.DeliveryException;

public interface DeliveryMethod {
    /**
     * 배송을 수행한다.
     * @param productSummary 상품 및 주소 등 요약정보(임시 String, 실제론 별도 DTO로 확장 가능)
     * @throws DeliveryException 배송 실패 시 예외 발생
     */
    void deliver(String productSummary); // 향후 필요 시 파라미터 구조 변경 가능
}