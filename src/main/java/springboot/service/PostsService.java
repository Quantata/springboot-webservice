package springboot.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import springboot.springboot.domain.posts.Posts;
import springboot.springboot.domain.posts.PostsRepository;
import springboot.web.dto.PostsListResponseDto;
import springboot.web.dto.PostsResponseDto;
import springboot.web.dto.PostsSaveRequestDto;
import springboot.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor	// @Autowired해버리면 final이라 postsRepository를 재정의할 수가 없음.
@Service
public class PostsService {
	
	//fianl을 넣으려면 postsRepository를 초기화 해줘야하고, 그러려면 이 필드값을 가지는 생성자 생성
	// final을 안쓰면 autowired해도 됨. 근데 final 쓰는 이유 불필요한 변경 막기 위해
	private final PostsRepository postsRepository;
	
	// 원래 조회 안하는데 조회에도 Transaction 붙이는 이유 : 성능 향상을 위해
	// @Transactional(readOnly = true) 붙여주는 이유
	@Transactional(readOnly = true)
	public List<PostsListResponseDto> findAllDesc() {
		/* 옛날 방식, but java 8 Lamda 나오면서 바뀜 */
		/*
		List results = new ArrayList();
		List<Posts> list = postsRepository.findAllDesc();
		for( int i = 0; i < list.size(); i++ ) {
			Posts post = list.get(i);
			PostsListResponseDto dto = new PostsListResponseDto(post);
			results.add(dto);
		}
		return results;
		*/
		
		// posts가 하나씩 들어 오면은 개별적인 posts를 PostListResponseDto를 만드는데 사용해라
		// 그 결과를 collect해. 그 list가 5개 있으면은 PostListResponseDto가 5개 라는 것임
		// 그걸 List로 만들어서 반환해라
		return postsRepository.findAllDesc().stream().map(posts -> new PostsListResponseDto(posts)).collect(Collectors.toList());
		/* 한번 더 축약 */
//		postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
		
	}
	
	@Transactional	// 이 함수 내 기능들이 하나의 묶음
	public Long save(PostsSaveRequestDto requestDto) {
		
		return postsRepository.save(requestDto.toEntity()).getId();
		
	}

	public PostsResponseDto findById(Long id) {
		Optional<Posts> optional = postsRepository.findById(id);
		if(optional.isPresent()) {
			Posts entity = optional.get();
			return new PostsResponseDto(entity);
		} else {
			throw new IllegalArgumentException("일치하는 정보가 존재하지 않습니다.");
		}
	}

	@Transactional
	public Long update(Long id, PostsUpdateRequestDto requestDto) {
		Optional<Posts> optional = postsRepository.findById(id);
		if (optional.isPresent()) {
			Posts entity = optional.get();
			// entity를 update
			entity.update(requestDto.getTitle(), requestDto.getContent());
			return postsRepository.save(entity).getId();
		} else {
			throw new IllegalArgumentException("일치하는 정보가 존재하지 않습니다.");
		}
	}
	
	public void delete(Long id) {
		postsRepository.deleteById(id);
	}

}

   