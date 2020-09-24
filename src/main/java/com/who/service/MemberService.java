package com.who.service;

import com.who.domain.Role;
import com.who.domain.entity.FaqEntity;
import com.who.domain.entity.MemberEntity;
import com.who.domain.repository.MemberRepository;
import com.who.dto.FaqDto;
import com.who.dto.MemberDto;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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

    @Transactional
    public Long joinUser(MemberDto memberDto) {
        //비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        return memberRepository.save(memberDto.toEntity()).getNo();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<MemberEntity> userEntityWrapper = memberRepository.findByEmail(email);
        MemberEntity userEntity = userEntityWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if(("admin").equals(email)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }
    
    @Transactional
    public List<MemberDto> getMemberlist() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<MemberDto> memberDtoList = new ArrayList<>();

        for (MemberEntity memberEntity : memberEntities) {
            MemberDto memberDto = MemberDto.builder()
                    .no(memberEntity.getNo())
                    .email(memberEntity.getEmail())
                    .name(memberEntity.getName())
                    .phone(memberEntity.getPhone())
                    .birthday(memberEntity.getBirthday())
                    .createdDate(memberEntity.getCreatedDate())
                    .build();

            memberDtoList.add(memberDto);
        
        }
        return memberDtoList;
    }
    
    @Transactional
    public MemberDto getMember(Long no) {
        Optional<MemberEntity> memberEntityWraper = memberRepository.findById(no);
        MemberEntity memberEntity = memberEntityWraper.get();

        MemberDto memberDto = MemberDto.builder()
        		.no(memberEntity.getNo())
                .email(memberEntity.getEmail())
                .name(memberEntity.getName())
                .phone(memberEntity.getPhone())
                .birthday(memberEntity.getBirthday())
                .createdDate(memberEntity.getCreatedDate())
                .build();

        return memberDto;
    }
}
