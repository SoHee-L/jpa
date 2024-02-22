package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
//테이블에 ORDER가 아닌 ORDERS라고 쓴 이유는 DB에 ORDER가 예약어로 걸려있어서
//안되는 DB도 있기 때문에 보통 ORDERS라고 사용.
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

//    @Column(name = "MEMBER_ID")
//    private Long memberId;

    //order입장에서는 나를 주문한 member가 필요하기 때문에 memberId가 아닌 member로 바꿔줘야 됨.
    //만약에 member에서 양방향 맵핑을 하고 싶다면 member에서 컬렉션을 하면 됨.
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    //private Member member로 불러와야됨. private 위와같이 Long memberId 같은 경우를
    //객체보다는 관계형 DB를 객체에 맞춰서 설계함. -> 참조가 다 끊김.
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //양방향 연관관계기 때문에 연관관계 편의 메서드로 만듬.
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
