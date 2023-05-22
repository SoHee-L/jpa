package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository//스프링에서 제공하는 Repository 어노테이션. 얘가 컴포넌트 스캔해서 자동으로 관리가 됨.
@RequiredArgsConstructor
public class MemberRepository {

    //스프링 부트 라이브러리를 쓰면 @PersistenceContext => @Autowired 로 사용가능. 그렇게 되면 생성자 주입 가능.
    @Autowired
    private EntityManager em; //이렇게 해주면 스프링이 EntityManager를 만들어서 얘를 주입(injection)해줌

    public void save(Member member){
        //em.persist 해서 member를 집어넣으면 jpa가 회원을 저장하는 로직을 만들어줌.
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        //전체 조회시 jpql 을 작성해야됨.
        //createQuery 에서 첫번째는 jpql 쓰고 두번째는 반환타입을 넣으면 됨.
        //sql 과 jpql 의 문법은 약간씩 차이가 있는데 sql 은 테이블을 대상으로 쿼리를 하지만 jpql 은 엔티티 객체를 대상으로 쿼리함.
        return em.createQuery("SELECT  m from  Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("SELECT m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
