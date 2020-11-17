package com.who.config;

import org.springframework.security.oauth2.client.registration.ClientRegistration; 
import org.springframework.security.oauth2.core.AuthorizationGrantType; 
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public enum CustomOAuth2Provider {

	KAKAO { 
		@Override 
		public ClientRegistration.Builder getBuilder(String registrationId) { 
			ClientRegistration.Builder builder = getBuilder(registrationId, 
				ClientAuthenticationMethod.POST, DEFAULT_LOGIN_REDIRECT_URL); 
			builder.scope("profile"); // 요청할 권한
			builder.authorizationUri("https://kauth.kakao.com/oauth/authorize"); // authorization code 얻는 API
			builder.tokenUri("https://kauth.kakao.com/oauth/token"); // access Token 얻는 API
			builder.userInfoUri("https://kapi.kakao.com/v2/user/me"); // 유저 정보 조회 API
			builder.userNameAttributeName("id"); // userInfo API Response에서 얻어올 ID 프로퍼티
			builder.clientName("Kakao"); // spring 내에서 인식할 OAuth2 Provider Name
			return builder; 
		} 
	},
	
	NAVER { 
		@Override 
		public ClientRegistration.Builder getBuilder(String registrationId) { 
			ClientRegistration.Builder builder = getBuilder(registrationId, 
				ClientAuthenticationMethod.POST, DEFAULT_LOGIN_REDIRECT_URL); 
			builder.scope("profile"); 
			builder.authorizationUri("https://nid.naver.com/oauth2.0/authorize"); 
			builder.tokenUri("https://nid.naver.com/oauth2.0/token"); 
			builder.userInfoUri("https://openapi.naver.com/v1/nid/me"); 
			builder.userNameAttributeName("id"); 
			builder.clientName("Naver"); 
			return builder; 
		} 
	};

	private static final String DEFAULT_LOGIN_REDIRECT_URL = 
		"{baseUrl}/login/oauth2/code/{registrationId}"; 
	
	protected final ClientRegistration.Builder getBuilder( 
		String registrationId, ClientAuthenticationMethod method, String redirectUri) { 
			ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId); 
			builder.clientAuthenticationMethod(method); 
			builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE); 
			builder.redirectUriTemplate(redirectUri); 
			return builder; 
		} 
	
	public abstract ClientRegistration.Builder getBuilder(String registrationId); 
	
}

