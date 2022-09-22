package com.login.repository;

import com.login.domain.Profile;
import com.login.domain.User;
import com.login.util.code.YnCode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserAndUseYnOrderByProfileIdDesc(User user, YnCode useYn);
}

