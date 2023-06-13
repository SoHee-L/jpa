package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//과거에는 @Controller 를 적었지만  @Controller + @RequestBody = @RestController 가 생김
//@RequestBody = 데이터 자체를 json xml 로 보내자 라고 할때 쓰임.
@RestController
@RequiredArgsConstructor
//v1 의 회원 등록 API
public class MemberApiController {
    private final MemberService memberService;

    //조회 기능 api
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        //이렇게 Entity 를 직접 호출하게 되면 Entity 의 내부의 정보들이 다 노출됨.
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();

        //forEach 문을 돌려서 넣어도 되지만 자바 8 이라 람다 사용
        List<MemberDto> collect = findMembers.stream()
                //member Entity 에서 이름을 꺼내와서 dto 에 넣음.
                .map(m->new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    //이렇게 한번 감싸줘야됨. list 를 바로 collection 으로 바로되면
    //json 배열 타입으로 바로 나가서 유연성이 확 떨어짐.
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

    //@RequestBody 를 사용하면 json 으로 온 body 를 Member 에 그대로 맵핑에서 넣어줌.
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    } //=> 실무에서는 이렇게 Member 와 같이 Entity 를 직접 받아서 사용하면 안됨. Member 에 있는 name 이 userName 으로 변경된다면
    //jpa spec 이 바뀌기 때문에 큰 장애로 이어지기 때문.
    // 그렇기 때문에 api spec 을 위한 별도의 data trans object 인 dto 를 만들어야 됨.

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    //등록이랑 수정은 api 가 다 다름.
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        //수정할때 가급적이면 변경감지 사용.
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }

    //Entity 에는 lombok 어노테이션을 쓰는 걸 최대한 자제 @Getter 정도만 사용
    //Dto 는 로직이 있는 것도 아니고 데이터가 왔다갔다 하는 것이기 때문에 막씀.
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
