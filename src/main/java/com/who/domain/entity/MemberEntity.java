package com.who.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EqualsAndHashCode(callSuper=false, of = "email")
@Table(name="member")
public class MemberEntity extends TimeEntity{
	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;
    
    @Column(length = 20, nullable = false)
    private String name;
    
    @Column(length = 20, nullable = false)
    private String phone;
    
    @Column(length = 20, nullable = false)
    private String birthday;
    
    @Column
    private boolean enabled;


    @Builder
    MemberEntity(Long id, String email, String password,
    			String name, String phone, String birthday, LocalDateTime createdDate,
    			boolean enabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.enabled = enabled;
    }
}