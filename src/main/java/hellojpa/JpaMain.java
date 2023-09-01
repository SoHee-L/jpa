package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args){
        //EntityManagerFactory 는 Entity 로딩시점에 하나만 만들어야됨.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //실제 내가 DB 에 저장하거나 하는 transaction 단위는 고객이 상품장바구니를 담는다 던가하는 일괄적인 단위를 할때마다
        //EntityManager 를 꼭 만들어줘야 된다.
        EntityManager em = emf.createEntityManager();
        // 실제 동작하는 코드 작성
        EntityTransaction tx = em.getTransaction();
        tx.begin();



        try {
//            //회원 등록
//            Member member = new Member();
//            //테이블에 생성될 값
//            member.setId(1L);
//            member.setName("HelloA");
//            //.persist = member 에 값을 저장함.
//            em.persist(member);

            //회원 수정
            //.find 회원 수정 매서드.
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");


            //자바객체에서 값만 바꿧는데 되는 이유 : JPA를 통해서 엔티티를 가져오면 관리를하고
            // 변경이 됐는지 안됐는지 트렌젝션을 커밋하는 시점에서 다체크를하고 트렌젝션 하기 직전에 업데이트쿼리를 날리고 트렌젝션이 커밋이됨.

            tx.commit();

//            //회원 삭제
//            Member findMember = em.find(Member.class, 1L);
//            em.remove(findMember);

            //try catch 사용해서 정상적일땐 commit
            tx.commit();
        } catch (Exception e) {
            //문제가 생기면 rollback
            tx.rollback();
        } finally {
            //작업이끝나면 entityManager가 작업을 닫아주는데 이게 중요.
            em.close();
            //entityManager가 내부적으로 데이터베이스커넥션을 물고 작업하기 때문에 꼭 닫아줘야한다.
        }

        //회원 삭제

        emf.close();
    }
}
//정석적인 코딩이지만 실제로는 Spring이 다해줘서 em.persist(member);정도만 호출하면 끝남.