package com.spring.spring_0316.controller;

import com.spring.spring_0316.dto.MemberDto;
import com.spring.spring_0316.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Map;

@Controller
@AllArgsConstructor
public class MemberController {
    private MemberService memberService;

    @GetMapping("/")
    public String index() {
        return "/index";
    }

    /* 회원가입 페이지 */
    @GetMapping("/join")
    public String dispSignup(MemberDto memberDto) {
        System.out.println("start");
        return "/user/join";
    }

    /* 아이디 중복검사 */
    @PostMapping("/join/exists")
    @ResponseBody
    public boolean accountIdCheck(@RequestParam("accountId") String accountId) {
        System.out.println("userIdCheck : " + accountId);
        boolean exist = memberService.accountIdCheck(accountId);
        System.out.println("result:" + exist);

        return exist;
    }

    /* 회원가입 처리 */
    @PostMapping("/join")
    public String execSignup(@Valid MemberDto memberDto, Errors errors, Model model) {
        if(errors.hasErrors()) {
            System.out.println("error 구문 통과");
            // 회원가입 실패시, 입력 데이터 유지
            model.addAttribute("memberDto", memberDto);

            // 유효성 검사 실패한 필드 및 메시지 핸들링
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for(String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            /*String birth = memberDto.getBirth();
            if(birth!=null && !"".equals(birth)) {
                if(birthYear!=null && !"".equals(birthYear)) {
                    model.addAttribute("birthYear", birthYear);
                }
                if(birthMonth!=null && !"".equals(birthMonth))
                {
                    model.addAttribute("birthMonth", birthMonth);
                }
                if(birthDay!=null && !"".equals(birthDay))
                {
                    model.addAttribute("birthDay", birthDay);
                }
            }

            String mail = memberDto.getEmail();
            if(mail!="@")
            {
                if(mailId!=null && !"".equals(mailId))
                {
                    model.addAttribute("mailId", mailId);
                }
                if(mailDomain!=null && !"".equals(mailDomain))
                {
                    model.addAttribute("mailId", mailId);
                }
            }

            String phone = memberDto.getPhone();
            if(phone!=null && !"".equals(phone)) {
                if(phone!="") {
                    if(phone1!=null && !"".equals(phone1))
                    {
                        model.addAttribute("phone1", phone1);
                    }
                    if(phone2!=null && !"".equals(phone2))
                    {
                        model.addAttribute("phone2", phone2);
                    }
                    if(phone3!=null && !"".equals(phone3))
                    {
                        model.addAttribute("phone3", phone3);
                    }
                }
            }*/

            return "/user/join";
        }

        memberService.joinUser(memberDto);
        return "redirect:/login";
    }

    /* 로그인 페이지 */
    @GetMapping("/login")
    public String dispLogin() {
        return "/user/login";
    }

    /* 로그인 결과 페이지 */
    @GetMapping("/login/result")
    public String dispLoginResult() {
        return "/user/loginSuccess";
    }

    /* 로그아웃 결과 페이지 */
    @GetMapping("/logout/result")
    public String dispLogout() {
        return "/user/logout";
    }

    /* 접근 거부 페이지 */
    @GetMapping("/denied")
    public String dispDenied() {
        return "/denied";
    }

    /* 회원 정보 페이지 */
    @GetMapping("/mypage")
    public String dispMyPage() {
        return "/user/mypage";
    }

    /* 관리자 페이지 */
    @GetMapping("/admin")
    public String dispAdmin() {
        return "/admin";
    }
}
