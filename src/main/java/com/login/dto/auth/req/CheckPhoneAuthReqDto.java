package com.login.dto.auth.req;

import com.login.util.code.AuthTypeCode;
import com.login.util.code.TelecomCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckPhoneAuthReqDto {

    @NotNull
    private TelecomCode telecomCode;

    @NotBlank
    private String phone;

    @NotNull
    private AuthTypeCode authTypeCode;

    @NotBlank
    private String authValue;

}
