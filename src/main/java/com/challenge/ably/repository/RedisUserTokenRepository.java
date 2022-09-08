package com.challenge.ably.repository;

import com.challenge.ably.domain.redis.RedisUserToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisUserTokenRepository extends CrudRepository<RedisUserToken, String> {

}
