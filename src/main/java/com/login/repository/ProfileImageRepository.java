package com.login.repository;

import com.login.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}

