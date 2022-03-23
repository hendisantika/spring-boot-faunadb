package com.hendisantika.post;

import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.faunadb.client.query.Language.*;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-faunadb
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 23/03/22
 * Time: 08.02
 */
@Component
public class PostsService {
    @Autowired
    private FaunaClient faunaClient;

    Post getPost(String id, Long before) throws ExecutionException, InterruptedException {
        var query = Get(Ref(Collection("posts"), id));
        if (before != null) {
            query = At(Value(before - 1), query);
        }

        var postResult = faunaClient.query(
                Let(
                        "post", query
                ).in(
                        Obj(
                                "post", Var("post"),
                                "author", Get(Select(Arr(Value("data"), Value("authorRef")), Var("post")))
                        )
                )).get();

        return parsePost(postResult);
    }

    List<Post> getAllPosts() throws ExecutionException, InterruptedException {
        var postsResult = faunaClient.query(Map(
                Paginate(
                        Join(
                                Documents(Collection("posts")),
                                Index("posts_sort_by_created_desc")
                        )
                ),
                Lambda(
                        Arr(Value("extra"), Value("ref")),
                        Obj(
                                "post", Get(Var("ref")),
                                "author", Get(Select(Arr(Value("data"), Value("authorRef")), Get(Var("ref"))))
                        )
                )
        )).get();

        var posts = postsResult.at("data").asCollectionOf(Value.class).get();
        return posts.stream().map(this::parsePost).collect(Collectors.toList());
    }
}
