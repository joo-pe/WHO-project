package com.who.controller;

import com.who.dto.MemberDto;
import com.who.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class MemberController {
    private MemberService memberService;

    //회원가입 페이지
    @GetMapping("/signup")
    public String dispSignup() {
        return "login/signup";
    }

    //회원가입 처리
    @PostMapping("/signup")
    public String execSignup(MemberDto memberDto) {
        memberService.joinUser(memberDto);

<<<<<<< HEAD
        return "redirect:login/login";
=======
        return "redirect:/login";
>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String dispLogin() {
<<<<<<< HEAD
        return "/login";
=======
        return "login/login";
>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec
    }

    // 로그인 결과 페이지
    @GetMapping("/login/result")
    public String dispLoginResult() {
        return "login/loginSuccess";
    }

    // 로그아웃 결과 페이지
    @GetMapping("/logout/result")
    public String dispLogout() {
        return "login/logout";
    }

    // 접근 거부 페이지
    @GetMapping("/denied")
    public String dispDenied() {
        return "login/denied";
    }

    // 내 정보 페이지
    @GetMapping("/info")
    public String dispMyInfo() {
        return "login/myinfo";
    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "login/admin";
    }


}
