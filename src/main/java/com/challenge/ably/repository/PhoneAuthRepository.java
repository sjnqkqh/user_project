package com.challenge.ably.repository;

import com.challenge.ably.domain.PhoneAuth;
import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.TelecomCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {

    Optional<PhoneAuth> findFirstByAuthTypeCodeAndEncPhoneAndTelecomCode(AuthTypeCode authTypeCode, String encPhone, TelecomCode telecomCode);

    Optional<PhoneAuth> findFirstByAuthenticationAndAuthTypeCode(String authentication, AuthTypeCode authTypeCode);
}
