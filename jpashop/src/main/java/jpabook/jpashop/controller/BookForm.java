package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    //stockQuantity 까지 상품의 공통 속성
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    //책과 관련된 속성
    private String author;
    private String isbn;
}
