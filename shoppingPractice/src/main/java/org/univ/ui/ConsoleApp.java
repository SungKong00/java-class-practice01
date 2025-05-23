package org.univ.ui;

import org.univ.domain.customer.Customer;
import org.univ.domain.order.Order;
import org.univ.domain.product.Clothing;
import org.univ.domain.product.Electronics;
import org.univ.domain.product.Food;
import org.univ.domain.product.Product;
import org.univ.exception.IllegalOrderStateException;
import org.univ.service.OrderService;
import org.univ.strategy.delivery.ExpressDelivery;
import org.univ.strategy.delivery.StandardDelivery;
import org.univ.strategy.delivery.DeliveryMethod;
import org.univ.strategy.payment.BankTransferPayment;
import org.univ.strategy.payment.CreditCardPayment;
import org.univ.strategy.payment.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 콘솔 UI 애플리케이션
 * - 로그인 후 메인 메뉴에서 주문하기, 주문 조회, 내 정보 조회, 종료를 반복
 * - 카탈로그는 애플리케이션 전체에서 한 번만 초기화되어 재고가 유지됨
 */
public class ConsoleApp {

    private final InputReader reader = new InputReader();
    private final OrderService orderService = new OrderService();
    private Customer customer;
    // 애플리케이션 실행 내내 공유할 카탈로그
    private final List<Product> catalog = buildCatalog();

    public static void main(String[] args) {
        new ConsoleApp().run();
    }

    public void run() {
        System.out.println("=== 쇼핑몰 주문 시스템 ===");
        customer = readCustomer();

        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== 메인 메뉴 ===");
            System.out.println("1) 주문하기");
            System.out.println("2) 주문 조회");
            System.out.println("3) 내 정보 조회");
            System.out.println("4) 종료");
            String choice = reader.readLine("선택: ",
                    s -> s.matches("[1-4]"),
                    "1~4 사이 숫자만 입력해주세요.");

            switch (choice) {
                case "1":
                    handlePlaceOrder();
                    break;
                case "2":
                    handleViewOrders();
                    break;
                case "3":
                    handleViewCustomerInfo();
                    break;
                case "4":
                    exit = true;
                    break;
            }
        }

        reader.close();
        System.out.println("프로그램을 종료합니다.");
    }

    private void handlePlaceOrder() {
        try {
            List<Product> items = selectProducts(catalog);
            PaymentMethod pm = selectPaymentMethod();
            DeliveryMethod dm = selectDeliveryMethod();

            Order order = orderService.placeOrder(customer, items, pm, dm);
            customer.addOrder(order);

            System.out.println("\n--- 주문 완료 ---");
            System.out.println("주문 상태: " + order.getStatus());
            System.out.println(order.getOrderSummary());
        } catch (IllegalOrderStateException | IllegalArgumentException ex) {
            System.out.println("\n오류: " + ex.getMessage());
        }
    }

    private void handleViewOrders() {
        System.out.println("\n=== 내 주문 조회 ===");
        List<Order> orders = customer.getOrders();
        if (orders.isEmpty()) {
            System.out.println("등록된 주문이 없습니다.");
        } else {
            for (Order o : orders) {
                System.out.printf(
                        "• 주문ID: %s, 상태: %s%n  %s%n",
                        o.getOrderId(),
                        o.getStatus(),
                        o.getOrderSummary()
                );
            }
        }
    }

    private void handleViewCustomerInfo() {
        System.out.println("\n=== 내 정보 조회 ===");
        System.out.println("고객ID:    " + customer.getCustomerId());
        System.out.println("이름:      " + customer.getName());
        System.out.println("이메일:    " + customer.getEmail());
        System.out.println("연락처:    " + customer.getPhoneNumber());
        System.out.println("주소:      " + customer.getAddress());
    }

    private Customer readCustomer() {
        System.out.println("\n=== 로그인 / 회원 등록 ===");
        String id      = reader.readLine("고객 ID: ",
                s -> !s.isBlank(),
                "ID는 비워둘 수 없습니다.");
        String name    = reader.readLine("이름: ",
                s -> !s.isBlank(),
                "이름은 비워둘 수 없습니다.");
        String email   = reader.readLine("이메일: ",
                s -> s.contains("@"),
                "유효한 이메일이어야 합니다.");
        String phone   = reader.readLine("연락처: ",
                s -> !s.isBlank(),
                "연락처는 비워둘 수 없습니다.");
        String address = reader.readLine("주소 (‘주소:’ 포함): ",
                s -> s.startsWith("주소:"),
                "‘주소:’로 시작해야 합니다.");
        return new Customer(id, name, email, phone, address);
    }

    private List<Product> selectProducts(List<Product> catalog) {
        System.out.println("\n=== 상품 선택 ===");
        List<Product> chosen = new ArrayList<>();
        while (true) {
            for (int i = 0; i < catalog.size(); i++) {
                Product p = catalog.get(i);
                System.out.printf(
                        "%d) %s (%s) %,d원 [%d개]%n",
                        i + 1,
                        p.getName(),
                        p.getProductId(),
                        (int) p.getPrice(),
                        p.getStockQuantity()
                );
            }
            String line = reader.readLine(
                    "상품 번호(콤마 구분), 0=완료: ",
                    s -> s.matches("[0-9](,[0-9])*"),
                    "0 또는 번호 리스트만 입력하세요."
            );
            if ("0".equals(line)) break;
            for (String tok : line.split(",")) {
                int idx = Integer.parseInt(tok) - 1;
                chosen.add(catalog.get(idx));
                System.out.println("→ " + catalog.get(idx).getName() + " 선택됨");
            }
        }
        return chosen;
    }

    private PaymentMethod selectPaymentMethod() {
        System.out.println("\n=== 결제 방식 선택 ===");
        while (true) {
            System.out.println("1) 카드  2) 계좌");
            String sel = reader.readLine("선택: ",
                    s -> s.equals("1") || s.equals("2"),
                    "1 또는 2만 입력해주세요.");
            try {
                if ("1".equals(sel)) {
                    String cardNo = reader.readLine(
                            "카드번호(12자리): ",
                            s -> s.matches("\\d{12}"),
                            "12자리 숫자여야 합니다."
                    );
                    String exp = reader.readLine(
                            "유효기간(MM/YY): ",
                            s -> s.matches("\\d{2}/\\d{2}"),
                            "MM/YY 형식이어야 합니다."
                    );
                    return new CreditCardPayment(cardNo, exp);
                } else {
                    String bank = reader.readLine(
                            "은행명: ",
                            s -> !s.isBlank(),
                            "은행명을 입력해주세요."
                    );
                    String acc = reader.readLine(
                            "계좌번호(6자리 이상): ",
                            s -> s.matches("\\d{6,}"),
                            "6자리 이상 숫자여야 합니다."
                    );
                    return new BankTransferPayment(bank, acc);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("입력 오류: " + e.getMessage() + " 다시 시도하세요.");
            }
        }
    }

    private DeliveryMethod selectDeliveryMethod() {
        System.out.println("\n=== 배송 방식 선택 ===");
        while (true) {
            System.out.println("1) 일반  2) 특급");
            String sel = reader.readLine("선택: ",
                    s -> s.equals("1") || s.equals("2"),
                    "1 또는 2만 입력하세요.");
            if ("1".equals(sel)) {
                return new StandardDelivery();
            } else {
                return new ExpressDelivery();
            }
        }
    }

    // 애플리케이션 실행 내내 한 번만 호출되는 카탈로그 빌더
    private List<Product> buildCatalog() {
        List<Product> c = new ArrayList<>();
        c.add(new Clothing("C001", "반팔티", 15000, "여름용 반팔티", 10, "M", "면"));
        c.add(new Electronics("E001", "무선헤드폰", 50000, "블루투스 헤드폰", 5, 12));
        c.add(new Clothing("C002", "청바지", 30000, "데님 청바지", 7, "L", "데님"));
        c.add(new Food("F001", "냉장 식품", 20000, "신선 냉장 보관 필요", 20, true));
        return c;
    }
}