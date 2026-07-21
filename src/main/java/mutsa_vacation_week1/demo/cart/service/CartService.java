package mutsa_vacation_week1.demo.cart.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.cart.entity.Cart;
import mutsa_vacation_week1.demo.cart.entity.CartItem;
import mutsa_vacation_week1.demo.cart.entity.CartItemOption;
import mutsa_vacation_week1.demo.cart.dto.request.CartItemAddRequest;
import mutsa_vacation_week1.demo.cart.dto.request.CartItemUpdateRequest;
import mutsa_vacation_week1.demo.cart.dto.response.CartItemResponse;
import mutsa_vacation_week1.demo.cart.dto.response.CartResponse;
import mutsa_vacation_week1.demo.cart.repository.CartItemRepository;
import mutsa_vacation_week1.demo.cart.repository.CartRepository;
import mutsa_vacation_week1.demo.global.apiPayload.code.CartErrorCode;
import mutsa_vacation_week1.demo.global.apiPayload.exception.CustomException;
import mutsa_vacation_week1.demo.menu.entity.Menu;
import mutsa_vacation_week1.demo.menu.repository.MenuRepository;
import mutsa_vacation_week1.demo.menuOption.entity.MenuOption;
import mutsa_vacation_week1.demo.menuOption.repository.MenuOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;

    // 담기
    @Transactional
    public CartItemResponse addCartItem(Long memberId, CartItemAddRequest request) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseGet(() -> cartRepository.save(new Cart(memberId)));

        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new CustomException(CartErrorCode.MENU_NOT_FOUND));

        CartItem cartItem = new CartItem(menu, request.getQuantity());

        // 1. 옵션이 있는 경우 한번에 조회하여 추가 (N+1 방지 및 NPE 예방)
        if (request.getMenuOptionId() != null && !request.getMenuOptionId().isEmpty()) {
            List<MenuOption> menuOptions = menuOptionRepository.findAllById(request.getMenuOptionId());
            for (MenuOption menuOption : menuOptions) {
                cartItem.addOption(new CartItemOption(menuOption));
            }
        }

        // 2. 장바구니에 아이템 추가 (양방향 연결)
        cart.addCartItem(cartItem);

        // 3. 저장 및 응답 반환
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return CartItemResponse.from(savedCartItem);
    }

    // 조회
    @Transactional
    public CartResponse getCart(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(CartErrorCode.CART_NOT_FOUND)); // 수정
        return CartResponse.from(cart);
    }

    // 수정 (수량 + 옵션)
    @Transactional
    public CartItemResponse updateCartItem(Long memberId, Long cartItemId, CartItemUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException(CartErrorCode.CART_ITEM_NOT_FOUND)); // 수정 (아래 참고)
        validateOwnership(cartItem, memberId);

        cartItem.changeQuantity(request.getQuantity());

        if (request.getMenuOptionId() != null) {
            cartItem.clearOptions();
            for (Long optionId : request.getMenuOptionId()) {
                MenuOption menuOption = menuOptionRepository.findById(optionId)
                        .orElseThrow(() -> new CustomException(CartErrorCode.MENU_OPTION_NOT_FOUND)); // 수정
                cartItem.addOption(new CartItemOption(menuOption));
            }
        }

        return CartItemResponse.from(cartItem);
    }

    // 삭제
    @Transactional
    public void deleteCartItem(Long memberId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException(CartErrorCode.CART_ITEM_NOT_FOUND)); // 수정
        validateOwnership(cartItem, memberId);

        cartItemRepository.deleteById(cartItemId);
    }

    private void validateOwnership(CartItem cartItem, Long memberId) {
        if (!cartItem.getCart().getMemberId().equals(memberId)) {
            throw new CustomException(CartErrorCode.CART_ACCESS_DENIED);
        }
    }
}

