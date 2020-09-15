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
    private String id;
    private String password;
    private String name;
    private String phone;
    private String birthday;
    private LocalDateTime createdDate;

	public MemberEntity toEntity(){
        return MemberEntity.builder()
        		.no(no)
                .id(id)
                .name(name)
                .password(password)
                .phone(phone)
                .birthday(birthday)
                .build();
    }

    @Builder
    public MemberDto(Long no, String id, String name, String password, 
			String phone, String birthday) {
    	
    	this.no = no;
    	this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.birthday = birthday;
    }
}
