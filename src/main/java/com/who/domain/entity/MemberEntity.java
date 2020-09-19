package com.who.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

<<<<<<< HEAD
import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
=======
import java.sql.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
<<<<<<< HEAD
=======
@EqualsAndHashCode(of = "email")
>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec
@Table(name="member")
public class MemberEntity {
	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long no;
<<<<<<< HEAD
    
=======

>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec
    @Column(length = 50, nullable = false)
    private String email;
    
    @Column(length = 100, nullable = false)
    private String password;
    
    @Column(length = 20, nullable = false)
    private String name;
    
<<<<<<< HEAD
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
=======
    @Column(length = 20, nullable = false)
    private String phone;
    
    @Column(length = 20, nullable = false)
    private String birthday;
    
    @CreationTimestamp
	private Date created_date;

    @Builder
    MemberEntity(Long no, String email, String password,
    		String name, String phone, String birthday) {
    	
        this.no = no;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
>>>>>>> 971909878b3efee5999b8584ff37d1442e07acec
        this.birthday = birthday;
    }
}
