package com.who.config;

import com.who.service.MemberService;
import lombok.AllArgsConstructor;

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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.who.config.SocialType.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private MemberService memberService;

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
        http.authorizeRequests()
        		
        		.antMatchers("/", "/oauth2/**","/image/**","/login/**")
        		.permitAll()
                // 페이지 권한 설정
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/myinfo").hasRole("MEMBER")
                .antMatchers("/**")
                .permitAll()
                .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType()) 
                .antMatchers("/google").hasAuthority(GOOGLE.getRoleType())
                .anyRequest().authenticated()

                .and() // 로그인 설정
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/login/result")
                .loginProcessingUrl("/login/result")
                .permitAll()

                .and() // 로그아웃 설정
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logout/result")
                .invalidateHttpSession(true)

                .and()
                // 403 예외처리 핸들링
                .exceptionHandling().accessDeniedPage("/denied");
//        		http.csrf().disable();
    }

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
