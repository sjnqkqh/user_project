package com.login.repository;

import com.login.domain.PhoneAuth;
import com.login.util.code.AuthTypeCode;
import com.login.util.code.TelecomCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {

    Optional<PhoneAuth> findFirstByAuthTypeCodeAndEncPhoneAndTelecomCodeOrderByUpdatedAtDesc(AuthTypeCode authTypeCode, String encPhone, TelecomCode telecomCode);

    Optional<PhoneAuth> findFirstByAuthenticationAndAuthTypeCode(String authentication, AuthTypeCode authTypeCode);
}
