package com.jeonguk.web.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jeonguk.web.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>  {
    Page<Post> findAll(Pageable pageable);
}
