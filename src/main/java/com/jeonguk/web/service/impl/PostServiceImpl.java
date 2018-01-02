package com.jeonguk.web.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.jeonguk.web.entity.Post;
import com.jeonguk.web.repository.PostRepository;
import com.jeonguk.web.service.PostService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RedisTemplate<String, Post> redisTemplate;

    @Override
    public Post findPostById(Long id) {
        String key = "post_" + id;
        ValueOperations<String, Post> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            Post post = operations.get(key);
            log.info("PostServiceImpl.findPostById() : cache post >> " + post.toString());
            return post;
        }
        Post post = postRepository.findOne(id);
        operations.set(key, post, 10, TimeUnit.SECONDS);
        log.info("PostServiceImpl.findPostById() : cache insert >> " + post.toString());
        return post;
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
        String key = "post_" + post.getId();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            log.info("PostServiceImpl.updatePost() : cache delete >> " + post.toString());
        }
        return postRepository.save(post);        
    }

    @Override
    public void deletePost(Long id) {
        String key = "post_" + id;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            log.info("PostServiceImpl.deletePost() : cache delete ID >> " + id);
        }
        postRepository.delete(id);
    }
    
}