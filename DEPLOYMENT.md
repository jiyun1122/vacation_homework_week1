# 배포 완료했어요 (박수민)

배포 주소는 여기입니다.

```
https://mutsa-vacation-team1.site
```

HTTPS 적용해놨고, http로 들어와도 알아서 https로 리다이렉트되게 해놨어요.

---

## 1. 회원가입 / 로그인 API

실제로 배포 서버에서 다 테스트해봤고 정상 동작합니다.

| 기능 | Method | URL | 인증 |
|---|---|---|---|
| 회원가입 | POST | `/members` | 필요 없음 |
| 로그인 | POST | `/members/auth/login` | 필요 없음 |
| 내 정보 조회 | GET | `/members/me` | 토큰 필요 |

회원가입 요청 예시:
```json
{"login_id":"testuser","password":"password1234","name":"홍길동"}
```

로그인 응답 예시:
```json
{
  "success": true,
  "message": "로그인 성공",
  "result": {
    "access_token": "eyJhbGci...",
    "token_type": "Bearer",
    "member": {"member_id": 1, "login_id": "testuser", "name": "홍길동", "credit": 0}
  }
}
```

인증 필요한 API 부를 땐 헤더에 이렇게 붙이면 돼요.
```
Authorization: Bearer {access_token}
```

**프론트 쪽은 꼭 봐주세요**: 전역 Jackson 설정을 SNAKE_CASE로 정해놔서 (팀에서 이유 있어서 결정한 거 맞아요), `loginId`가 아니라 `login_id`, `memberId`가 아니라 `member_id` 이런 식으로 응답/요청 다 스네이크 케이스로 나갑니다. 캐멀케이스로 보내면 안 먹어요.

---

## 2. 프론트 연동할 때 CORS 관련

지금 CORS 허용 목록엔 `localhost:3000`, `localhost:5173`만 등록돼있어요. 프론트 실제로 배포하면 그 주소 CORS에 추가해야 되는데, 그건 제가 임의로 안 건드리고 남겨뒀습니다. 필요하면 얘기해주세요, 바로 추가해드릴게요.

---

## 3. 인프라/보안 쪽 정리

- 서버는 systemd로 등록해놔서 EC2 재부팅되도 알아서 다시 뜹니다
- DB 계정이랑 JWT 시크릿 같은 민감 정보는 서버 안 별도 파일(`/etc/vacation-demo.env`)에 넣어놨고 깃허브엔 안 올라갑니다
- 8080 포트는 외부에서 바로 못 들어오고, Nginx(80/443) 거쳐서만 접근되게 해놨어요
