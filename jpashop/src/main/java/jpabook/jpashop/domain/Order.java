package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id") //조인 컬럼에 맵핑을 뭘로 할건지 정해줌.
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    //jpa에서는 fk를 일대일 관계기 때문에 order에 둬도되고 delivery에 둬도 됨.
    //어디에 두든 장단점이 있지만 김영한은 access를 많이 하는 곳에 둠.
    //ex) 시스템상 delivery를 조회하는 일보단 order에 조회하는 일이 더 많음.
    //그렇기 때문에 order에 fk를 둠.
    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]


}
