package springboot.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ProfileController {
	
	// env는 ProfileController의 의존 객체. 의존 객체를 해당하는 클래스에서 직접 생성하면 의존성 높아짐.
	// 그래서 DI 해주는 것. - 1. 생성자 이용  2.setter 3. @Component 등
	// 그 중에서 생성자를 이용해서 멤버 필드값을 초기화 시키겠다. 그래서 @RequiredArgsConstructor 사용
	// final이면 생성자 이용해서 초기화 시켜줘야함.
	private final Environment env;
	
	@GetMapping("/profile")
	public String profile() {
		// env.getActiveProfiles() : 현재 실행중인 ActiveProfile을 모두 가져옴.
		// ActiveProfile : application.properties에 보면 spring.profiles 가 있음. 여기에 명시된 profiles 불러옴.
		List<String> profiles = Arrays.asList(env.getActiveProfiles());
		String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);
		// 첫번째 springboot는 real1, 두번째는 real2
		// 첫번째는 8001, 두번째는 8002 이렇게 분리
		List<String> realProfiles = Arrays.asList("real", "real1", "real2");
		return profiles.parallelStream().filter(realProfiles::contains).findAny().orElse(defaultProfile);
	}
	
}
