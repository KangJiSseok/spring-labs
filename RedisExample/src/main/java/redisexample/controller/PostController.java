package redisexample.controller;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import redisexample.domain.Post;
import redisexample.service.PostService;
import redisexample.service.ViewCountService;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	private final ViewCountService viewCountService;

	// ✅ 게시글 저장
	@PostMapping
	public Post create(@RequestBody Post post) {
		return postService.save(post);
	}

	// ✅ 게시글 조회 (캐시 사용됨)
	@GetMapping("/{id}")
	public Post get(@PathVariable Long id) {
		// 조회수 증가
		viewCountService.increaseViewCount(id);

		// 캐시된 Post 조회
		return postService.getPost(id);
	}

	// ✅ 게시글 수정 (캐시 무효화됨)
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @RequestBody Post updated) {
		postService.updatePost(id, updated);
	}

	@GetMapping("/{id}/views")
	public int getViews(@PathVariable Long id) {
		return viewCountService.getViewCount(id);
	}
}
