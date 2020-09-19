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
<<<<<<< HEAD
    private String phon;
    private String birthday;
    

    public MemberEntity toEntity() {
        MemberEntity memberEntity = MemberEntity.builder()
                .email(email)
                .password(password)
                .name(name)
                .birthday(birthday)
                .phon(phon)
=======
    private String phone;
    private String birthday;
    private LocalDateTime createdDate;

    public MemberEntity toEntity() {
        MemberEntity memberEntity = MemberEntity.builder()
                .no(no)
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .birthday(birthday)
>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec
                .build();
        return memberEntity;
    }

    @Builder
<<<<<<< HEAD
    public MemberDto(Long no, String email, String name, String password, String birthday, String phon) {
        this.no = no;
=======
    public MemberDto(Long no, String email, String password,
    		String name, String phone, String birthday) {
    	this.no = no;
>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec
        this.email = email;
        this.name = name;
        this.password = password;
<<<<<<< HEAD
        this.birthday = birthday;
        this.phon = phon;
        
=======
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec
    }
}
