package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //jpa의 내장타입
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //jpa 스펙에서는 생성자를 protected 까지 허용해줌 그래서 함부로 new 로 생성하면 안되겠네가 판단가능함.
    protected Address(){
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
