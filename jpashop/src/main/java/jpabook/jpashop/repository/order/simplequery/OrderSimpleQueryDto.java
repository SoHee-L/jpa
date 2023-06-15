package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class OrderSimpleQueryDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
// Order 가 직접 엔티티 로 받을 수 없음 o 를 jpa 는 식별자로 구분해서 매개변수에 직접 넣어줘야됨.
//        public OrderSimpleQueryDto(Order order) {
//            orderId = order.getId();
//            name = order.getMember().getName(); //LAZY 초기화
//            orderDate = order.getOrderDate();
//            orderStatus =order.getStatus();
//            address = order.getDelivery().getAddress(); //LAZY 초기화
//        }

        public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
            this.orderId = orderId;
            this.name = name;
            this.orderDate = orderDate;
            this.orderStatus =orderStatus;
            this.address = address;
        }
}
