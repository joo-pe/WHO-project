package com.who.dto;

import com.who.domain.entity.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String privatekey;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String birthday;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean enabled;

    public MemberEntity toEntity() {
       
        MemberEntity memberEntity = MemberEntity.builder()
                .id(id)
                .privatekey(privatekey)
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .birthday(birthday)
                .enabled(enabled)
                .build();
        return memberEntity;
    }

    @Builder
    public MemberDto(Long id, String privatekey, String email, String password,
                String name, String phone, String birthday, 
                LocalDateTime createdDate, LocalDateTime modifiedDate,
                Boolean enabled) {
        this.id = id;
        this.privatekey = privatekey;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.enabled = true;
    }
}