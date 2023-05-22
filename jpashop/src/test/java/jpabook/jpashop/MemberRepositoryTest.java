//package jpabook.jpashop;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
////스프링부트로 테스트를 돌려야되기 때문에 junit한테 알려줘야됨
//@RunWith(SpringRunner.class)//스프링과 관련된걸 테스트할거야라고.
//@SpringBootTest
//public class MemberRepositoryTest {
//    @Autowired MemberRepository memberRepository;
//    @Test
//    //@Transactional 이 두개있는데 springframework 걸 쓰는걸 권장 쓸 수 있는 옵션이 많기 때문.
//    //@Transactional 은 테스트가 끝나면 바로 롤백해버림.
//    @Transactional
//    @Rollback(false)// 롤백 안하고 바로 커밋해버림.
//    public void testMember() throws Exception{
//        //given
//        Member member = new Member();
//        member.setUsername("memberA");
//
//        //when
//        Long saveId = memberRepository.save(member);
//        Member findMember = memberRepository.find(saveId);
//
//        //then
//        assertThat(findMember.getId()).isEqualTo(member.getId());
//        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//
//        //true로 나오는 이유는 같은 트렌젝션안에서 저장하고 조회하면
//        //영속성컨텍스트가 똑같은데 같은 영속성 컨텍스트 안에서는 id값이 같으면 같은 엔티티로 식별되서.
//        assertThat(findMember).isEqualTo(member);//JPA 엔티티 동일성 보장
//        System.out.println("findMember == member: "+ (findMember == member));
//
//
//    }
//
//}