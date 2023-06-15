package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    //entity 로 반환하는 버전
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            //member 에 쿼리를 날려서 jpa 가 끌고 옴.
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            //=>이렇게 하면 member 랑 delivery 랑 출력이 됨.
        }
        return all;
    }

    //dto 로 반환하는 버전
    @GetMapping("/api/v2/simple-orders")
    //원래는 List 로 반환하는게 아닌 result 로 감싸야 됨.
    public List<SimpleOrderDto> ordersV2() {
        //ORDER 2개
        //N + 1 문제 발생 = 첫번째 쿼리가 나가면 orders 를 가지고오는데
        //1 + N(2) 첫 번째 쿼리 결과로 N 번만큼 쿼리가 추가 실행되는 문제가 발생. N(2) 는 회원 N + 배송 N
        //즉, 1 + 회원 N + 배송 N
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        //List<SimpleOrderDto> 이런식으로 v1 과달리 dto 로 다 바꿔서 넣어야됨
        List<SimpleOrderDto> result=  orders.stream()
                //.map(o->new SimpleOrderDto(o)) 이걸 밑에와 같이 줄일 수 있음.
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
        return result;
    }

    //V2 처럼 쿼리가 다중조회시 성능문제로인한 개선버전.
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
         List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    //V3 는 entity 를 dto 로 변환해서 조회했지만 V4 는 jpa 에서 바로 dto 로 끄집어내서 성능최적화를 할 수 있음.
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus =order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }

}
