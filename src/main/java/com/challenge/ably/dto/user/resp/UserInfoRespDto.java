package com.challenge.ably.dto.user.resp;

import com.challenge.ably.domain.User;
import com.challenge.ably.util.EncryptUtil;
import lombok.Getter;

@Getter
public class UserInfoRespDto {

    private String loginId;

    private String email;

    private String nickname;

    private String phone;


    public UserInfoRespDto(User user) throws Exception {
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phone = EncryptUtil.decryptAES256(user.getEncryptedPhone());
    }

}
