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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        CartItem cartItem = new CartItem(menu, request.getQuantity());
        cart.addCartItem(cartItem);

        if (request.getMenuOptionIds() != null) {
            for (Long optionId : request.getMenuOptionIds()) {
                MenuOption menuOption = menuOptionRepository.findById(optionId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옵션입니다."));
                cartItem.addOption(new CartItemOption(menuOption));
            }
        }

        cartItemRepository.save(cartItem);
        return CartItemResponse.from(cartItem);
    }

    // 조회
    public CartResponse getCart(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 없습니다."));
        return CartResponse.from(cart);
    }

    // 수정 (수량 + 옵션)
    @Transactional
    public CartItemResponse updateCartItem(Long memberId, Long cartItemId, CartItemUpdateRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("담긴 메뉴가 없습니다."));
        validateOwnership(cartItem, memberId);

        cartItem.changeQuantity(request.getQuantity());

        if (request.getMenuOptionIds() != null) {
            cartItem.clearOptions();
            for (Long optionId : request.getMenuOptionIds()) {
                MenuOption menuOption = menuOptionRepository.findById(optionId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 옵션입니다."));
                cartItem.addOption(new CartItemOption(menuOption));
            }
        }

        return CartItemResponse.from(cartItem);
    }

    // 삭제
    @Transactional
    public void deleteCartItem(Long memberId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("담긴 메뉴가 없습니다."));
        validateOwnership(cartItem, memberId);

        cartItemRepository.deleteById(cartItemId);
    }

    private void validateOwnership(CartItem cartItem, Long memberId) {
        if (!cartItem.getCart().getMemberId().equals(memberId)) {
            throw new CustomException(CartErrorCode.CART_ACCESS_DENIED);
        }
    }
}