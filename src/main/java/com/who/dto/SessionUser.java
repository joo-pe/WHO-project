package com.who.dto;

import java.io.Serializable;

import com.who.domain.entity.GuestEntity;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable{
	
	private String name;
	private String email;
	private String picture;
	
	public SessionUser(GuestEntity user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.picture = user.getPicture();
	}

}
