package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    //Spring ui에 Model 란 얘가 model에 어떤 data를 심어서 view에 넘길 수 있음.
    public String hello(Model model){
        model.addAttribute("data", "hello!!!");
        return "hello"; //이게 resources/templates/hello.html로 전달됨.
    }
}
