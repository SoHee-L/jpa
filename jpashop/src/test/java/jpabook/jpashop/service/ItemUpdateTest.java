package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;
    @Test
    public void updateTest () throws Exception{
        Book book = em.find(Book.class, 1L);

        //TX
        //트랜젝션에선 이렇게 이름을 바꿔친 다음에
        book.setName("asdfert");
        //TX commit
        //트랜젝션을 커밋해버리면 jpa 가 위와 같은 변경문에 대해서 jpa 가 자동으로 찾아서
        //update 를 자동으로 생성해서 데이터에 반영함.(더티체킹 == 변경감지라고 함)
    }
}
