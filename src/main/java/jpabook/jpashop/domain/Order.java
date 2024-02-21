package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
//테이블에 ORDER가 아닌 ORDERS라고 쓴 이유는 DB에 ORDER가 예약어로 걸려있어서
//안되는 DB도 있기 때문에 보통 ORDERS라고 사용.
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    //이렇게 식별자로 따로 있으면 객체 지향스럽지 않음.
    private Long memberId;

    //private Member member로 불러와야됨. private 위와같이 Long memberId 같은 경우를
    //객체보다는 관계형 DB를 객체에 맞춰서 설계함. -> 참조가 다 끊김.
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
