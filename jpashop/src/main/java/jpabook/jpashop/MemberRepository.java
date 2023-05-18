package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository //Springboot가 제공하는 기본타입 /기본 컴포넌트 스캔의 대상이 되는 어노테이션
public class MemberRepository {

    //스프링 부트를 쓰기 때문에 스프링 컨테이너 위에서 다 동작함.
    @PersistenceContext //<- 이 어노테이션이 있으면 EntityManager를 다 주입해줌.
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
        //member를 반환하지 않고 id만 반환하는 이유는 커맨드와 쿼리를 분리해라로 저장을 하고나면 가급적이면
        //리턴값은 거의 만들지 않음. 대신 id 정보가 있으면 다음에 조회 가능.
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }
}
