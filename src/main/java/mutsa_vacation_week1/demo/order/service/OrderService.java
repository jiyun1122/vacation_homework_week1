package mutsa_vacation_week1.demo.order.service;

import lombok.RequiredArgsConstructor;
import mutsa_vacation_week1.demo.global.apiPayload.exception.CustomException;
import mutsa_vacation_week1.demo.global.apiPayload.exception.OrderErrorCode;
import mutsa_vacation_week1.demo.member.entity.Member;
import mutsa_vacation_week1.demo.order.dto.*;
import mutsa_vacation_week1.demo.order.entity.Order;
import mutsa_vacation_week1.demo.order.entity.OrderItem;
import mutsa_vacation_week1.demo.order.entity.OrderItemOption;
import mutsa_vacation_week1.demo.order.entity.OrderStatus;
import mutsa_vacation_week1.demo.order.repository.OrderItemOptionRepository;
import mutsa_vacation_week1.demo.order.repository.OrderItemRepository;
import mutsa_vacation_week1.demo.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemOptionRepository orderItemOptionRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public OrderResponse createOrder(Member member, OrderRequest request) {
        List<CartItem> cartItems = request.getCartItemIds().stream()
                .map(id -> cartItemRepository.findById(id)
                        .orElseThrow(() -> new CustomException(OrderErrorCode.CART_ITEM_ID_NOT_FOUND)))
                .toList();

        int totalPrice = calculateTotalPrice(cartItems);

        Order order = new Order(member, totalPrice);
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = createOrderItem(savedOrder, cartItem);
            orderItems.add(orderItem);
        }

        member.deductCredit(totalPrice);

        List<OrderStoreGroup> storeGroups = groupByStore(orderItems);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalPrice(),
                savedOrder.getStatus().name(),
                storeGroups
        );

    }

    @Transactional
    public OrderCancelResponse cancelOrder(Member member, Long orderId) {

        Order order = orderRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new CustomException(OrderErrorCode.ORDER_ALREADY_CANCELED);
        }

        order.cancel();
        member.chargeCredit(order.getTotalPrice());

        return new OrderCancelResponse(
                order.getId(),
                order.getStatus().name(),
                order.getTotalPrice()
        );
    }

    private int calculateTotalPrice(List<CartItem> cartItems) {

        int total = 0;
        for (CartItem cartItem : cartItems) {
            int menuPrice = cartItem.getMenu().getPrice();
            int optionPrice = cartItem.getCartItemOptions().stream()
                    .mapToInt(option -> option.getMenuOption().getPrice())
                    .sum();
            total += (menuPrice + optionPrice) * cartItem.getQuantity();
        }
        return total;

    } // 나중에 Cart받고 바꿔야함

    private OrderItem createOrderItem(Order order, CartItem cartItem) {

        Menu menu = cartItem.getMenu();

        OrderItem orderItem = new OrderItem(
                order,
                menu,
                cartItem.getQuantity(),
                menu.getPrice()
        );
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        for (CartItemOption cartItemOption : cartItem.getCartItemOptions()) {
            OrderItemOption orderItemOption = new OrderItemOption(
                    savedOrderItem,
                    cartItemOption.getMenuOption(),
                    cartItemOption.getMenuOption().getPrice()
            );
            orderItemOptionRepository.save(orderItemOption);
        }

        return savedOrderItem;
    }

    private List<OrderStoreGroup> groupByStore(List<OrderItem> orderItems) {

        Map<Long, List<OrderItem>> groupedByStoreId = orderItems.stream()
                .collect(Collectors.groupingBy(oi -> oi.getMenu().getStore().getId()));

        List<OrderStoreGroup> result = new ArrayList<>();

        for (Map.Entry<Long, List<OrderItem>> entry : groupedByStoreId.entrySet()) {
            List<OrderItem> items = entry.getValue();
            String storeName = items.get(0).getMenu().getStore().getName();

            List<OrderItemInfo> itemInfos = items.stream()
                    .map(this::toOrderItemInfo)
                    .toList();

            result.add(new OrderStoreGroup(entry.getKey(), storeName, itemInfos));
        }

        return result;

    }

    private OrderItemInfo toOrderItemInfo(OrderItem orderItem) {

        List<OrderOptionInfo> optionInfos = orderItemOptionRepository.findByOrderItem(orderItem).stream()
                .map(opt -> new OrderOptionInfo(
                        opt.getMenuOption().getId(),
                        opt.getMenuOption().getContent(),
                        opt.getOrderPrice()
                ))
                .toList();

        return new OrderItemInfo(
                orderItem.getId(),
                orderItem.getMenu().getId(),
                orderItem.getMenu().getName(),
                orderItem.getQuantity(),
                orderItem.getOrderPrice(),
                optionInfos
        );

    }

}
