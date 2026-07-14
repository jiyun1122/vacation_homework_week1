---
name: 병합 후 에러 수정 및 구조 리팩토링
overview: 브랜치 병합 과정에서 서로 다른 팀(menu, member/order, cart)이 독립적으로 만든 코드가 통합되지 않아 발생한 컴파일 에러를 모두 수정하고, 에러/응답 포맷을 apiPayload 체계로 통일하며, 전 도메인의 패키지 구조를 controller/service/repository/entity/dto(request,response) 레이어드 구조로 표준화한다.
todos:
  - id: fix-memberservice
    content: "MemberService.java: import/타입 오류 수정 (Cart, CartRepository, ErrorCode->MemberErrorCode)"
    status: completed
  - id: fix-cartservice
    content: "CartService.java: Menu/MenuOption/Repository import 추가, java.awt import 제거"
    status: completed
  - id: fix-orderservice
    content: "OrderService.java: CartItem 관련 import 추가, getStore() -> getStoreId()/getStoreName() 수정, java.awt import 제거"
    status: completed
  - id: fix-cartitem-import
    content: CartItem.java의 잘못된 Menu import 경로 수정
    status: completed
  - id: fix-orderitem-imports
    content: OrderItem.java, OrderItemOption.java에 Menu/MenuOption import 추가
    status: completed
  - id: unify-error-system
    content: global.exception 삭제하고 global.apiPayload 체계로 통일 (MenuErrorCode, CommonErrorCode 신설, GlobalExceptionHandler에 fallback 추가)
    status: completed
  - id: update-menu-services
    content: MenuService/MenuOptionService를 apiPayload CustomException/MenuErrorCode로 교체, 컨트롤러 응답 ApiResponse로 래핑
    status: completed
  - id: move-successcode
    content: SuccessCode를 global.apiPayload.code로 이동
    status: completed
  - id: remove-duplicate-member
    content: 미사용 member/domain/Member.java 삭제
    status: completed
  - id: restructure-menu
    content: menu, menuOption 패키지를 controller/service/repository/entity/dto 구조로 재구성
    status: completed
  - id: restructure-cart
    content: cart의 domain 패키지를 entity로 리네임
    status: completed
  - id: restructure-member-order-dto
    content: member, order의 dto를 request/response 하위 패키지로 재구성
    status: completed
isProject: false
---

# 병합 후 에러 정리 + 폴더/코드 리팩토링 플랜

## 배경 (근본 원인)
`git log`상 menu(jiyun), member/order(soomin), cart(yunhyeok) 브랜치가 서로 다른 컨벤션으로 독립 개발된 뒤, 서로의 실제 코드를 참조·검증하지 않고 병합되어 import 누락/오기, 타입 불일치, 중복된 아키텍처(에러 처리 2벌, Member 엔티티 2벌)가 남아있음. Java/Gradle이 로컬에 없어 실제 컴파일은 못 돌렸지만, 코드 리딩으로 아래 문제들을 모두 특정함.

## 1단계 — 컴파일 에러 수정 (최우선, 앱이 뜨게 만들기)

- [member/service/MemberService.java](src/main/java/mutsa_vacation_week1/demo/member/service/MemberService.java)
  - `global.exception.ErrorCode.MEMBER_ALREADY_EXISTS` / `LOGIN_FAILED` 참조 → 해당 enum엔 메뉴 코드만 있어 심볼 없음. `global.apiPayload.exception.CustomException` + `MemberErrorCode`로 교체
  - `Cart`, `CartRepository` import 누락 (필드/생성자에서 사용 중)
  - `new Cart(savedMember)` — `Cart` 생성자는 `Long memberId`를 받는데 `Member` 객체를 넘김 → `new Cart(savedMember.getId())`로 수정
- [cart/service/CartService.java](src/main/java/mutsa_vacation_week1/demo/cart/service/CartService.java)
  - `Menu`, `MenuOption`, `MenuRepository`, `MenuOptionRepository` import 전부 누락 → 추가
  - 불필요한 `import java.awt.*;` 제거
- [order/service/OrderService.java](src/main/java/mutsa_vacation_week1/demo/order/service/OrderService.java)
  - `CartItemRepository`, `CartItem`, `CartItemOption`, `Menu` import 누락 → 추가
  - 불필요한 `import java.awt.*;` 제거
  - `groupByStore()`에서 `oi.getMenu().getStore().getId()/getName()` 호출 — `Menu` 엔티티엔 `getStore()`가 없고 `storeId`/`storeName` 필드만 존재 (`OrderStoreGroup`도 이 필드명과 일치) → `getMenu().getStoreId()` / `getMenu().getStoreName()`으로 수정
- [cart/domain/CartItem.java](src/main/java/mutsa_vacation_week1/demo/cart/domain/CartItem.java)
  - `import mutsa_vacation_week1.demo.menu.domain.Menu;` — 존재하지 않는 패키지 (`Menu`는 `menu` 패키지 바로 아래 위치) → `mutsa_vacation_week1.demo.menu.Menu`로 수정
- [order/entity/OrderItem.java](src/main/java/mutsa_vacation_week1/demo/order/entity/OrderItem.java), [order/entity/OrderItemOption.java](src/main/java/mutsa_vacation_week1/demo/order/entity/OrderItemOption.java)
  - `Menu`, `MenuOption` import 누락 → 추가

## 2단계 — 에러/응답 포맷 통일 (`global.apiPayload` 체계로 일원화)

현재 `global.exception.*`(flat `ErrorCode`, menu 전용)와 `global.apiPayload.*`(`BaseErrorCode` 인터페이스 + 도메인별 enum, member/order 사용) 두 체계가 공존. **`apiPayload` 체계로 통일**하기로 결정.

- `global/apiPayload/code/BaseErrorCode.java` 유지
- 도메인별 ErrorCode enum들을 `global/apiPayload/code/` 밑으로 모음 (현재 `exception` 패키지에 흩어져 있음): `MemberErrorCode`, `OrderErrorCode` 이동
- 신규 `global/apiPayload/code/MenuErrorCode.java` 생성 — 기존 `global.exception.ErrorCode`의 `MENU_NOT_FOUND`(`MENU404_1`), `MENU_OPTION_NOT_FOUND`(`MENU404_2`)를 `BaseErrorCode` 구현체로 이관
- 신규 `global/apiPayload/code/CommonErrorCode.java` 생성 — `INTERNAL_SERVER_ERROR`(`COMMON500_1`) 이관
- [global/apiPayload/exception/GlobalExceptionHandler.java](src/main/java/mutsa_vacation_week1/demo/global/apiPayload/exception/GlobalExceptionHandler.java)에 `Exception.class` 처리용 핸들러 추가 (현재 apiPayload 쪽엔 fallback 핸들러가 없음) — `CommonErrorCode.INTERNAL_SERVER_ERROR` 사용
- `global/exception/` 패키지 전체 삭제 (`ErrorCode`, `ErrorResponse`, `CustomException`, `GlobalExceptionHandler`)
- [menu/MenuService.java](src/main/java/mutsa_vacation_week1/demo/menu/MenuService.java), [menuOption/MenuOptionService.java](src/main/java/mutsa_vacation_week1/demo/menuOption/MenuOptionService.java)에서 `global.exception.*` → `global.apiPayload.exception.CustomException` + `MenuErrorCode`로 교체
- `global/response/SuccessCode.java` → `global/apiPayload/code/SuccessCode.java`로 이동 (같은 응답 체계 패키지로 정리), 참조하는 [menuOption/dto/MenuOptionDeleteResponse.java](src/main/java/mutsa_vacation_week1/demo/menuOption/dto/MenuOptionDeleteResponse.java) import 경로 수정
- 일관성을 위해 `MenuController`, `MenuOptionController`의 응답도 `ApiResponse<T>`로 감싸도록 수정 (현재 raw DTO 반환 중, member/order/cart는 이미 컨트롤러 레벨에서 감쌈 — cart는 현재 raw 반환이라 이 부분도 함께 통일)

## 3단계 — 중복 제거

- `member/domain/Member.java` 삭제 (미사용 고아 엔티티, 실제 사용되는건 `member/entity/Member.java`)

## 4단계 — 폴더 구조 표준화 (레이어드: controller/service/repository/entity/dto/request,response)

모든 도메인을 `member`/`order`와 동일한 레이어드 구조로 통일.

- **menu** (현재 flat) → `menu/controller/MenuController.java`, `menu/service/MenuService.java`, `menu/repository/MenuRepository.java`, `menu/entity/Menu.java`, `menu/dto/response/{MenuDetailResponse,MenuListResponse,MenuSummaryResponse}.java`
- **menuOption** (현재 flat) → `menuOption/controller/...`, `menuOption/service/...`, `menuOption/repository/...`, `menuOption/entity/MenuOption.java`, `menuOption/dto/request/{MenuOptionCreateRequest,MenuOptionUpdateRequest}.java`, `menuOption/dto/response/{MenuOptionResponse,MenuOptionDeleteResponse}.java`
- **cart** (현재 `domain/`) → `domain` 패키지를 `entity`로 리네임 (`Cart`, `CartItem`, `CartItemOption`), controller/service/repository/dto는 이미 올바른 구조라 유지
- **member** — `dto/` 밑 플랫하게 있는 `SignupRequest.java`, `LoginRequest.java` → `dto/request/`로, `MemberInfo.java`(응답용) → `dto/response/`로 이동. `CreditChargeRequest`/`CreditChargeResponse`/`CreditResponse`는 이미 올바른 위치
- **order** — `dto/`에 플랫하게 섞여있는 `OrderRequest.java`(요청) → `dto/request/`로, `OrderResponse`, `OrderCancelResponse`, `OrderItemInfo`, `OrderOptionInfo`, `OrderStoreGroup`(전부 응답 관련) → `dto/response/`로 이동

각 이동마다 패키지 선언(`package ...`) 및 해당 클래스를 참조하는 모든 파일의 import 문을 함께 수정.

## 진행 순서

1단계(컴파일 에러 수정)만 완료해도 앱이 기동 가능한 상태가 됨. 이후 2~4단계는 순서대로 진행하되 각 단계 후 관련 파일들의 import를 전수 점검. 전 과정에서 Java 툴체인이 로컬에 없어 `gradlew compileJava`로 최종 검증은 못하므로, 각 클래스의 참조 관계를 교차 확인하는 방식으로 꼼꼼히 진행.
