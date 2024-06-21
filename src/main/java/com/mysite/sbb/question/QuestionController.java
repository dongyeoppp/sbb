package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
//    @ResponseBody
    // RequestParam을 통해 HTTP 요청의 쿼리 파라미터 'page'를 'page' 매개변수로 바인딩, 세팅되지 않으면 기본값 0으로 설정  // http://localhost:8080/question/list?page=0 방식으로 url 호출
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page) {       // 매개변수로 model을 지정하면 객체가 자동으로 생성됨
        Page<Question> paging = this.questionService.getList(page);        // model 객체에 'paging'라는 이름을 저장
        model.addAttribute("paging", paging);
        return "question_list";
    }

//    @GetMapping("/list")
//    public ResponseEntity<Page<Question>> list(@RequestParam(value = "page", defaultValue = "0") int page) {
//        Page<Question> paging = this.questionService.getList(page);
//        return ResponseEntity.ok(paging);
//    }

    @GetMapping(value = "/detail/{id}")        // 변하는 id 값을 얻을 때 @pathVariable 애너테이션을 사용
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }
//    @GetMapping("/detail/{id}")
//    public ResponseEntity<Question> detail(@PathVariable("id") Integer id) {
//        Question question = this.questionService.getQuestion(id);
//        return ResponseEntity.ok(question);
//    }

//    @PreAuthorize("isAuthenticated()")      // 해당 에너테이션이 붙은 메서드는 로그인을 한 경우에만 실행된다.
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {       // questionForm 객체는 폼 데이터를 담기 위한 객체
        return "question_form";
    }
//
//    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    // 메서드 오버로딩 : 한 클래스에서 동일한 메서드명을 사용할 수 있다. (매개변수의 형태가 다른 경우에 가능)
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {      // valid 에너테이션을 사용하여 questionForm객체의 유효성을 검사
        if (bindingResult.hasErrors()) {        // bindingResult 객체는 유효성 검사의결과를 담음
            System.out.println("error");
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());      // principal 객체를 통해 사용자명을 구한 후
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);     // 게시글 저장 할 때 함께 사용자명 저장
        return "redirect:/question/list";
    }
//    @PreAuthorize("isAuthenticated()")
//    @PreAuthorize("permitAll()")
//    @PostMapping("/create")
//    public ResponseEntity<?> questionCreate(@RequestBody QuestionForm questionForm, Principal principal) {
////        if (bindingResult.hasErrors()) {
////            return ResponseEntity.badRequest().body("Invalid form data");
////        }
////        SiteUser siteUser = this.userService.getUser(principal.getName());
//        SiteUser siteUser = null;
//        if (principal != null) {
//            siteUser = this.userService.getUser(principal.getName());
//        }
//        this.questionService.create(questionForm.getSubject(), questionForm.getContent(),siteUser);
//        return ResponseEntity.ok("Question created successfully");
//    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal,@PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
}