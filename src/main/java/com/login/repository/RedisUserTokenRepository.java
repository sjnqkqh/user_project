package com.login.repository;

import com.login.domain.redis.RedisUserToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisUserTokenRepository extends CrudRepository<RedisUserToken, String> {

}
