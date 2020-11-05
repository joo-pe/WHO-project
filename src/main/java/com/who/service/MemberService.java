package com.who.service;

import com.who.domain.MemberDetail;
import com.who.domain.entity.CertifiedEntity;
import com.who.domain.entity.MemberEntity;
import com.who.domain.repository.CertifiedRepository;
import com.who.domain.repository.MemberRepository;
import com.who.dto.MemberDto;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
	
    @Autowired
	MemberRepository memberRepository;
    CertifiedRepository certifiedRepository;

    @Transactional
    public Long joinUser(MemberDto memberDto) {
        //비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        
        return memberRepository.save(memberDto.toEntity()).getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email);
        if(memberEntity == null) {
            throw  new UsernameNotFoundException("Could not find user with that email");
        }
        
        return new MemberDetail(memberEntity);

//        Optional<MemberEntity> userEntityWrapper = memberRepository.findByEmail(email);
//        MemberEntity userEntity = userEntityWrapper.get();
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        if(("admin").equals(email)) {
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
//        } else {
//            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
//        }
//
//        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }


    @Transactional
    public Long savePost(MemberDto memberDto) {
        return memberRepository.save(memberDto.toEntity()).getId();
    }


    @Transactional
    public List<MemberDto> getMemberlist() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<MemberDto> memberDtoList = new ArrayList<>();

        for (MemberEntity memberEntity : memberEntities) {
            MemberDto memberDto = MemberDto.builder()
                    .id(memberEntity.getId())
                    .email(memberEntity.getEmail())
                    .name(memberEntity.getName())
                    .phone(memberEntity.getPhone())
                    .birthday(memberEntity.getBirthday())
                    .createdDate(memberEntity.getCreatedDate())
                    .enabled(memberEntity.isEnabled())
//                    .roles(memberEntity.getRoles())
                    .build();

            memberDtoList.add(memberDto);
        
        }
        return memberDtoList;
    }
    
    @Transactional
    public MemberDto getMember(Long id) {
        Optional<MemberEntity> memberEntityWraper = memberRepository.findById(id);
        MemberEntity memberEntity = memberEntityWraper.get();

        MemberDto memberDto = MemberDto.builder()
        		.id(memberEntity.getId())
                .email(memberEntity.getEmail())
                .name(memberEntity.getName())
                .phone(memberEntity.getPhone())
                .birthday(memberEntity.getBirthday())
                .createdDate(memberEntity.getCreatedDate())
                .enabled(memberEntity.isEnabled())
//                .roles(memberEntity.getRoles())
                .build();

        return memberDto;
    }
    
    //입력한 email과 name이 일치하는 값을 찾는 요청
	public boolean userEmailCheck(String email, String name) {

        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email);
        if(memberEntity!=null && memberEntity.getName().equals(name)) {
            return true;
        }
        else {
            return false;
        }
    }

    //해당 유저의 패스워드 변경
    public void updatePassword(String newpw, String email){
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	String password = passwordEncoder.encode(newpw);
        Long id = memberRepository.findMemberEntityByEmail(email).getId();
        memberRepository.update(id, password);
    }
    
    //회원가입 email 중복확인
    public String idCheck(String email) {
        System.out.println(memberRepository.findMemberEntityByEmail(email));

        if (memberRepository.findMemberEntityByEmail(email) == null) {
            return "YES";
        } else {
            return "NO";
        }
    }
    
    //email로 발송 된 인증번호와 입력한 인증번호가 일치하는지 확인
    public String CertifiedCheck(String number) {
    	CertifiedEntity certifiedEntity = certifiedRepository.findCertifiedEntityByNumber(number);
	        if(certifiedEntity!=null && certifiedEntity.getNumber().equals(number)) {
	            return "YES";
	        }
	        else {
	            return "NO";
	        }
    }
 
}
