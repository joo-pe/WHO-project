package com.who.controller;

import com.who.dto.FaqDto;
import com.who.dto.MemberDto;
import com.who.service.MemberService;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

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

	//리스트를 통째로 가져오는 맵핑
	@GetMapping("/myticket")
	public String myinfo(Model model) {
	List<MemberDto> memberList = memberService.getMemberlist();
		
	model.addAttribute("memberList", memberList);
	return "myticket/myinfo";
	}
    
		  //회원정보 가져오기
		  @GetMapping("/member/{email}")
		  public String resignup(@PathVariable("email") String email, Model model) {
		  	MemberDto memberDto = memberService.getmember(email);
		
		      model.addAttribute("memberDto", memberDto);
		      return "myticket/resignup";
		  }
//		  //업데이트 할때 회원 정보 가져오기
//		  @GetMapping("/remember/up/{no}")
//		  public String up(@PathVariable("no") Long no, Model model) {
//		      MemberDto memberDto = memberService.upmember(no);
//		
//		      model.addAttribute(("memberDto"), memberDto);
//		      return "myticket/resignup";
//		  }
	//  //업데이트 맵핑
	//  @PutMapping("/post/up/{no}")
	//  public String update(MemberDto memberDto) {
	//  	memberService.savePost(memberDto);
	//
	//      return "redirect:/member";
	//  }
    
//	 // 회원정보수정 페이지
//	  @GetMapping("/resignup")
//	  public String dispresignup() {
//	  	return "myticket/resignup";
//	  }
	  
//	  //비밀번호 설정 변경 페이지
//	  @GetMapping("/repass")
//	  public String disprepass() {
//	  	return "myticket/repass";
//	  }
    

    // 어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "login/admin";
    }

}
