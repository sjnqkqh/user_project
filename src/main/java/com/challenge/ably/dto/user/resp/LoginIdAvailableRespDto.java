package com.challenge.ably.dto.user.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class LoginIdAvailableRespDto {

    private Boolean isDuplicateId;
}
