package mutsa_vacation_week1.demo.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.member.dto.*;
import mutsa_vacation_week1.demo.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
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
                    description = "MEMBER409_1 - 이미 존재하는 login_id 입니다",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
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
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<MemberInfo>> login(@Valid @RequestBody LoginRequest request) {
        MemberInfo result = memberService.login(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.onSuccess("로그인 성공", result));
    }

    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        memberService.logout();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.onSuccess("로그아웃 성공", null));
    }
    
     // 크레딧 충전
    @PostMapping("/credit/charge")
    public ResponseEntity<CreditChargeResponse> chargeCredit(
            @RequestParam Long memberId,
            @RequestBody CreditChargeRequest request) {

        CreditChargeResponse response = memberService.chargeCredit(memberId, request.getAmount());
        return ResponseEntity.ok(response);
    }

    // 크레딧 조회
    @GetMapping("/credit")
    public ResponseEntity<CreditResponse> getCredit(@RequestParam Long memberId) {
        CreditResponse response = memberService.getCredit(memberId);
        return ResponseEntity.ok(response);
    }

}
