package springboot.config.auth.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import net.minidev.json.JSONObject;
import springboot.domain.user.Role;
import springboot.domain.user.User;

@Getter
public class OAuthAttributes {
	
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String picture;
	
	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.picture = picture;
	}
	
	// userNameAttribute : 소셜 로그인 쪽에서 제공해주는 key가 명시 . Naver 같은 경우는 response에서 넘어감.
	// response 안에 있는 특정 값에 들어 있음.
	// registrationId : 소셜 로그인(google, naver, facebook, ...) 구분을 위한 용도
	public static OAuthAttributes of(String registrationId, 
			String userNameAttributeName, Map<String, Object> attributes) {
		
		System.out.println(registrationId);
		System.out.println(getJsonStringFromMap(attributes));
		
		// 네이버인 경우
		if ("naver".contentEquals(registrationId)) {
			return ofNaver("id", attributes);
		}
		
		return ofGoogle(userNameAttributeName, attributes);

	}
	
	// Map에 소셜쪽에서 던져주는 정보들 있음.
		public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
			
			// response라는 최상위 객체에서 데이터 뽑아옴
			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
			
			return OAuthAttributes.builder()
					.name((String)response.get("name"))
					.email((String)response.get("email"))
					.picture((String)response.get("profileImage"))
					.attributes(response)
					.nameAttributeKey(userNameAttributeName)
					.build();
		}
	
	// Map에 소셜쪽에서 던져주는 정보들 있음.
	public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
				.name((String)attributes.get("name"))
				.email((String)attributes.get("email"))
				.picture((String)attributes.get("picture"))
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.build();
	}
	
	public static JSONObject getJsonStringFromMap( Map<String, Object> map )    {
        JSONObject jsonObject = new JSONObject();
        for( Map.Entry<String, Object> entry : map.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            jsonObject.put(key, value);
        }
        
        return jsonObject;
    }

	
	public User toEntity() {
		return User.builder()
				.name(this.name)
				.email(this.email)
				.picture(this.picture)
				.role(Role.GUEST)
				.build();
	}

	 
}
