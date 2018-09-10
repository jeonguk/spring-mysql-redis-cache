package com.jeonguk.web.controller;

import com.jeonguk.web.entity.Post;
import com.jeonguk.web.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/posts")
@Api(tags = {"posts"})
public class PostController extends AbstractRestHandler {

    private final PostService postService;

    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single post.", notes = "You have to provide a valid post ID.")
    public Post findOnePost(@ApiParam(value = "The ID of the post.", required = true) @PathVariable("id") Long id) {
        log.info("Post PostController {}", postService.findPostById(id));
        return postService.findPostById(id);
    }

    @GetMapping(produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a paginated list of all posts.", notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
    public @ResponseBody Page<Post> getAllPosts(@ApiParam(value = "The page number (zero-based)", required = true)
                           @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUM) Integer page,
                           @ApiParam(value = "Tha page size", required = true) 
                           @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) Integer size) {
        return postService.getAllPosts(page, size);
    }

    @PostMapping(consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a post resource.", notes = "Returns the URL of the new resource in the Location header.")
    public void createPost(@RequestBody Post post,
            HttpServletRequest request, HttpServletResponse response) {
        final Post createPost = postService.savePost(post);
        response.setHeader("Location", request.getRequestURL().append("/").append(createPost.getId()).toString());
    }

    @PutMapping(value = "/{id}", consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a post resource.", notes = "You have to provide a valid post ID in the URL and in the payload. The ID attribute can not be updated.")
    public void modifyPost(@ApiParam(value = "The ID of the existing post resource.", required = true) @PathVariable("id") Long id, @RequestBody Post post) {
        checkResourceFound(this.postService.findPostById(id));
        postService.updatePost(post);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a post resource.", notes = "You have to provide a valid post ID in the URL. Once deleted the resource can not be recovered.")
    public void deletePost(@ApiParam(value = "The ID of the existing post resource.", required = true) @PathVariable("id") Long id) {
        checkResourceFound(this.postService.findPostById(id));
        postService.deletePost(id);
    }
    
}