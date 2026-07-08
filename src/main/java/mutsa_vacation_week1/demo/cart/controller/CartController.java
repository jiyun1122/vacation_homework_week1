package mutsa_vacation_week1.demo.cart.controller;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.cart.dto.request.CartItemAddRequest;
import mutsa_vacation_week1.demo.cart.dto.request.CartItemUpdateRequest;
import mutsa_vacation_week1.demo.cart.dto.response.CartItemResponse;
import mutsa_vacation_week1.demo.cart.dto.response.CartResponse;
import mutsa_vacation_week1.demo.cart.service.CartService;
import mutsa_vacation_week1.demo.global.apiPayload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 담기
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartItemResponse>> addItem(@RequestBody CartItemAddRequest request) {
        CartItemResponse response = cartService.addCartItem(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.onSuccess("장바구니 담기 성공", response));
    }

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@RequestParam Long memberId) {
        CartResponse response = cartService.getCart(memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess("장바구니 조회 성공", response));
    }

    // 장바구니 수정 (수량/옵션)
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateItem(
            @PathVariable Long cartItemId,
            @RequestBody CartItemUpdateRequest request) {
        CartItemResponse response = cartService.updateCartItem(cartItemId, request);
        return ResponseEntity.ok(ApiResponse.onSuccess("장바구니 수정 성공", response));
    }

    // 장바구니 삭제
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
