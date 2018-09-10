package com.jeonguk.web.service.impl;

import com.jeonguk.web.entity.Post;
import com.jeonguk.web.exception.ResourceNotFoundException;
import com.jeonguk.web.repository.PostRepository;
import com.jeonguk.web.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final RedisTemplate<String, Post> redisTemplate;

    @Override
    public Post findPostById(Long id) {
        final String key = "post_" + id;
        final ValueOperations<String, Post> operations = redisTemplate.opsForValue();
        final boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            final Post post = operations.get(key);
            log.info("PostServiceImpl.findPostById() : cache post >> " + post.toString());
            return post;
        }
        final Optional<Post> post = Optional.ofNullable(postRepository.findOne(id));
        if(post.isPresent()) {
            operations.set(key, post.get(), 10, TimeUnit.SECONDS);
            log.info("PostServiceImpl.findPostById() : cache insert >> " + post.get().toString());
            return post.get();
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public Page<Post> getAllPosts(Integer page, Integer size) {
        return postRepository.findAll(new PageRequest(page, size));
    }
    
    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);        
    }

    @Override
    public Post updatePost(Post post) {
        final String key = "post_" + post.getId();
        final boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            log.info("PostServiceImpl.updatePost() : cache delete >> " + post.toString());
        }
        return postRepository.save(post);        
    }

    @Override
    public void deletePost(Long id) {
        final String key = "post_" + id;
        final boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            log.info("PostServiceImpl.deletePost() : cache delete ID >> " + id);
        }
        final Optional<Post> post = Optional.ofNullable(postRepository.findOne(id));
        if(post.isPresent()) {
            postRepository.delete(id);
        } else {
            throw new ResourceNotFoundException();
        }
    }
    
}