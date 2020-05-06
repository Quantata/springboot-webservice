package springboot.springboot.domain.posts;


import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// length 안쓰면 문자열인 경우 크기 255 기본
	@Column(length = 500, nullable = false)
	private String title;
	
	// columnDefinition : ColumnType 등 선언할때 슴
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;
	
	private String author;
	
	@Builder
	public Posts(String title, String content, String author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}
	
	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}


}
