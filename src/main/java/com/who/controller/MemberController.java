package com.who.controller;

import com.who.domain.MemberDetail;
import com.who.domain.entity.MemberEntity;
import com.who.domain.repository.MemberRepository;
import com.who.dto.FaqDto;
import com.who.dto.MemberDto;
import com.who.service.MemberService;
import lombok.AllArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class MemberController {
    private MemberService memberService;
    private MemberRepository memberRepository;

    //회원가입 페이지
    @GetMapping("/signup")
    public String dispSignup() {
        return "login/signup";
    }

    //회원가입 처리
    @PostMapping("/signup")
    public String execSignup(MemberDto memberDto) {
        memberService.joinUser(memberDto);

        return "redirect:/login";
    }

    // 로그인 페이지
    @GetMapping("/login") 
    public String dispLogin() {
        return "login/login";
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

    // 현재 사용자 정보 가져오기
    @GetMapping("/myinfo")
    public String dispCuurentUserInfo(@AuthenticationPrincipal MemberDetail memberDetail, Model model) {
        String email = memberDetail.getUsername();
        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email);

        model.addAttribute("currentUser", memberEntity);
        return "myticket/myinfo";
    }
    
    @GetMapping("/resignup/{no}")
    public String resignup(@PathVariable("no") Long no, Model model) {
        MemberDto memberDto = memberService.getMember(no);

        model.addAttribute("memberDto", memberDto);
        return "myticket/resignup";
    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "admin/admin";
    }
    
    @GetMapping("/admin/member")
    public String list(Model model) {
        List<MemberDto> memberList = memberService.getMemberlist();

        model.addAttribute("memberList", memberList);
        return "admin/Member";
    }

}
