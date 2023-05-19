package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//상속관계 전략을 지정해야되는데 전략을 부모클래스에 잡아줘야됨 (Item은 Album, Book, Movie의 부모 클래스)

//InheritanceType 중 JOINED는 가장 정교화된 스타일
//SINGLE_TABLE는 한테이블에 다 때려 넣음.
//TABLE_PER_CLASS는 예시로 Album, Book, Movie 세개의 테이블로 나온 전략
//여기선 싱글테이블 전략이니 InheritanceType.SINGLE_TABLE 사용.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@DiscriminatorColumn(name = "dtype") //Book 이면 어떻게 할까요? 라는 의미.
@Getter @Setter
public abstract class Item {//구현체를 가지고 할것이기 때문에 abstract 사용
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    //상속관계에 대한 맵핑
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
