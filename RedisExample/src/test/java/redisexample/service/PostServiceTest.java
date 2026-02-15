package redisexample.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import redisexample.domain.Post;
import redisexample.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@Mock
	private PostRepository postRepository;

	@InjectMocks
	private PostService postService;

	@Test
	void givenValidPost_whenSave_thenReturnsSavedPost() {
		// Given
		Post input = new Post("title", "content");
		Post saved = new Post("title", "content");
		when(postRepository.save(input)).thenReturn(saved);

		// When
		Post result = postService.save(input);

		// Then
		assertEquals(saved, result);
		verify(postRepository, times(1)).save(input);
	}

	@Test
	void givenPostIdIsZero_whenGetPost_thenReturnsPost() {
		// Given
		Long postId = 0L;
		Post post = new Post("zero-id", "boundary");
		when(postRepository.findById(postId)).thenReturn(Optional.of(post));

		// When
		Post result = postService.getPost(postId);

		// Then
		assertEquals(post, result);
		verify(postRepository, times(1)).findById(postId);
	}

	@Test
	void givenPostExists_whenUpdatePost_thenUpdatesAndSavesPost() {
		// Given
		Long postId = 1L;
		Post existing = new Post("before-title", "before-content");
		Post updated = new Post("after-title", "after-content");
		when(postRepository.findById(postId)).thenReturn(Optional.of(existing));
		when(postRepository.save(existing)).thenReturn(existing);

		// When
		postService.updatePost(postId, updated);

		// Then
		ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
		verify(postRepository, times(1)).save(captor.capture());
		assertEquals("after-title", captor.getValue().getTitle());
		assertEquals("after-content", captor.getValue().getContent());
	}

	@Test
	void givenUpdatedPostWithNullTitle_whenUpdatePost_thenSavesPostWithNullTitle() {
		// Given
		Long postId = 2L;
		Post existing = new Post("before-title", "before-content");
		Post updated = new Post(null, "updated-content");
		when(postRepository.findById(postId)).thenReturn(Optional.of(existing));
		when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// When
		postService.updatePost(postId, updated);

		// Then
		assertNull(existing.getTitle());
		assertEquals("updated-content", existing.getContent());
		verify(postRepository, times(1)).save(existing);
	}

	@Test
	void givenUpdatedPostWithEmptyContent_whenUpdatePost_thenSavesPostWithEmptyContent() {
		// Given
		Long postId = 3L;
		Post existing = new Post("before-title", "before-content");
		Post updated = new Post("updated-title", "");
		when(postRepository.findById(postId)).thenReturn(Optional.of(existing));
		when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// When
		postService.updatePost(postId, updated);

		// Then
		assertEquals("updated-title", existing.getTitle());
		assertEquals("", existing.getContent());
		verify(postRepository, times(1)).save(existing);
	}

	@Test
	void givenPostNotFound_whenGetPost_thenThrowsRuntimeException() {
		// Given
		Long postId = 999L;
		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		// When
		RuntimeException exception = assertThrows(RuntimeException.class, () -> postService.getPost(postId));

		// Then
		assertEquals("게시글을 찾을 수 없습니다.", exception.getMessage());
		verify(postRepository, times(1)).findById(postId);
	}

	@Test
	void givenPostNotFound_whenUpdatePost_thenThrowsNoSuchElementException() {
		// Given
		Long postId = 999L;
		Post updated = new Post("after-title", "after-content");
		when(postRepository.findById(postId)).thenReturn(Optional.empty());

		// When
		NoSuchElementException exception = assertThrows(NoSuchElementException.class,
			() -> postService.updatePost(postId, updated));

		// Then
		assertEquals(NoSuchElementException.class, exception.getClass());
		verify(postRepository, times(1)).findById(postId);
		verify(postRepository, never()).save(any(Post.class));
	}
}
