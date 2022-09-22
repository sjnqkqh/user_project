package com.login.repository;

import com.login.domain.User;
import com.login.util.code.YnCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginIdAndUseYn(String loginId, YnCode deleteYn);

    Optional<User> findFirstByLoginIdAndEmailAndUseYnOrderByCreatedAtDesc(String loginId, String email, YnCode deleteYn);

    Optional<User> findFirstByLoginIdAndUseYnOrderByCreatedAtDesc(String loginId, YnCode deleteYn);

}
