package com.mysite.sbb.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")      // 회원가입을 위한 템플릿 렌더링
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "signup_form";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){       // password1과 password2가 동일한지 검증
            bindingResult.rejectValue("password2","passwordInCorrect","2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        try{
            userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1());     // 전달 받은 데이터 저장
        } catch (DataIntegrityViolationException e){        // id나 이메일이 중복되는 경우
            e.printStackTrace();        // 예외 처리 과정에서 예외의 스택 추적 정보를 출력할 뿐 코드 동작에는 영향을 주지 않는다.
            bindingResult.reject("signupFaild","이미 등록된 사용자입니다.");
            return "signup_form";
        }catch (Exception e){
            e.printStackTrace();
            bindingResult.reject("signupFailed",e.getMessage());
            return "singup_form";
        }

        return "redirect:/";
    }
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }


}
