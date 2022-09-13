package com.login.repository;

import com.login.domain.User;
import com.login.util.code.YnCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginIdAndDeleteYn(String loginId, YnCode deleteYn);

    Optional<User> findFirstByLoginIdAndEmailAndDeleteYnOrderByCreatedAtDesc(String loginId, String email, YnCode deleteYn);

    Optional<User> findFirstByLoginIdAndDeleteYnOrderByCreatedAtDesc(String loginId, YnCode deleteYn);

}
