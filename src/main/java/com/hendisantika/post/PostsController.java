package com.hendisantika.post;

import com.faunadb.client.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-faunadb
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 23/03/22
 * Time: 08.04
 */
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostsService postsService;

    @GetMapping
    public List<Post> listPosts(@RequestParam(value = "author", required = false) String author) throws ExecutionException, InterruptedException {
        return author == null ? postsService.getAllPosts() : postsService.getAuthorPosts("naruto");
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable("id") String id, @RequestParam(value = "before", required = false) Long before)
            throws ExecutionException, InterruptedException {
        return postsService.getPost(id, before);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void createPost(@RequestBody UpdatedPost post) throws ExecutionException, InterruptedException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        postsService.createPost(name, post.title(), post.content());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void updatePost(@PathVariable("id") String id, @RequestBody UpdatedPost post)
            throws ExecutionException, InterruptedException {
        postsService.updatePost(id, post.title(), post.content());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void postNotFound() {
    }
}
