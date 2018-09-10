package com.jeonguk.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeonguk.web.SpringMysqlRedisCacheApplication;
import com.jeonguk.web.entity.Post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringMysqlRedisCacheApplication.class)
@ActiveProfiles("test")
public class PostControllerTest {

    private static final String RESOURCE_LOCATION_PATTERN = "http://localhost/api/posts/[0-9]+";
    
    @Autowired
    WebApplicationContext context;
    
    private MockMvc mvc;
    
    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
    @Test
    public void shouldCreateRetrieveDelete() throws Exception {
        Post p1 = mockPost("shouldCreateRetrieveDelete");
        byte[] r1Json = toJson(p1);
        
        // CREATE
        MvcResult result = mvc.perform(post("/api/posts")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern())
                .andReturn();
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());
        
        // RETRIEVE
        mvc.perform(get("/api/posts/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.title", is(p1.getTitle())))
                .andExpect(jsonPath("$.content", is(p1.getContent())));

        // DELETE
        mvc.perform(delete("/api/posts/" + id))
                .andExpect(status().isNoContent());

    }
    
    
    @Test
    public void shouldCreateAndUpdateAndDelete() throws Exception {
        Post p1 = mockPost("shouldCreateAndUpdate");
        byte[] r1Json = toJson(p1);
        //CREATE
        MvcResult result = mvc.perform(post("/api/posts")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern())
                .andReturn();
        long id = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        Post p2 = mockPost("shouldCreateAndUpdate2");
        p2.setId(id);
        byte[] r2Json = toJson(p2);

        // UPDATE
        result = mvc.perform(put("/api/posts/" + id)
                .content(r2Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        // RETRIEVE updated
        mvc.perform(get("/api/posts/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.title", is(p2.getTitle())))
                .andExpect(jsonPath("$.content", is(p2.getContent())));

        // DELETE
        mvc.perform(delete("/api/posts/" + id))
                .andExpect(status().isNoContent());
    }
    
    // ========================================================== //
    
    private long getResourceIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return Long.valueOf(parts[parts.length - 1]);
    }
    
    private Post mockPost(String prefix) {
        Post post = new Post(); 
        post.setTitle(prefix + "_title");
        post.setContent(prefix + "_content");
        return post;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }

    // match redirect header URL (aka Location header)
    private static ResultMatcher redirectedUrlPattern() {
        return result -> {
            Pattern pattern = Pattern.compile("\\A" + PostControllerTest.RESOURCE_LOCATION_PATTERN + "\\z");
            assertTrue(pattern.matcher(result.getResponse().getRedirectedUrl()).find());
        };
    }

}
