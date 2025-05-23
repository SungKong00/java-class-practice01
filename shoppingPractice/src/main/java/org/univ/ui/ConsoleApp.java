package org.univ.ui;

import org.univ.domain.customer.Customer;
import org.univ.domain.product.Clothing;
import org.univ.domain.product.Electronics;
import org.univ.domain.product.Product;
import org.univ.domain.order.Order;
import org.univ.exception.IllegalOrderStateException;
import org.univ.service.OrderService;
import org.univ.strategy.delivery.ExpressDelivery;
import org.univ.strategy.delivery.StandardDelivery;
import org.univ.strategy.delivery.DeliveryMethod;
import org.univ.strategy.payment.CreditCardPayment;
import org.univ.strategy.payment.BankTransferPayment;
import org.univ.strategy.payment.PaymentMethod;

import java.util.*;

/**
 * 간단한 콘솔 UI 애플리케이션
 * - 고객 정보 입력
 * - 상품 목록 표시 및 선택
 * - 결제/배송 방식 선택
 * - 주문 실행 및 결과 출력
 */
public class ConsoleApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderService orderService = new OrderService();

        // 1. 고객 정보 입력
        System.out.println("=== 쇼핑몰 주문 시스템 ===");
        System.out.print("고객 ID: ");
        String customerId = sc.nextLine().trim();
        System.out.print("이름: ");
        String name = sc.nextLine().trim();
        System.out.print("이메일: ");
        String email = sc.nextLine().trim();
        System.out.print("연락처: ");
        String phone = sc.nextLine().trim();
        System.out.print("주소 (예: 주소: 서울시 강남구 ...): ");
        String address = sc.nextLine().trim();
        Customer customer = new Customer(customerId, name, email, phone, address);

        // 2. 샘플 상품 목록 생성
        List<Product> catalog = new ArrayList<>();
        catalog.add(new Clothing("C001", "반팔티", 15000, "여름용 반팔티", 10, "M", "면"));
        catalog.add(new Electronics("E001", "무선헤드폰", 50000, "블루투스 헤드폰", 5, 12));
        catalog.add(new Clothing("C002", "청바지", 30000, "데님 청바지", 7, "L", "데님"));

        // 3. 상품 선택
        List<Product> orderProducts = new ArrayList<>();
        while (true) {
            System.out.println("\n== 상품 목록 ==");
            for (int i = 0; i < catalog.size(); i++) {
                Product p = catalog.get(i);
                System.out.printf("%d) %s - %s (가격: %,d원, 재고: %d)%n",
                        i+1, p.getProductId(), p.getName(), (int)p.getPrice(), p.getStockQuantity());
            }
            System.out.print("주문할 상품 번호(여러 개는 콤마 구분), 0 입력 시 완료: ");
            String line = sc.nextLine().trim();
            if ("0".equals(line)) break;

            String[] tokens = line.split(",");
            for (String tok : tokens) {
                try {
                    int idx = Integer.parseInt(tok.trim()) - 1;
                    if (idx >= 0 && idx < catalog.size()) {
                        orderProducts.add(catalog.get(idx));
                        System.out.printf("'%s' 상품이 주문 목록에 추가되었습니다.%n",
                                catalog.get(idx).getName());
                    } else {
                        System.out.println("잘못된 번호: " + tok);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("숫자만 입력하세요: " + tok);
                }
            }
        }

        // 4. 결제 방식 선택
        PaymentMethod paymentMethod;
        while (true) {
            System.out.println("\n== 결제 방식 선택 ==");
            System.out.println("1) 신용카드");
            System.out.println("2) 계좌이체");
            System.out.print("선택: ");
            String sel = sc.nextLine().trim();
            if ("1".equals(sel)) {
                System.out.print("카드 번호(12자리): ");
                String cardNumber = sc.nextLine().trim();
                System.out.print("유효기간(MM/YY): ");
                String expiry = sc.nextLine().trim();
                paymentMethod = new CreditCardPayment(cardNumber, expiry);
                break;
            } else if ("2".equals(sel)) {
                System.out.print("은행명: ");
                String bank = sc.nextLine().trim();
                System.out.print("계좌번호: ");
                String acc = sc.nextLine().trim();
                paymentMethod = new BankTransferPayment(bank, acc);
                break;
            } else {
                System.out.println("1 또는 2를 입력하세요.");
            }
        }

        // 5. 배송 방식 선택
        DeliveryMethod deliveryMethod;
        while (true) {
            System.out.println("\n== 배송 방식 선택 ==");
            System.out.println("1) 일반 배송");
            System.out.println("2) 특급 배송");
            System.out.print("선택: ");
            String sel = sc.nextLine().trim();
            if ("1".equals(sel)) {
                deliveryMethod = new StandardDelivery();
                break;
            } else if ("2".equals(sel)) {
                deliveryMethod = new ExpressDelivery();
                break;
            } else {
                System.out.println("1 또는 2를 입력하세요.");
            }
        }

        // 6. 주문 실행
        try {
            Order order = orderService.placeOrder(customer, orderProducts, paymentMethod, deliveryMethod);
            System.out.println("\n--- 주문 완료 ---");
            System.out.println("최종 상태: " + order.getStatus());
            System.out.println(order.getOrderSummary());
        } catch (IllegalOrderStateException ex) {
            System.out.println("\n!!! 주문 처리 중 오류 발생 !!!");
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println("\n!!! 예기치 못한 오류 !!!");
            ex.printStackTrace();
        }

        System.out.println("\n프로그램을 종료합니다.");
        sc.close();
    }
}