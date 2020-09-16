package com.who.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "member")
@EqualsAndHashCode(of = "id")
@ToString
public class MemberEntity {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long no;
	
    @Column(length = 20, nullable = false)
    private String id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String password;
    
    @Column(length = 20, nullable = false)
    private String phone;
    
    @Column(length = 20, nullable = false)
    private String birthday;
    
    @CreationTimestamp
    private LocalDateTime createdDate;

	@Builder
    public MemberEntity(Long no, String id, String name, String password, 
    			String phone, String birthday) {
		
		this.no = no;
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.birthday = birthday;
        
    }

}