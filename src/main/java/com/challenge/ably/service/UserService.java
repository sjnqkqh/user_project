package com.challenge.ably.service;

import com.challenge.ably.domain.User;
import com.challenge.ably.dto.user.req.UserCreateReqDto;
import com.challenge.ably.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 생성 DTO 유효성 검사
     *
     * @param reqDto 회원 생성 DTO
     */
    @Transactional(readOnly = true)
    public void validateCreateUser(UserCreateReqDto reqDto) {

    }

    /**
     * 회원 생성
     *
     * @param reqDto 회원 생성 DTO
     */
    @Transactional
    public Long createUser(UserCreateReqDto reqDto) {

        log.info("[UserService.createUser] Do something");
        User user = new User(reqDto);
        return user.getUserId();
    }

}
