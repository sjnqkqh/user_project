package com.challenge.ably.repository;

import com.challenge.ably.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String loginId);

}
