package com.who.dto;

import com.who.domain.entity.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private Long no;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String birthday;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public MemberEntity toEntity() {
        MemberEntity memberEntity = MemberEntity.builder()
                .no(no)
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .birthday(birthday)
                .build();
        return memberEntity;
    }

    @Builder
    public MemberDto(Long no, String email, String password,
    		String name, String phone, String birthday, 
    		LocalDateTime createdDate, LocalDateTime modifiedDate) {
    	this.no = no;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}