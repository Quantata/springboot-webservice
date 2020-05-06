package springboot.domain.posts;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import springboot.springboot.domain.posts.Posts;
import springboot.springboot.domain.posts.PostsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTests {
	@Autowired
	PostsRepository postsRepository;
	
	// Repostiory가 여러번 갖고오면 0번째가 다른 애일 수 있음
	@After
	public void cleanup() {
		postsRepository.deleteAll();
	}
	
	@Test
	public void 게시글저장_불러오기() {
		String title = "테스트 제목";
		String content = "테스트 내용";
		
		// 저장
		postsRepository.save(Posts.builder().title(title).content(content).author("안나연").build());
		List<Posts> postList = postsRepository.findAll();
		
		Posts posts = postList.get(0);
		
		Assertions.assertThat(posts.getTitle()).isEqualTo(title);
		Assertions.assertThat(posts.getContent()).isEqualTo(content);
	}
}	
