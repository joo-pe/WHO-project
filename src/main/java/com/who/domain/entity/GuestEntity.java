package com.who.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class GuestEntity extends TimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String email;
	
	@Column
	private String picture;
	
	//기본적으로 JPA로 DB에 저장될 때 Enum은 int로 저장
	//숫자로 저장되면 DB로 확인할 때 값의 의미를 정확히 알지 못하므로
	//문자열로 저장할 수 있게 함
	@Enumerated(EnumType.STRING)
	@Column
	private Role role;
	
	@Builder
	public GuestEntity(String name, String email, String picture, Role role) {
		this.name = name;
		this.email = email;
		this.picture = picture;
		this.role = role;
	}
	
	public GuestEntity update(String name, String picture) {
		this.name = name;
		this.picture = picture;
		
		return this;
	}
	
	public String getRoleValue(){
        return this.role.getValue();
    }

}
