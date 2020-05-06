package springboot.web;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import springboot.springboot.domain.posts.Posts;
import springboot.springboot.domain.posts.PostsRepository;
import springboot.web.dto.PostsSaveRequestDto;
import springboot.web.dto.PostsUpdateRequestDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)	// port�� �浹�� �� �ֱ� ������ port��ȣ ����
class PostsApiControllerTests {

	@LocalServerPort
	private int port;
	
	// RestURL 형태의 데이터를 전달할 수 있도록 제공해주는 클래스
	@Autowired
	private TestRestTemplate restTemplate;
	
	// web환경 가져옴
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mvc;
	
	// 그걸 mvc에 담음
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}
	
	@Autowired
	private PostsRepository postsRepository;
	
	@After
	public void tearDown() throws Exception {
		postsRepository.deleteAll();
	}
	
	@WithMockUser(roles = "USER")
	@Test
	public void Posts_등록된다() throws Exception {
		// given
		String title = "제목";
		String content = "내용";
		
		PostsSaveRequestDto requestDto =
				PostsSaveRequestDto.builder().title(title).content(content).author("작성자").build();
		String url = "http://localhost:" + port + "/api/v1/posts";
		
//		// post 방식으로 전달
//		// url 주소에 requestDto 전달 그럼 Long.class 타입 반환
//		// when : 테스트 호출
//		ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
//		
//		// then
//		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//		Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L); // 0L : 0보다 큰것 id는 0보다 커야한다 // getBody까지만 하면 id 값 나옴
		
		mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(MockMvcResultMatchers.status().isOk());
		
		List<Posts> all = postsRepository.findAll();
		Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
		Assertions.assertThat(all.get(0).getContent()).isEqualTo(content);
		
		
//		Optional<Posts> optional = postsRepository.findById(responseEntity.getBody());
//		if(optional.isPresent()) {
//			Posts posts = optional.get();
//			Assertions.assertThat(posts.getTitle()).isEqualTo(title);
//			Assertions.assertThat(posts.getContent()).isEqualTo(content);
//		}
	}
	
	@WithMockUser(roles = "USER")
	@Test
	public void Posts_수정된다() throws Exception { 
		// given
		// 새 데이터를 추가
		Posts savedPosts = postsRepository.save(Posts.builder().title("title").content("content").author("author").build());
		Long updateId = savedPosts.getId();
		
		String expectedTitle = "new title";
		String expectedContent = "new content";
		
		// 수정할 데이터
		PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder().title(expectedTitle).content(expectedContent).build();
		// 수정 API URL
		String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
		
		HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
		
//		// when
//		ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
//
//		// then
//		Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//		Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L);
		
		mvc.perform(MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(MockMvcResultMatchers.status().isOk());
		
		
		List<Posts> all = postsRepository.findAll();
		Assertions.assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
		Assertions.assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
	}

}
