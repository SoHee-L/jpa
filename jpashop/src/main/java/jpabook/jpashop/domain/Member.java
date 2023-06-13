package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty //이름은 무조건 필수 값이니 @NotEmpty 넣어줌.
    private String name;
    //@Embedded / @Embeddable 둘중에 하나만 사용해도 내장타입을 알 수 있음.
    @Embedded //내장타입에 포함에 따른 어노테이션.
    private Address address;

    //order 와 일대다 관계 / 양방향관계라 연관관계 주인을 정해줘야됨.
    //연관관계 주인이 아니기 때문에 mappedBy를 넣어줌.
    @JsonIgnore //@JsonIgnore 은 주문정보가 빠짐 회원 데이터만 넣고 싶을 때 사용
    @OneToMany (mappedBy = "member") //order 테이블에 있는 member field 에 의해 맵핑된거야 라는 뜻.
    private List<Order> orders = new ArrayList<>();

}
