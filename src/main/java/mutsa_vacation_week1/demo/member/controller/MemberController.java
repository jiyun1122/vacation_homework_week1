package mutsa_vacation_week1.demo.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.global.security.AuthMember;
import mutsa_vacation_week1.demo.member.dto.request.LoginRequest;
import mutsa_vacation_week1.demo.member.dto.request.CreditChargeRequest;
import mutsa_vacation_week1.demo.member.dto.response.CreditChargeResponse;
import mutsa_vacation_week1.demo.member.dto.response.CreditResponse;
import mutsa_vacation_week1.demo.member.dto.response.LoginResponse;
import mutsa_vacation_week1.demo.member.dto.response.MemberInfo;
import mutsa_vacation_week1.demo.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@Tag(name = "Members", description = "로그인, 로그아웃")
public class MemberController {

    private final MemberService memberService;
  

    @Operation(summary = "로그인", description = "아이디, 비밀번호로 로그인합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "MEMBER401_1 - 아이디 또는 비밀번호가 일치하지 않습니다",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/members/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse result = memberService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.onSuccess("로그인 성공", result));
    }

    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    @PostMapping("/members/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        memberService.logout();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.onSuccess("로그아웃 성공", null));
    }

    @Operation(summary = "내 정보 조회", description = "인증된 사용자의 정보를 조회합니다.")
    @GetMapping("/members/me")
    public ResponseEntity<ApiResponse<MemberInfo>> getMyInfo(@AuthenticationPrincipal AuthMember authMember) {
        MemberInfo result = memberService.getMyInfo(authMember.memberId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.onSuccess("내 정보 조회 성공", result));
    }

     // 크레딧 충전
    @PostMapping("/api/v1/members/me/credit/charge")
    public ResponseEntity<CreditChargeResponse> chargeCredit(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestBody CreditChargeRequest request) {

        CreditChargeResponse response = memberService.chargeCredit(authMember.memberId(), request.getAmount());
        return ResponseEntity.ok(response);
    }

    // 크레딧 조회
    @GetMapping("/api/v1/members/me/credit")
    public ResponseEntity<CreditResponse> getCredit(@AuthenticationPrincipal AuthMember authMember) {
        CreditResponse response = memberService.getCredit(authMember.memberId());
        return ResponseEntity.ok(response);
    }

}
