package com.hendisantika.post;

import java.time.Instant;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-faunadb
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 23/03/22
 * Time: 08.01
 */
public record Post(String id, String title, String content, Author author, Instant created, Long version) {
}
