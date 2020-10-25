package com.springboot.book.config;

import com.springboot.book.config.auth.CustomOAuth2UserService;
import com.springboot.book.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2UserService customOAuth2UserService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
					.headers().frameOptions().disable()
				.and()
					.authorizeRequests()
					.antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() /// 전체 열람 권한
					.antMatchers("/api/v1/**").hasRole(Role.USER.name()) // POST 메소드이면서 /api/v1/** 주소를 가진 API는 USER 권한을 가진 사람만 가능
					.anyRequest().authenticated() // 나머지 URL은 모두 인증된 사용자들에게만 허용 가능 (로그인한 사용자들)
				.and()
					.logout()
						.logoutSuccessUrl("/") // 로그아웃 성공시 / 주소로 이동
				.and()
					.oauth2Login() // OAuth2 로그인 기능에 대한 진입점
						.userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올때의 설정 담당
							.userService(customOAuth2UserService); // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록.
	}

}
