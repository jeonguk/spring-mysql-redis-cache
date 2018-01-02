package com.jeonguk.web.service;

import org.springframework.data.domain.Page;

import com.jeonguk.web.entity.Post;

public interface PostService {

    Post findPostById(Long id);

    Page<Post> getAllPosts(Integer page, Integer size);
    
    Post savePost(Post post);

    Post updatePost(Post post);

    void deletePost(Long id);
}
