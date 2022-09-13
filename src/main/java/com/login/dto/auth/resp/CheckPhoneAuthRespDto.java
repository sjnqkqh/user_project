package com.login.dto.auth.resp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckPhoneAuthRespDto {

    private Boolean isSuccess;

    private String authentication;

    @Builder
    public CheckPhoneAuthRespDto(boolean isSuccess, String authentication){
        this.isSuccess = isSuccess;
        this.authentication = authentication;
    }

}
