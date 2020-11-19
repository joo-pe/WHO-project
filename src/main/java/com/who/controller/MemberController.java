package com.who.controller;

import com.who.domain.MemberDetail;
import com.who.domain.entity.MemberEntity;
import com.who.domain.repository.MemberRepository;
import com.who.dto.CertifiednumberDto;
import com.who.dto.FaqDto;
import com.who.dto.MailDto;
import com.who.dto.MemberDto;
import com.who.dto.NoticeDto;
import com.who.dto.SessionUser;
import com.who.dto.SportsDto;
import com.who.helpers.ZXingHelper;
import com.who.service.FaqService;
import com.who.service.FileService;
import com.who.service.MemberService;
import com.who.service.NoticeService;
import com.who.service.SendEmailService;
import com.who.service.SportsService;

import lombok.AllArgsConstructor;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private SendEmailService sendEmailService;
    private SportsService sportsService;
    private FaqService faqService;
    private FileService fileService;
    private NoticeService noticeService;
    private final HttpSession httpSession;

    //회원가입 동의페이지
    @GetMapping("/signup")
    public String dispSignup() {
        return "login/signup";
    }
    
    //회원가입 페이지
    @GetMapping("/signup2")
    public String dispSignup2() {
        return "login/signup2";
    }

    //회원가입 처리
    @PostMapping("/signup2")
    public String execSignup(MemberDto memberDto) {
        memberService.joinUser(memberDto);

        return "redirect:/login";
    }

    // 로그인 페이지
    @GetMapping("/login") 
    public String dispLogin(Model model) {

    	//CustomOAuth2UserService에서 로그인 성공시 세션에 SessionUser를 저장
    	//즉, 로그인 성공 시 httpSession.getAttribute("user")에서 값을 가져올 수 있다
    	SessionUser user = (SessionUser) httpSession.getAttribute("user");

    	//세션에 값이 있을때만 model에 userName 등록
    	if(user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "login/login";
    }

    // 로그인 결과 페이지
    @GetMapping("/login/result")
    public String dispLoginResult(Model model) {
    	 List<FaqDto> faqList = faqService.getFaqlist();
         model.addAttribute("faqList", faqList);
         List<NoticeDto> noticeList = noticeService.getNoticelist();
         model.addAttribute("noticeList", noticeList);
        return "login/loginSuccess";
    }
    
    // 비밀번호 찾기 페이지
    @GetMapping("/searchpass") 
    public String searchPass() {
        return "login/searchPass";
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
        SportsDto sportsDto = sportsService.getSports((long) 1);

        model.addAttribute("sportsList", sportsDto);
        model.addAttribute("currentUser", memberEntity);
        return "myticket/myinfo";
    }
    
    //현재 사용자 정보변경 페이지
    @GetMapping("/resignup/{no}")
    public String resignup(@AuthenticationPrincipal MemberDetail memberDetail, Model model) {
        String email = memberDetail.getUsername();
        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email);

        model.addAttribute("currentUser", memberEntity);
        return "myticket/resignup";
    }
    
    // 현재 사용자 정보와 스포츠 정보 qr로 가져오기
    @GetMapping("/qr")
    public String dispSportInfo(@AuthenticationPrincipal MemberDetail memberDetail, Model model) {
        String email = memberDetail.getUsername();
        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email);
        SportsDto sportsDto = sportsService.getSports((long) 1);

        model.addAttribute("sportsList", sportsDto);
        model.addAttribute("currentUser", memberEntity);
        return "myticket/Qr";
    }

    //QR코드
    @GetMapping("/qrcode/{id}")
    public void qrcode(@PathVariable("id") long id, @AuthenticationPrincipal MemberDetail memberDetail, HttpServletResponse response) throws Exception{
        //"기생충이진호19860909"
        // TITLE NAME BIRTHDAY =ID
        //ID = TITLE NAME BIRTHDAY
        //id = title + "\n" + name +"\n" +birthday;
        //id = 1101
        String email = memberDetail.getUsername();
        SportsDto sportInfo= sportsService.getSports(id);
        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email);
        String QRContents ="공연제목:"+sportInfo.getTitle() + "\n" + "고객이름:" + memberEntity.getName() + "\n" + "고객생년월일:" + memberEntity.getBirthday();
        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        String encoded_QRContents = new String(QRContents.getBytes("UTF-8"), "ISO-8859-1");
        outputStream.write(ZXingHelper.getQRCodeImage(encoded_QRContents, 200, 200));
        outputStream.flush();
        outputStream.close();
    }
    
    //현재 사용자 정보변경 처리
    @GetMapping("/resignup/update/{no}")
    public String update(MemberDto memberDto) {
        memberService.savePost(memberDto);

        return "redirect:/myinfo";
    }
    
    //현재 사용자 비밀번호변경 페이지
    @GetMapping("/repass/{no}")
    public String repass(@AuthenticationPrincipal MemberDetail memberDetail, Model model) {
        String email = memberDetail.getUsername();
        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email);

        model.addAttribute("currentUser", memberEntity);
        return "myticket/repass";
    }
    
    //입력한 password와 DB상 password 일치여부를 check하는 컨트롤러
    @PostMapping("/check/Pw")
    public @ResponseBody void userPasswordCheck(String currentpw, String email) {	

        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("DB 회원의 비밀번호 : " + memberEntity.getPassword());
        System.out.println("입력한 비밀번호 : " + currentpw);	
        
        if(encoder.matches(currentpw, memberEntity.getPassword())) { 
    		System.out.println("비밀번호 일치");
    	} else {
    		System.out.println("비밀번호 불일치");
    	}
    }
    
    //새로 입력한 password로 사용자의 password를 변경하는 컨트롤러
    @PostMapping("/check/Pw/changePw")
    public @ResponseBody void changePw(String newpw, String email){
      memberService.updatePassword(newpw, email);
       
    }
        
    //Email과 name의 일치여부를 check하는 컨트롤러
    @GetMapping("/check/findPw")
       public @ResponseBody Map<String, Boolean> pw_find(String email, String name){
           Map<String,Boolean> json = new HashMap<>();
           boolean pwFindCheck = memberService.userEmailCheck(email, name);
           System.out.println(pwFindCheck);
           json.put("check", pwFindCheck);
           return json;
       }

    //등록된 이메일로 임시비밀번호를 발송하고 발송된 임시비밀번호로 사용자의 password를 변경하는 컨트롤러
    @PostMapping("/check/findPw/sendEmail")
    public @ResponseBody void sendEmail(String email, String name){
       MailDto dto = sendEmailService.createMailAndChangePassword(email, name);
       sendEmailService.mailSend(dto);
    }

    //회원가입 시 사용가능한 email인지 체크
    @GetMapping("/idCheck")
    @ResponseBody
    public String email_check(String email) {
        System.out.println(email);
        String str = memberService.idCheck(email);
        return str;
    }

    //이메일로 보낸 인증번호 입력 시 일치하는지 여부확인
    @PostMapping("/CertifiedCheck")
    @ResponseBody
    public String certified_Check(String number) {
        System.out.println(number);
        return memberService.CertifiedCheck(number);

    }

    //등록된 이메일로 인증번호를 발송하고 인증번호를 DB에 저장
    @PostMapping("/idCheck/sendEmail")
    public @ResponseBody void sendEmail(CertifiednumberDto certifiednumberDto, String email){
    	// 인증번호 생성
    	String number =sendEmailService.getTempNumber();
    	certifiednumberDto.setNumber(number);
    	sendEmailService.joinCertified(certifiednumberDto);
        MailDto dto = sendEmailService.createMailAndCheck(email, number);
        sendEmailService.mailSend(dto);

    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "admin/admin";
    }
    
    // 어드민 회원명단 페이지
    @GetMapping("/admin/member")
    public String list(Model model) {
        List<MemberDto> memberList = memberService.getMemberlist();

        model.addAttribute("memberList", memberList);
        return "admin/Member";
    }

}
