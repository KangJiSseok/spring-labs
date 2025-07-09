package redisexample.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViewCountService {
	private final RedisTemplate<String, Integer> redisTemplate;

	public ViewCountService(RedisTemplate<String, Integer> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void increaseViewCount(Long postId) {
		String key = "view:post:" + postId;
		redisTemplate.opsForValue().increment(key);
	}

	public int getViewCount(Long postId) {
		String key = "view:post:" + postId;
		Integer count = redisTemplate.opsForValue().get(key);
		return count != null ? count : 0;
	}
}
