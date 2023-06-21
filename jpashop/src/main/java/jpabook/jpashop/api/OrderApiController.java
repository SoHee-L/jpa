package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    //V1 처럼 entity 를 직접 호출하면 안됨.
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            //주문과 관련된 orderItems 를 다가져와서 돌리면서 이름을 가져와야 되는데 그래서 Item 도 다 초기화를 함.
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    //api 를 직접 노출하는게 아닌 dto 로 변환해서 노출하는 방법.
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

    //성능이 안나와서 entity 를 dto 로 변환하는 작업을 fetch join 으로 최적화 하는 방법.
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        for (Order order : orders) {
            System.out.println("order ref= " + order + " id=" + order.getId());
        }

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;

    }

    //페이징 처리가 가능한 방법 (이 방식을 굉장히 선호)
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset",
            defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue
                                                = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

    //Collection 이 있을 때 Dto 로 직접 조회하는 방법
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    //Collection 상황에서 최적화 하는 방법
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    //쿼리 한번으로 해결하는 방법
    @GetMapping("/api/v6/orders")
    //public List<OrderFlatDto> ordersV6() { => 원래 반환해야되는 값.
    public List<OrderQueryDto> ordersV6() {
        //spec 이 안맞아서 OrderFlatDto 반환이 아닌 OrderQueryDto 를 반환하고 싶으면 직접 중복을 거르면 됨.
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        //flats 를 가지고 루프를 넘겨서 돌림. 그렇게 OrderFlatDto -> OrderQueryDto 로 변환해주는 코드
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }


    @Getter
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        //OrderItem 조차도 dto 로 바꿔야됨. 바꾸지 않으면 나중에 api 수정시 화면이 다 망가져버림.
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();

            //order.getOrderItems().stream().forEach(o->o.getItem().getName());
            //orderItems=order.getOrderItems();
            //위와 같은 과정을 dto 로 변환해줌
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }

        //orderItem 을 dto 로 변환한 과정.
        @Getter
        static class OrderItemDto {
            private String itemName; //상품 명
            private int orderPrice; //주문 가격
            private int count; //주문 수량

            public OrderItemDto(OrderItem orderItem) {
                itemName = orderItem.getItem().getName();
                orderPrice = orderItem.getOrderPrice();
                count = orderItem.getCount();
            }
        }

    }
}
