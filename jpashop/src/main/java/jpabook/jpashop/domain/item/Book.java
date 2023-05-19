package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("B") //싱글테이블이니 뭔가 저장할때 DB에 구분이 되어야됨. 그때 기본으로 넣는값.
@Getter
@Setter
public class Book extends Item {
    private String author;
    private String isbn;


}
