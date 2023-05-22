package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//데이터 변경하는 것은 @Transactional 이 꼭 있어야 함.
//@Transactional 이 두개 있는데 springframework @Transactional 로 쓰기
//읽기가 많으면 여기에 @Transactional(readOnly = true)를 넣고 쓰기엔 가만히 놔두면 됨.
@Transactional(readOnly = true)
@RequiredArgsConstructor //final 이 있는 필드만 가지고 생성자를 만들어줌. / 이 스타일을 선호.
public class MemberService {
    //변경할 일 없기 때문에 final 로 권장함.
    //컴파일 시점을 체크할 수 있기 때문에 final 어노테이션 넣는 것을 추천함.
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    //읽기가 아닌 쓰기에는 readOnly = true 를 넣으면 안됨
    @Transactional //따로 설정한 것은 이게 우선권을 가짐.
    public Long join(Member member){
        //중복회원 검증 로직.
        validateDuplicateMember(member);
        //문제가 없으면 회원을 바로 정상적으로 save
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        //EXCEPTION (중복회원이 있으면 얘를 터트려 버림.)
        List<Member> findMembers =memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    //읽기에는 가급적이면 readOnly = true 를 넣기
    //@Transactional(readOnly = true)
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //@Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
