package com.challenge.ably.repository;

import com.challenge.ably.domain.PhoneAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {

}
