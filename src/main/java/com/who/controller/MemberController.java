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

	    // 메인 페이지
	    @GetMapping("/")
	    public String index() {
	        return "/index.html";
	    }

	    // 회원가입 페이지
	    @GetMapping("/user/signup")
	    public String dispSignup() {
	        return "/signup";
	    }

	    // 회원가입 처리
	    @PostMapping("/user/signup")
	    public String execSignup(MemberDto memberDto) {
	        memberService.joinUser(memberDto);

	        return "redirect:/login";
	    }

	    // 로그인 페이지
	    @GetMapping("/login")
	    public String dispLogin() {
	        return "login.html";
	    }

	    // 로그인 결과 페이지
	    @GetMapping("/login/result")
	    public String dispLoginResult() {
	        return "/loginMain";
	    }

	    // 로그아웃 결과 페이지
	    @GetMapping("/logout/result")
	    public String dispLogout() {
	        return "/logout";
	    }

	    // 접근 거부 페이지
	    @GetMapping("/user/denied")
	    public String dispDenied() {
	        return "/denied";
	    }

	    // 내 정보 페이지
	    @GetMapping("/user/info")
	    public String dispMyInfo() {
	        return "/myinfo";
	    }

	    // 어드민 페이지
	    @GetMapping("/admin")
	    public String dispAdmin() {
	        return "/admin";
	    }
	}
