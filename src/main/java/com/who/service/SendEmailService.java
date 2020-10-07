package com.who.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.who.domain.repository.MemberRepository;
import com.who.dto.MailDto;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SendEmailService {

	@Autowired
    MemberRepository memberRepository;
	

    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "kwonga3@gmail.com";


    //DTO에 사용자가 원하는 내용과 제목을 저장
    public MailDto createMailAndChangePassword(String email, String name) {
        String str = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(email);
        dto.setTitle(name+"님의 WHOTicket 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. WHOTicket 임시비밀번호 안내 관련 이메일 입니다." + "[" + name + "]" +"님의 임시 비밀번호는 "
        + str + " 입니다.");
        updatePassword(str, email);
        return dto;
    }

    //이메일로 발송된 임시비밀번호로 해당 유저의 패스워드 변경
    public void updatePassword(String str, String email){
    	String password = EncryptionUtils.encryptMD5(str);
        Long id = memberRepository.findMemberEntityByEmail(email).getId();
        memberRepository.update(id, password);
    } 


    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
    
    public void mailSend(MailDto mailDto){
        System.out.println("이메일 전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress()); //받는 사람 주소
        message.setFrom(SendEmailService.FROM_ADDRESS); //보내는 사람 주소(설정하지 않으면 디폴트 값인  properties에 작성한 username) 
        message.setSubject(mailDto.getTitle()); // 메일 제목
        message.setText(mailDto.getMessage()); //메일 내용

        mailSender.send(message);
    }
}
