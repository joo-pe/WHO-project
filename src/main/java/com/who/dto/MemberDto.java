package com.who.dto;

import com.who.domain.entity.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {
    private Long no;
    private String email;
    private String password;
    private String name;
    private String phon;
    private String birthday;
    

    public MemberEntity toEntity() {
        MemberEntity memberEntity = MemberEntity.builder()
                .email(email)
                .password(password)
                .name(name)
                .birthday(birthday)
                .phon(phon)
                .build();
        return memberEntity;
    }

    @Builder
    public MemberDto(Long no, String email, String name, String password, String birthday, String phon) {
        this.no = no;
        this.email = email;
        this.name = name;
        this.password = password;
        this.birthday = birthday;
        this.phon = phon;
        
    }
}
