package com.who.config;

import com.who.domain.entity.Role;
import com.who.service.CustomOAuth2UserService;
import com.who.service.MemberService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.who.config.SocialType.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private MemberService memberService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //static 디렉터리의 하위 파일 목록은 인증 무시 (=항상통과)
        web.ignoring().antMatchers("/css/**", "/js/**", "/images/**", "/lib/**",
        		"/fonts/**","/image/**","/vendor/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	
		http
//		.csrf().disable()
		.authorizeRequests() //URL별 권한 관리를 설정하는 옵션의 시작점, 선언되야만 antMatcher를 사용할 수 있다.
		.antMatchers("/", "/oauth2/**","/image/**","/login/**","/myticket/**")
		.permitAll() //전체 권한 부여
        // 페이지 권한 설정
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/myinfo").hasRole("MEMBER")
        .antMatchers("/api/v1/**").hasRole(Role.MEMBER.name())
        .antMatchers("/**")
        .permitAll()

        .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType())
        .antMatchers("/google").hasAuthority(GOOGLE.getRoleType())
        .antMatchers("/kakao").hasAuthority(KAKAO.getRoleType())
        .antMatchers("/naver").hasAuthority(NAVER.getRoleType())
        .anyRequest().authenticated()

        .and() // 로그인 설정
        .formLogin()
        .loginPage("/login")
        .defaultSuccessUrl("/login/result")
        .loginProcessingUrl("/login/result")
        .permitAll()

        .and()
        .csrf()
		.ignoringAntMatchers("/check/findPw/sendEmail")
		.ignoringAntMatchers("/check/Pw")
		.ignoringAntMatchers("/check/Pw/changePw")
		.ignoringAntMatchers("/idCheck/sendEmail")
		.ignoringAntMatchers("/CertifiedCheck")

        .and() // 로그아웃 설정
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/logout/result")
        .invalidateHttpSession(true)

        .and()
        .oauth2Login()
        .loginPage("/login")
        .userInfoEndpoint()
        .userService(customOAuth2UserService); // 네이버 USER INFO의 응답을 처리하기 위한 설정

//        .and()
//        .exceptionHandling()
//        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));

    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
    		OAuth2ClientProperties oAuth2ClientProperties,
    		@Value("${custom.oauth2.kakao.client-id}") String kakaoClientId,
    		@Value("${custom.oauth2.kakao.client-secret}") String kakaoClientSecret,
    		@Value("${custom.oauth2.naver.client-id}") String naverClientId,
    		@Value("${custom.oauth2.naver.client-secret}") String naverClientSecret) {
    	List<ClientRegistration> registrations = oAuth2ClientProperties
    			.getRegistration().keySet().stream()
    			.map(client -> getRegistration(oAuth2ClientProperties, client))
    			.filter(Objects::nonNull)
    			.collect(Collectors.toList());

    	registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
    			.clientId(kakaoClientId)
    			.clientSecret(kakaoClientSecret)
    			.jwkSetUri("temp")
    			.build());

    	registrations.add(CustomOAuth2Provider.NAVER.getBuilder("naver")
    			.clientId(naverClientId)
    			.clientSecret(naverClientSecret)
    			.jwkSetUri("temp")
    			.build());
    	return new InMemoryClientRegistrationRepository(registrations); }


    private ClientRegistration getRegistration(OAuth2ClientProperties clientproperties, String client) {
    	
    	if("google".contentEquals(client)) {
    		OAuth2ClientProperties.Registration registration = 
    				clientproperties.getRegistration().get("google");
    		return CommonOAuth2Provider.GOOGLE.getBuilder(client)
    				.clientId(registration.getClientId())
    				.clientSecret(registration.getClientSecret())
    				.scope("email","profile")
    				.build();
    	}
    	if("facebook".contentEquals(client)) {
    		OAuth2ClientProperties.Registration registration = 
    				clientproperties.getRegistration().get("facebook");
    		return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
    				.clientId(registration.getClientId())
    				.clientSecret(registration.getClientSecret())
    				.userInfoUri("http://graph.facebook.com/me?fields=id,name,email,link")
    				.scope("email")
    				.build();
    	}
    	return null;
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
