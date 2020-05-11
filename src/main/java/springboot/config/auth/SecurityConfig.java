package springboot.config.auth;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;
import springboot.domain.user.Role;

// Spring Security 설정들을 활성화
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 설정정보 주입할 수 있는 클래스 상속받음
	
	// 구글 로그인을 통해 가져온 정보(email, name, picture 등)를 기반으로
	// 가입, 수정, 세션에 저장하는 등의 기능 제공
	private final CustomOAuth2UserService customOAuth2UserService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// 크로스 사이트 요청 위조가 가능하도록 설정
			// h2-console 화면을 사용하기 위해서 설정을 해제 (개발 용도)
			.csrf().disable()
			// 하나의 페이지에 여러가지 페이지를 끼워넣는 것을 가능하도록 함.
			// 그럼 가짜 페이지 넣을 수 있음.
			// 개발 용도로 풀어 놓음
			.headers().frameOptions().disable()
			.and()
				// URL별로 권한 관리 지정하겠다
				.authorizeRequests()
				// 권한 관리 대상을 지정
				.antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
				.antMatchers("/api/v1/**").hasRole(Role.USER.name())
				// 설정된 값을 제외한 나머지(anyRequest)에 대해서는 인증 받은 사용자만 허용
				.anyRequest().authenticated()
			.and()
				// logout 기능에 대해서 정의
				.logout()
					.logoutSuccessUrl("/")
			.and()
				// oauth2Login 기능에 대해 정의
				.oauth2Login()
					// OAuth2 로그인데 성공했을때 사용자 정보를 가져오는 방법을 설정
					.userInfoEndpoint()
						// 소셜 로그인에 성공했을 때 후속 조치를 구현한 구현체를 등록
						.userService(customOAuth2UserService);
				
	}
}
