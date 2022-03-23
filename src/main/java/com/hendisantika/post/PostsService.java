package com.hendisantika.post;

import com.faunadb.client.FaunaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
