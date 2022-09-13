package com.login.dto.user.resp;

import com.login.domain.User;
import com.login.util.EncryptUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
