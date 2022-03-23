package com.hendisantika.user;

import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.concurrent.ExecutionException;

import static com.faunadb.client.query.Language.Get;
import static com.faunadb.client.query.Language.Index;
import static com.faunadb.client.query.Language.Map;
import static com.faunadb.client.query.Language.Match;
import static com.faunadb.client.query.Language.Paginate;
import static com.faunadb.client.query.Language.Var;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-faunadb
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 23/03/22
 * Time: 07.25
 */
public class FaunaUserDetailsService implements UserDetailsService {
    private final FaunaClient faunaClient;

    public FaunaUserDetailsService(FaunaClient faunaClient) {
        this.faunaClient = faunaClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Value user = faunaClient.query(Map(
                            Paginate(Match(Index("users_by_username"), Value(username))),
                            Lambda(Value("user"), Get(Var("user")))))
                    .get();

            Value userData = user.at("data").at(0).orNull();
            if (userData == null) {
                throw new UsernameNotFoundException("User not found");
            }

            return User.withDefaultPasswordEncoder()
                    .username(userData.at("data", "username").to(String.class).orNull())
                    .password(userData.at("data", "password").to(String.class).orNull())
                    .roles("USER")
                    .build();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
