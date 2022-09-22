package com.login.repository;

import com.login.domain.Profile;
import com.login.domain.ProfileImage;
import com.login.util.code.YnCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
    Optional<ProfileImage> findFirstByProfileAndUseYnOrderByCreatedAtDesc(Profile profile, YnCode useYn);
}

