package com.login.dto.profile.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileReqDto {

    @NotNull
    Long profileId;

    @NotEmpty
    String nickname;

    String introduce;
}
