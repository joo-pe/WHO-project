package com.who.dto;

import java.util.Map;

import com.who.domain.entity.GuestEntity;
import com.who.domain.entity.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String picture;
	
	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
			String name, String email, String picture) {
		
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.picture = picture;
	}
	
	// of() : OAuth2User에서 반환하는 사용자정보는 Map이라서 값을 하나하나 변환해야 한다
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return ofGoole(userNameAttributeName, attributes);
    }
    
    private static OAuthAttributes ofGoole(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    
 // 첫 가입 시점에 User 엔티티를 생성
    public GuestEntity toEntity() {
        return GuestEntity.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)   //가입할때 기본원한을 GUEST
                .build();
    }

}
