package com.who.domain;

import com.who.domain.entity.MemberEntity;
import com.who.domain.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class MemberDetail implements UserDetails {
    private MemberEntity memberEntity;

    public MemberDetail(MemberEntity memberEntity){
        this.memberEntity = memberEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        if(("admin").equals(memberEntity.getEmail())) {
            authorityList.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorityList.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }
        return authorityList;
    }

    @Override
    public String getPassword() {
        return memberEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return memberEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return memberEntity.isEnabled();
    }
}