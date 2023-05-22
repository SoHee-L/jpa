package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //멤버를 FetchType.EAGER이거로 해놓으면 order를 조회할때 조인해서 같이가져옴. 쿼리한번에
    //근데 이건 em.find 해서 한건 조회할 때 가져옴. 근데 문제는
    //JPQL select o From order o; -> SQL select * from order; 이런식으로 번역이 되버림. 그렇기 때문에 n+1 문제가 되버림.
    //@ManyToOne은 기본패치 전략이 (fetch = FetchType.EAGER)이건데
    //@OneToMany은 기본패치 전략이 (fetch = FetchType.LAZY)이거임.
    //그렇기때문에 xxxToMany인 것들은 그냥 두면되고 @ManyToOne은 전부 코드 다 찾아서 (fetch = FetchType.LAZY)로 다바꿔야됨.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="member_id") //조인 컬럼에 맵핑을 뭘로 할건지 정해줌.
    private Member member;

    //cascade = CascadeType.ALL 란 orderItems에다가 데이터를 넣어두고 Order를 저장하면
    //orderItems에도 같이 저장이 됨.
    //persist(orderItemA)
    //persist(orderItemB)
    //persist(orderItemC)
    //persist(order) 원래 이렇게 각각 저장해야되는데 / cascade는 persist(order)이거 하나만 하면 됨.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //jpa에서는 fk를 일대일 관계기 때문에 order에 둬도되고 delivery에 둬도 됨.
    //어디에 두든 장단점이 있지만 김영한은 access를 많이 하는 곳에 둠.
    //ex) 시스템상 delivery를 조회하는 일보단 order에 조회하는 일이 더 많음.
    //그렇기 때문에 order에 fk를 둠.
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //==연관관계 메서드==//
    //양방향 연관관계기 때문에 이 두개를 묶는 메서드를 만들어줌.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
