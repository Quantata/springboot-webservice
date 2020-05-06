package springboot.web;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class indexControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void 메인페이지_로딩() {
		String body = this.restTemplate.getForObject("/", String.class);
		System.out.println(body);
		
		Assertions.assertThat(body).contains("스프링 부트 웹 서비스");
	}

}
