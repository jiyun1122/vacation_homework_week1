package mutsa_vacation_week1.demo.cart.controller;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.cart.dto.request.CartItemAddRequest;
import mutsa_vacation_week1.demo.cart.dto.request.CartItemUpdateRequest;
import mutsa_vacation_week1.demo.cart.dto.response.CartItemResponse;
import mutsa_vacation_week1.demo.cart.dto.response.CartResponse;
import mutsa_vacation_week1.demo.cart.service.CartService;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import mutsa_vacation_week1.demo.global.security.AuthMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 담기
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartItemResponse>> addItem(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestBody CartItemAddRequest request) {
        CartItemResponse response = cartService.addCartItem(authMember.memberId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess("장바구니 담기 성공", response));
    }

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@AuthenticationPrincipal AuthMember authMember) {
        CartResponse response = cartService.getCart(authMember.memberId());
        return ResponseEntity.ok(ApiResponse.onSuccess("장바구니 조회 성공", response));
    }

    // 장바구니 수정 (수량/옵션)
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateItem(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long cartItemId,
            @RequestBody CartItemUpdateRequest request) {
        CartItemResponse response = cartService.updateCartItem(authMember.memberId(), cartItemId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess("장바구니 수정 성공", response));
    }

    // 장바구니 삭제
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteItem(
            @AuthenticationPrincipal AuthMember authMember,
            @PathVariable Long cartItemId) {
        cartService.deleteCartItem(authMember.memberId(), cartItemId);
        return ResponseEntity.noContent().build();
    }
}
