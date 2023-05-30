package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")//url주소가 같더라도 get 은 화면을 열어보고
    public String createFrom(Model model) {
        //Model 은 .addAttribute()에 값을 넣으면 컨트롤러에서 뷰로 넘어갈 때 실어서 보냄.
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")//post는 실제로 등록하는 것.
    public String create(@Valid MemberForm form, BindingResult result) {
        //@Valid MemberForm 를 사용하면 MemberForm 에 있는 변수들을 자동으로 validation 해줌.
        //원래는 @Valid MemberForm 에서 오류가 나면 그 오류가 튕겨버리는데
        // @Valid 한 다음에 BindingResult 가 있으면 오류가 BindingResult 에 담겨서 코드가 실행됨.

        if(result.hasErrors()){ //만약 result 에 에러가 있으면 리턴한다는 의미.
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        //이렇게 컨트롤러에서 위와 같이 정제를 한 후에 필요한 데이터만 넣는것을 추천함.
        memberService.join(member);
        return "redirect:/"; //이렇게 하면 첫번째 페이지로 넘어감.
    }

    @GetMapping("/members")
    public String list(Model model){
        //members 에서 모든 회원을 조회해서 model.addAttribute 에 담아서 화면에 넘김.
        //그럼 html에서 돌리면 됨.
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
