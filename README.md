# 온라인 쇼핑몰 주문 시스템

본 프로젝트는 자바 언어로 작성한 **온라인 쇼핑몰 주문 처리 데모 애플리케이션**이다.  
객체지향 설계 원칙(SOLID)을 적용하여 **유지보수성과 확장성**을 높였으며, 결제·배송 로직에는 **전략 패턴**을 도입하여 새로운 방식 추가가 용이하도록 구성한다.

---

## ✨ 주요 기능
- **주문 생성**  
  고객이 여러 상품을 선택하여 한 번에 주문을 생성한다.
- **결제 처리**  
  - 신용카드 결제  
  - 계좌이체 결제  
  PaymentMethod 인터페이스를 구현하여 새로운 결제 방식 추가를 지원한다.
- **배송 처리**  
  - 일반 배송  
  - 특급 배송(냉장 식품 등 조건부)  
  DeliveryMethod 인터페이스로 배송 전략을 추상화한다.
- **재고 관리**  
  주문 성공 시 재고를 차감하고, 결제 실패·주문 취소 시 재고를 자동 복구한다.
- **콘솔 UI**  
  간단한 CLI 메뉴로 주문·조회·회원 정보를 관리한다.
- **단위·통합 테스트**  
  JUnit 기반 테스트를 작성하여 도메인 로직과 전체 프로세스를 검증한다.

---

## 🗂️ 프로젝트 구조
```
org.univ/
├─ domain/
│   ├─ customer/   (Customer)
│   ├─ product/    (Product 추상, Electronics, Clothing, Food 등)
│   ├─ order/      (Order, OrderStatus)
│   └─ exception/  (커스텀 예외)
├─ strategy/
│   ├─ payment/    (PaymentMethod, CreditCardPayment, BankTransferPayment)
│   └─ delivery/   (DeliveryMethod, StandardDelivery, ExpressDelivery)
├─ service/        (OrderService – 퍼사드 계층)
└─ console/        (ConsoleApp, InputReader – CLI 진입점)
```

3.	실행 후 나오는 CLI 메뉴를 따라 고객 등록 → 상품 선택 → 결제/배송 선택 → 주문을 진행한다.

⸻

🔑 설계 포인트
	•	SOLID 원칙
	•	SRP: 클래스별 책임 분리
	•	OCP & DIP: 결제·배송을 인터페이스와 전략 패턴으로 추상화
	•	LSP & ISP: Product 하위 타입, Payment/Delivery 인터페이스의 준수
	•	전략 패턴
PaymentMethod, DeliveryMethod 인터페이스를 통해 결제·배송 로직을 런타임에 주입한다.
	•	엄격한 상태 전이
OrderStatus(Enum)로 주문 라이프사이클을 관리하여 잘못된 상태 변경을 차단한다.

⸻

🧪 테스트
	•	단위 테스트: 각 도메인 객체의 입력 검증, 재고 계산, 상태 관리 로직을 검증한다.
	•	통합 테스트: 주문 생성→결제→배송 전 과정을 시나리오별로 검증하여 객체 간 협업을 확인한다.
