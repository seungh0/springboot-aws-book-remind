package com.springboot.book.service;

import com.springboot.book.domain.posts.Posts;
import com.springboot.book.domain.posts.PostsRepository;
import com.springboot.book.web.dto.PostsListResponseDto;
import com.springboot.book.web.dto.PostsResponseDto;
import com.springboot.book.web.dto.PostsSaveRequestDto;
import com.springboot.book.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

	private final PostsRepository postsRepository;

	@Transactional
	public Long save(PostsSaveRequestDto requestDto) {
		return postsRepository.save(requestDto.toEntity()).getId();
	}

	@Transactional
	public Long update(Long id, PostsUpdateRequestDto requestDto) {
		Posts posts = findPostsById(id);
		posts.update(requestDto.getTitle(), requestDto.getContent());
		return id;
	}

	public PostsResponseDto findById(Long id) {
		Posts posts = findPostsById(id);
		return new PostsResponseDto(posts);
	}

	@Transactional(readOnly = true) // 조회 속도 개
	public List<PostsListResponseDto> findAllDesc() {
		return postsRepository.findAllDesc().stream()
				.map(PostsListResponseDto::new)
				.collect(Collectors.toList());
	}

	@Transactional
	public void delete(Long id) {
		Posts posts = findPostsById(id);
		postsRepository.delete(posts);
	}

	private Posts findPostsById(Long id) {
		return postsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));
	}

}
