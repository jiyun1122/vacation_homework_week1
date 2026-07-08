# vacation_homework_week1
## 👥 1. 팀원 정보 및 역할 분담

| 이름 | 역할  | 담당 구현 기능 |
| :--- | :--- | :--- |
| **피윤혁** | **팀장 / Backend** | 카트 API 개발, 크래딧 API 개발, API 명세서 작성 |
| **박수민** | **팀원 / Backend** | 멤버 API 개발, 오더 API 개발 , API 명세서 작성 |
| **김지윤** | **팀원 / Backend** | 메뉴 API 개발, 메뉴옵션 API 개발, API 명세서 작성 |

<br>

## 🎯 2. 과제 명세 대비 구현 현황 (Checklist)

- [x] **요구사항 1:** 피그마 화면 기반 ERD 설계
- [x] **요구사항 2:** 피그마에 필요한 기능 API 명세서에 작성 
- [x] **요구사항 3:** API 명세서 기반 API 도메인별로 개발 
- [x] **요구사항 4:** 에러 공통 포멧,성공 코드 정의및 개발

<br>

## 📂 3. 프로젝트 폴더 구조 (Project Structure)

```text
src/main/java/mutsa_vacation_week1/demo
│
├── DemoApplication.java        
│
├── global/                     # 프로젝트 전역 공통 설정 및 예외 처리
│   ├── apiPayload/             # API 공통 응답 포맷
│   ├── code/                   # 성공/에러 도메인별 응답 코드 정의
│   ├── exception/              # 커스텀 예외 및 전역 예외 처리
│   └── config/                 # CORS 설정
│
├── member/                     # 회원 도메인 
│   ├── controller/             # API 엔드포인트 매핑
│   ├── dto/                    # Request / Response 파일 정의 
│   ├── entity/                 # DB 매핑 객체 
│   ├── repository/             # DB 데이터 접근 계층
│   └── service/                # 비즈니스 로직 처리
│
├── menu/                       # 메뉴 관련 도메인
│   └── controller, dto, entity, repository, service
│
├── menuOption/                 # 메뉴 옵션 관련 도메인
│   └── controller, dto, entity, repository, service
│
├── cart/                       # 장바구니 관련 도메인
│   └── controller, dto, entity, repository, service
│
└── order/                      # 주문 및 결제 관련 도메인
    └── controller, dto, entity, repository, service
