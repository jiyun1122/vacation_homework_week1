# vacation_homework_week3
## 👥 1. 팀원 정보 및 역할 분담

| 이름 | 역할  | 담당 구현 기능 |
| :--- | :--- | :--- |
| **피윤혁** | **팀장 / Backend** | Swagger 설정, Cors 설정 개발 , 에러 해결 |
| **박수민** | **팀원 / Backend** | docker를 통한 CI/CD 구축 ,배포 도메인 관리, 토큰 처리 코드 개발, 담기 에러 해결 |
| **김지윤** | **팀원 / Backend** | Oauth 로그인 및 회원가입 API 개발 |


## 🎯 2. 과제 명세 대비 구현 현황 (Checklist)

## 1. 회원가입 및 로그인 구현

일반 회원가입/로그인에 더해 **카카오 소셜 로그인(OAuth2)** 까지 구현했습니다.
인증 방식은 **JWT Access Token(Bearer)** 방식을 사용하며, 프론트엔드와 백엔드가 완전히 분리된 구조(SPA)를 전제로 설계했습니다.

### 필수 구현 기능

- [x] 회원가입 (`POST /members`)
- [x] 로그인 (`POST /members/auth/login`)
- [x] JWT Access Token 발급
- [x] JWT 토큰 검증 (`JwtAuthenticationFilter`)
- [x] 인증이 필요한 API에 대한 접근 제어 (`SecurityConfig` + `JwtAuthenticationEntryPoint`/`JwtAccessDeniedHandler`)
- [x] 로그인한 사용자 정보 조회 (`GET /members/me`)
- [x] 비밀번호 암호화 (`PasswordEncoder`/`BCryptPasswordEncoder`)
- [x] 로그아웃 (`POST /members/auth/logout`, Stateless라 클라이언트 토큰 폐기 방식)

### 회원가입

회원가입 요청을 받아 회원 정보를 데이터베이스에 저장합니다.

- [x] `loginId` 중복 여부 확인 (`MEMBER_ALREADY_EXISTS`)
- [x] 비밀번호 평문 저장 금지, `PasswordEncoder`로 암호화 저장
- [x] 요청값 검증 및 적절한 에러 반환 (`@Valid`, `GlobalExceptionHandler`)
- [x] 회원가입 Request/Response DTO 분리 (`SignupRequest` / `MemberInfo`)
- [x] 회원가입 시 장바구니(Cart) 자동 생성

### 카카오 소셜 로그인 (추가 구현)

백엔드-프론트 분리 구조에 맞춰, **프론트가 카카오로부터 직접 받은 인가코드(code)를 백엔드 API로 전달**하고, 백엔드가 카카오 서버와 통신해 토큰 교환 및 사용자 조회까지 대신 처리하는 방식으로 구현했습니다.

- [x] 인가코드 기반 카카오 로그인 API (`POST /members/auth/kakao`)
- [x] 카카오 토큰 교환/사용자 정보 조회 (`KakaoOAuthClient`)
- [x] 신규 사용자는 자동 회원가입, 기존 사용자는 로그인 처리 (`MemberService#loginOrSignUpKakaoMember`)
- [x] 카카오로 가입한 계정은 일반 로그인(ID/PW) 시도 시 차단 (`SOCIAL_ACCOUNT_LOGIN_NOT_ALLOWED`)
- [x] 로그인/가입 성공 시 자체 JWT 발급 → 이후 모든 API는 동일한 Bearer 토큰 방식으로 인증

### 크레딧(포인트) 관리

- [x] 크레딧 충전 (`POST /api/v1/members/me/credit/charge`)
- [x] 크레딧 조회 (`GET /api/v1/members/me/credit`)
- [x] 크레딧 차감 (`POST /members/credit/deduct`, 결제 완료 시 사용)
- [x] 잔액 부족 시 예외 처리

### 가게 · 메뉴 · 메뉴 옵션

- [x] 가게 목록 조회 (`GET /stores`)
- [x] 메뉴 목록/상세 조회 (`GET /menu`, `GET /menu/{menuId}`)
- [x] 메뉴 옵션 생성/수정/삭제 (`POST`, `PATCH`, `DELETE /menuOption/**`)

### 장바구니

- [x] 장바구니 담기 (`POST /api/v1/carts/items`)
- [x] 장바구니 조회 (`GET /api/v1/carts`)
- [x] 장바구니 항목 수정 (`PATCH /api/v1/carts/items/{cartItemId}`)
- [x] 장바구니 항목 삭제 (`DELETE /api/v1/carts/items/{cartItemId}`)
- [x] 장바구니 담기/조회 시 발생하던 `MultipleBagFetchException` 500 에러 해결

### 주문

- [x] 주문 생성 (`POST /orders`, 장바구니 항목 기반)
- [x] 주문 취소 및 크레딧 환불 (`PATCH /orders/{orderId}/cancel`)
- [x] 존재하지 않는/이미 사용된 리소스에 대한 예외 처리 (404/400)

### 공통 · 인프라

- [x] 공통 응답 포맷 `ApiResponse<T>` (성공/실패 규격 통일)
- [x] 전역 예외 처리 (`GlobalExceptionHandler`, `CustomException` + 도메인별 `ErrorCode`)
- [x] CORS 설정 (로컬/배포 프론트 도메인 허용)
- [x] Swagger(OpenAPI) API 문서화 및 JWT Bearer 인증 스킴 등록 (`SwaggerConfig`)
- [x] Docker 이미지 빌드 + GitHub Actions를 통한 EC2 자동 배포 (`Dockerfile`, `.github/workflows/deploy.yml`)
- [x] Nginx 리버스 프록시 뒤에서 HTTPS 요청 인식 안 되던 문제 수정 (Forwarded Header 처리)
- [x] API 서브도메인 분리에 따른 CORS 허용 목록 갱신

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
│   │   │   ├── AuthErrorCode.java # 카카오 OAuth 관련 에러
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
│       │   ├── PasswordEncoderConfig.java # PasswordEncoder 빈 (순환참조 방지용 분리)
│       │   ├── SwaggerConfig.java # ✅ Swagger/OpenAPI + JWT Bearer 인증 설정
│       │   └── JacksonConfig.java
│       ├── handler/
│       │   ├── JwtAuthenticationEntryPoint.java # 401 처리
│       │   └── JwtAccessDeniedHandler.java # 403 처리
│       ├── jwt/
│       │   ├── JwtTokenProvider.java # ✅ 토큰 생성/검증
│       │   ├── JwtAuthenticationFilter.java # ✅ 요청마다 토큰 검사
│       │   ├── JwtAuthenticationEntryPoint.java
│       │   ├── JwtAuthExceptionResponseWriter.java
│       │   └── JwtProperties.java
│       ├── oauth2/ # ✅ 카카오 소셜 로그인
│       │   ├── KakaoOAuthClient.java # 프론트가 넘긴 인가코드로 카카오 토큰/사용자 정보 조회
│       │   ├── KakaoTokenResponse.java
│       │   ├── CustomOAuth2User.java
│       │   ├── CustomOAuth2UserService.java
│       │   ├── OAuth2LoginSuccessHandler.java
│       │   └── OAuth2LoginFailureHandler.java
│       └── userdetails/
│           └── CustomUserDetails.java
├── member/ # 회원 도메인
│   ├── controller/MemberController.java
│   ├── service/MemberService.java
│   ├── repository/MemberRepository.java
│   ├── entity/ (Member, Provider) # Provider: LOCAL / KAKAO 구분
│   └── dto/
│       ├── request/ (SignupRequest, LoginRequest, KakaoLoginRequest, CreditChargeRequest, CreditDeductRequest)
│       └── response/ (LoginResponse, MemberInfo, CreditResponse, CreditChargeResponse, CreditDeductResponse)
├── store/ # 가게 도메인
│   ├── controller/StoreController.java
│   ├── service/StoreService.java
│   ├── repository/StoreRepository.java
│   ├── entity/Store.java
│   └── dto/response/ (StoreListResponse, StoreSummaryResponse)
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

<br>

## 🚀 4. 배포 및 인프라

- **CI/CD**: GitHub Actions (`.github/workflows/deploy.yml`) → `main` 브랜치 push 시 Gradle 빌드 → Docker 이미지 빌드/푸시(Docker Hub) → EC2 SSH 접속 후 `docker compose pull && up -d`로 무중단 배포
- **컨테이너**: `Dockerfile` (`eclipse-temurin:21-jre` 베이스, Fat Jar 실행)
- **리버스 프록시**: Nginx 뒤에서 동작하며 HTTPS Forwarded 헤더 인식 문제 해결
- **DB**: AWS RDS(MySQL) 사용, `application.yml`에서 환경변수(`DB_USERNAME`, `DB_PASSWORD`)로 주입
