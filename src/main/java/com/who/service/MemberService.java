package com.who.service;

import com.who.domain.Role;
import com.who.domain.entity.MemberEntity;
import com.who.domain.repository.MemberRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
	 
		@Autowired
		private MemberRepository memberRepository;

	    @Transactional
	    public String joinUser(MemberDto memberDto) {
	        // 비밀번호 암호화
	               BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

	        return memberRepository.save(memberDto.toEntity()).getId();
	    }

	    @Override
	    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
	        Optional<MemberEntity> userEntityWrapper = memberRepository.findById(userId);
	        MemberEntity userEntity = userEntityWrapper.get();

	        List<GrantedAuthority> authorities = new ArrayList<>();

	        if (("kwonga3").equals(userId)) {
	            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
	        } else {
	            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
	        }

	        return new User(userEntity.getId(), userEntity.getPassword(), authorities);
	    }
	}
