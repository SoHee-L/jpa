package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)//junit 실행할 때 스프링이랑 같이 엮어서 실행할래라는 어노테이션.
@SpringBootTest //이 두가지가 있어야 SpringBoot 와 실제 돌려서 테스트할 수 있다.
@Transactional // 데이터를 변경해야 되기 때문에 / 기본적으로  DB를 롤백해버림.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    @Test
    @Rollback(false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        //멤버 리파지토리에서 새로 멤버를 찾음.
        // savedId 에 저장한 member 와 찾은 멤버와 똑같은지 보는 메서드 = assertEquals
        //jpa 에서 같은 Transactional 안에서 id 값 = pk 값이 똑같으면 얘는 같은 영속성 컨텍스트에 똑같은 애가 걸림
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);

//(expected = IllegalStateException.class)을 넣으면 제외해도 됨.
//        try {
//            memberService.join(member2); //예외가 발생해야 한다.!!
//        }catch (IllegalStateException e) {
//            return;
//        }
        //then
        fail("예외가 발생해야 한다.");
    }

}