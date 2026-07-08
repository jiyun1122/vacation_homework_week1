package mutsa_vacation_week1.demo.cart.controller;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.cart.dto.request.CartItemAddRequest;
import mutsa_vacation_week1.demo.cart.dto.request.CartItemUpdateRequest;
import mutsa_vacation_week1.demo.cart.dto.response.CartItemResponse;
import mutsa_vacation_week1.demo.cart.dto.response.CartResponse;
import mutsa_vacation_week1.demo.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 담기
    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addItem(@RequestBody CartItemAddRequest request) {
        CartItemResponse response = cartService.addCartItem(request);
        return ResponseEntity.ok(response);
    }

    // 장바구니 조회
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestParam Long memberId) {
        CartResponse response = cartService.getCart(memberId);
        return ResponseEntity.ok(response);
    }

    // 장바구니 수정 (수량/옵션)
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateItem(
            @PathVariable Long cartItemId,
            @RequestBody CartItemUpdateRequest request) {
        CartItemResponse response = cartService.updateCartItem(cartItemId, request);
        return ResponseEntity.ok(response);
    }

    // 장바구니 삭제
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
}
