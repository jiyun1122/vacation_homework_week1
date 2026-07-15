# vacation_homework_week2
## 👥 1. 팀원 정보 및 역할 분담

| 이름 | 역할  | 담당 구현 기능 |
| :--- | :--- | :--- |
| **피윤혁** | **팀장 / Backend** | SpringSecurity, Cors 설정 개발 |
| **박수민** | **팀원 / Backend** | 배포 및 프로젝트 환경설정 EC2 관리 |
| **김지윤** | **팀원 / Backend** | 로그인 및 회원가입 API 개발 |


## 🎯 2. 과제 명세 대비 구현 현황 (Checklist)

## 1. 회원가입 및 로그인 구현

방학 1주차에는 구현하지 않았던 회원가입 및 로그인 기능을 구현합니다.
인증 방식은 **JWT 토큰 방식**을 사용합니다.

### 필수 구현 기능

- 회원가입
- 로그인
- JWT Access Token 발급
- JWT 토큰 검증
- 인증이 필요한 API에 대한 접근 제어
- 로그인한 사용자 정보 조회
- 비밀번호 암호화

### 회원가입

회원가입 요청을 받아 회원 정보를 데이터베이스에 저장합니다.

다음 내용을 고려하여 구현해주세요.

- 이메일, 아이디 등 서비스에서 사용하는 고유 값의 중복 여부를 확인합니다.
- 비밀번호는 평문으로 저장하지 않습니다.
- Spring Security의 `PasswordEncoder` 등을 활용하여 비밀번호를 암호화합니다.
- 요청값이 올바르지 않은 경우 적절한 에러를 반환합니다.
- 회원가입 Request DTO와 Response DTO를 분리하여 작성합니다.

<br>

## 📂 3. 프로젝트 폴더 구조 (Project Structure)
<pre><code>
mutsa_vacation_week1/demo
├── DemoApplication.java # 스프링부트 시작점
├── global/ # 공통/전역 설정
│   ├── apiPayload/
│   │   ├── ApiResponse.java # 공통 응답 포맷
│   │   ├── code/
│   │   │   ├── BaseErrorCode.java
│   │   │   ├── CommonErrorCode.java
│   │   │   ├── AuthErrorCode.java
│   │   │   ├── MemberErrorCode.java
│   │   │   ├── MenuErrorCode.java
│   │   │   ├── CartErrorCode.java
│   │   │   ├── OrderErrorCode.java
│   │   │   └── SuccessCode.java
│   │   └── exception/
│   │       ├── CustomException.java
│   │       └── GlobalExceptionHandler.java # 전역 예외 처리
│   ├── config/
│   │   └── CorsConfig.java
│   ├── exception/
│   │   └── ErrorCode.java
│   └── security/
│       ├── AuthMember.java # 인증된 사용자 정보 객체
│       ├── config/
│       │   ├── SecurityConfig.java # ✅ Security 필터체인 설정
│       │   └── JacksonConfig.java
│       ├── handler/
│       │   ├── JwtAuthenticationEntryPoint.java # 401 처리
│       │   └── JwtAccessDeniedHandler.java # 403 처리
│       ├── jwt/
│       │   ├── JwtTokenProvider.java # ✅ 토큰 생성/검증
│       │   ├── JwtAuthenticationFilter.java # ✅ 요청마다 토큰 검사
│       │   ├── JwtAuthenticationEntryPoint.java # (handler와 중복 이름 - 확인 필요)
│       │   ├── JwtAuthExceptionResponseWriter.java
│       │   └── JwtProperties.java
│       └── userdetails/
│           └── CustomUserDetails.java
├── member/ # 회원 도메인
│   ├── controller/MemberController.java
│   ├── service/MemberService.java
│   ├── repository/MemberRepository.java
│   ├── entity/Member.java
│   └── dto/
│       ├── request/ (SignupRequest, LoginRequest, CreditChargeRequest)
│       └── response/ (LoginResponse, MemberInfo, CreditResponse, CreditChargeResponse)
├── menu/ # 메뉴 도메인
│   ├── controller/MenuController.java
│   ├── service/MenuService.java
│   ├── repository/MenuRepository.java
│   ├── entity/Menu.java
│   └── dto/response/ (MenuListResponse, MenuDetailResponse, MenuSummaryResponse)
├── menuOption/ # 메뉴 옵션 도메인
│   ├── controller/MenuOptionController.java
│   ├── service/MenuOptionService.java
│   ├── repository/MenuOptionRepository.java
│   ├── entity/MenuOption.java
│   └── dto/
│       ├── request/ (MenuOptionCreateRequest, MenuOptionUpdateRequest)
│       └── response/ (MenuOptionResponse, MenuOptionDeleteResponse)
├── cart/ # 장바구니 도메인
│   ├── controller/CartController.java
│   ├── service/CartService.java
│   ├── repository/ (CartRepository, CartItemRepository)
│   ├── entity/ (Cart, CartItem, CartItemOption)
│   └── dto/
│       ├── request/ (CartItemAddRequest, CartItemUpdateRequest)
│       └── response/ (CartResponse, CartItemResponse, CartItemOptionResponse)
└── order/ # 주문 도메인
    ├── controller/OrderController.java
    ├── service/OrderService.java
    ├── repository/ (OrderRepository, OrderItemRepository, OrderItemOptionRepository)
    ├── entity/ (Order, OrderItem, OrderItemOption, OrderStatus)
    └── dto/
        ├── request/OrderRequest.java
        └── response/ (OrderResponse, OrderCancelResponse, OrderItemInfo, OrderOptionInfo, OrderStoreGroup)
</code></pre>
