package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {
    @Id  @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;

    @ManyToMany //다대다 관계도 연관관계 맵핑 필요 / 다대다는 조인테이블이 필요함.
    //중간테이블로 맵핑해줘야되는 이유는 객체에는 컬렉션, 컬렉션이 있어서
    // 다대다 연관관계가 가능한데 관계형디비는 컬렉션 관게를 가질 수 있는게 아니기
    // 때문에 일대다 다대일로 풀어내는 중간 테이블이 있어야됨.
    //다대다관계를 실전에서 쓰지말라고 하는 이유는 더 필드를 추가하게되면 불가능하기때문에 실무에서 거의 사용 못함.
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>(); //자식은 여러개 가질 수 있으니 List<Category>

    //==연관관계 메서드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
