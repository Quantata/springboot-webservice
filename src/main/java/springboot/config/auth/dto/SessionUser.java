package springboot.config.auth.dto;

import java.io.Serializable;

import lombok.Getter;
import springboot.domain.user.User;

/* 직렬화
 * 자바 시스템 내부에서 사용되는 객체 또는 데이터를 외부의 자바 시스템에서도 사용할 수 있도록 바이트(byte) 형태로 데이터를 변환하는 기술
 */
@Getter
public class SessionUser implements Serializable {
	// 이러한 자바코드를 웹서버가 이해 못할 수 있음. 그래서 이를 일정한 규칙을 통해 데이터를 변환
	private String name;
	private String email;
	private String picture;
	
	public SessionUser(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.picture = user.getPicture();
	}
}
