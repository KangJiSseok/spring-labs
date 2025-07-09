package redisexample.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import redisexample.domain.Post;
import redisexample.repository.PostRepository;

@Service
@Slf4j
public class PostService {

	private final PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public Post save(Post post) {
		return postRepository.save(post);
	}

	@Cacheable(value = "post", key = "#postId")
	public Post getPost(Long postId) {
		log.info("📦 DB에서 조회합니다.");
		return postRepository.findById(postId)
			.orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
	}

	@CacheEvict(value = "post", key = "#postId")
	public void updatePost(Long postId, Post updated) {
		Post post = postRepository.findById(postId).orElseThrow();
		post.setTitle(updated.getTitle());
		post.setContent(updated.getContent());
		postRepository.save(post);
	}
}
