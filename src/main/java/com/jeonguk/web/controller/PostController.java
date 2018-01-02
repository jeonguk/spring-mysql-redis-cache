package com.jeonguk.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jeonguk.web.entity.Post;
import com.jeonguk.web.service.PostService;

@RestController
public class PostController extends AbstractRestHandler {
    
    @Autowired
    private PostService postService;
    
    @RequestMapping(value = "/api/posts/{id}", method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public Post findOnePost(@PathVariable("id") Long id) {
        return postService.findPostById(id);
    }

    @RequestMapping(value = "/api/posts",
            method = RequestMethod.GET,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Page<Post> getAllPosts(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                           @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        return postService.getAllPosts(page, size);
    }
    
    @RequestMapping(value = "/api/posts", method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createPost(@RequestBody Post city,
            HttpServletRequest request, HttpServletResponse response) {
        Post createPost = postService.savePost(city);
        response.setHeader("Location", request.getRequestURL().append("/").append(createPost.getId()).toString());
    }

    @RequestMapping(value = "/api/posts/{id}", method = RequestMethod.PUT,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyPost(@PathVariable("id") Long id, @RequestBody Post city) {
        checkResourceFound(this.postService.findPostById(id));
        postService.updatePost(city);
    }

    @RequestMapping(value = "/api/posts/{id}", method = RequestMethod.DELETE,
            produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") Long id) {
        checkResourceFound(this.postService.findPostById(id));
        postService.deletePost(id);
    }
    
}