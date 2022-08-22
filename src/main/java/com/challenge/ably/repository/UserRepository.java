package com.challenge.ably.repository;

import com.challenge.ably.domain.User;
import com.challenge.ably.util.YnCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginIdAndDeleteYn(String loginId, YnCode deleteYn);

    Optional<User> findFirstByLoginIdAndEmailAndDeleteYnOrderByCreatedAtDesc(String loginId, String email, YnCode deleteYn);

    Optional<User> findFirstByLoginIdAndDeleteYnOrderByCreatedAtDesc(String loginId, YnCode deleteYn);

    Optional<User> findFirstByAccessTokenAndDeleteYnOrderByCreatedAtDesc(String accessToken, YnCode deleteYn);



}
