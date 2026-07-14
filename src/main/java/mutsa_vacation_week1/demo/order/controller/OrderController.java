package mutsa_vacation_week1.demo.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.global.apiPayload.exception.CustomException;
import mutsa_vacation_week1.demo.global.apiPayload.code.MemberErrorCode;
import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.member.repository.MemberRepository;
import mutsa_vacation_week1.demo.order.dto.request.OrderRequest;
import mutsa_vacation_week1.demo.order.dto.response.OrderCancelResponse;
import mutsa_vacation_week1.demo.order.dto.response.OrderResponse;
import mutsa_vacation_week1.demo.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Orders", description = "주문 생성, 취소 API")
public class OrderController {

    private final OrderService orderService;
    private final MemberRepository memberRepository;

    @Operation(summary = "주문 생성", description = "장바구니 항목 id 목록을 받아 주문을 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201", description = "주문 생성 성공",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "ORDER404_2 - 존재하지 않는 cartItemId입니다.",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "ORDER400_1 - 이미 사용된 cartItemId입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestParam Long memberId,
            @Valid @RequestBody OrderRequest request
    ) {

        OrderResponse result = orderService.createOrder(memberId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess("주문 생성 성공", result));

    }

    @Operation(summary = "주문 취소", description = "주문을 취소하고 크레딧을 환불합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "주문 취소 성공",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "ORDER404_1 - 존재하지 않는 orderId입니다.",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "ORDER400_2 - 이미 취소된 주문입니다.",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderCancelResponse>> cancelOrder(
            @RequestParam Long memberId,
            @PathVariable Long orderId
    ) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        OrderCancelResponse result = orderService.cancelOrder(member, orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.onSuccess("주문 취소 성공", result));

    }

























}
