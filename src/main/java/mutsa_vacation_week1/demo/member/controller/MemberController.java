package mutsa_vacation_week1.demo.member.controller;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.member.dto.request.CreditChargeRequest;
import mutsa_vacation_week1.demo.member.dto.response.CreditChargeResponse;
import mutsa_vacation_week1.demo.member.dto.response.CreditResponse;
import mutsa_vacation_week1.demo.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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