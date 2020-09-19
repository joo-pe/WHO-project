package com.who.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long no;
    
    @Column(length = 50, nullable = false)
    private String email;
    
    @Column(length = 100, nullable = false)
    private String password;
    
    @Column(length = 20, nullable = false)
    private String name;
    
    @Column(length = 40, nullable = false)
    private String phon;
    
    @Column(length = 40, nullable = false)
    private String birthday;
    

    @Builder
    MemberEntity(Long no, String email, String name, String password , String phon, String birthday) {
    	this.no = no;
        this.email = email;    
        this.password = password;
        this.name = name;
        this.phon = phon;   
        this.birthday = birthday;
    }
}
