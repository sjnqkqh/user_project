package com.challenge.ably.repository;

import com.challenge.ably.domain.PhoneAuth;
import com.challenge.ably.util.code.AuthTypeCode;
import com.challenge.ably.util.code.TelecomCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {

    Optional<PhoneAuth> findFirstByAuthTypeCodeAndEncPhoneAndTelecomCodeOrderByUpdatedAtDesc(AuthTypeCode authTypeCode, String encPhone, TelecomCode telecomCode);

    Optional<PhoneAuth> findFirstByAuthenticationAndAuthTypeCode(String authentication, AuthTypeCode authTypeCode);
}
