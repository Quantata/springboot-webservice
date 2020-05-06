package springboot.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import springboot.service.PostsService;
import springboot.web.dto.PostsResponseDto;
import springboot.web.dto.PostsSaveRequestDto;
import springboot.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
	
	private final PostsService postsService;
	
	// 클라이언트에서 PostsSaveRequestDto 형식 데이터가 올거임. 그 데이터가 @RequestBody에 들어있음
	// POST 방식(method)은 요청 본문(request body)에 서버로 전달하는 내용(request parameter)을 포함해서 전달하는 방식
	// 그럼 본문에서 꺼내 써야할 것아님. 그래서 @RquestBody 사용
	@PostMapping("/api/v1/posts")
	public Long save(@RequestBody PostsSaveRequestDto requestDto) {
		// Long 타입은 게시판 번호 리턴 해주는 것
		return postsService.save(requestDto);
		
	}
	
	// 조회 => GET
	@GetMapping("/api/v1/posts/{id}")
	public PostsResponseDto findById(@PathVariable Long id) {
		return postsService.findById(id);
	}
	
	// 수정 => PUT
	@PutMapping("/api/v1/posts/{id}")
	public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
		return postsService.update(id, requestDto);
	}
	
	@DeleteMapping("/api/v1/posts/{id}")
	public Long delete(@PathVariable Long id) {
		postsService.delete(id);
		return id;
	}
}
