package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;
    @Embedded
    private Address address;

    //Enum 타입은 조심해야될게 @Enumerated을 반드시 넣어야됨.
    //EnumType.ORDINAL(default) /EnumType.String을 넣을 수 있는데
    //ORDINAL 은 1,2,3,4 숫자로 들어감. READY, COMP를 넣으면 1,2가 들어가는데 문제는 다른 변수가 들어가면 망함.
    //그래서 EnumType.ORDINAL 절대로 쓰면 안되고 무조건 EnumType.String으로 들어가야됨. 그래야 중간에 들어가도
    //순서가 밀리는 경우가 없다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, COMP
}
