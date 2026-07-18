package mutsa_vacation_week1.demo.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.global.security.AuthMember;
import mutsa_vacation_week1.demo.member.dto.request.CreditDeductRequest;
import mutsa_vacation_week1.demo.member.dto.request.LoginRequest;
import mutsa_vacation_week1.demo.member.dto.request.SignupRequest;
import mutsa_vacation_week1.demo.member.dto.request.CreditChargeRequest;
import mutsa_vacation_week1.demo.member.dto.response.*;
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
@Tag(name = "Members", description = "회원가입, 로그인, 로그아웃")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 이름을 입력받아 회원을 등록합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "MEMBER409_1 - 이미 존재하는 loginId 입니다",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/members")
    public ResponseEntity<ApiResponse<MemberInfo>> signup(@Valid @RequestBody SignupRequest request) {
        MemberInfo result = memberService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess("회원가입 성공", result));
    }

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

    @Operation(summary = "크레딧 차감", description = "결제 완료 시 크레딧을 차감합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "크레딧 차감 성공",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "MEMBER400_1 - 크레딧 잔액이 부족합니다",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"success\": false, \"message\": \"크레딧 잔액이 부족합니다.\"}"
                            )
                    ))
    })
    @PostMapping("/members/credit/deduct")
    public ResponseEntity<ApiResponse<CreditDeductResponse>> deductCredit(
            @AuthenticationPrincipal AuthMember authMember,
            @Valid @RequestBody CreditDeductRequest request
    ) {
        CreditDeductResponse result = memberService.deductCredit(authMember.memberId(), request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.onSuccess("크레딧 차감 성공", result));
    }

}
