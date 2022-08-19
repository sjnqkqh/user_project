package com.challenge.ably.repository;

import com.challenge.ably.domain.User;
import com.challenge.ably.util.YnCode;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginIdAndDeleteYn(String loginId, YnCode deleteYn);

}
