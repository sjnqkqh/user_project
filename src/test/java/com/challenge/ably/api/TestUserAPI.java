package com.challenge.ably.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challenge.ably.config.AuthenticationInterceptor;
import com.challenge.ably.config.EnableMockMvcUTF8;
import com.challenge.ably.config.RestDocConfiguration;
import com.challenge.ably.domain.User;
import com.challenge.ably.dto.user.UserTokenDto;
import com.challenge.ably.dto.user.req.LoginReqDto;
import com.challenge.ably.dto.user.req.PasswordResetReqDto;
import com.challenge.ably.dto.user.req.UserCreateReqDto;
import com.challenge.ably.dto.user.resp.UserInfoRespDto;
import com.challenge.ably.service.AuthService;
import com.challenge.ably.service.UserService;
import com.challenge.ably.service.UserTokenService;
import com.challenge.ably.util.code.TelecomCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureRestDocs
@Import(RestDocConfiguration.class)
@Transactional
@EnableMockMvcUTF8
@WebAppConfiguration
@SpringBootTest
public class TestUserAPI {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserTokenService userTokenService;

    @MockBean
    private AuthenticationInterceptor authenticationInterceptor;

    /**
     * ID 중복확인 테스트
     */
    @Test
    void isDuplicateIdTest() throws Exception {
        /* Given */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        /* When */
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/user/check-duplicate")
            .headers(headers)
            .param("loginId", "Hiring_Test"));

        /* Then */
        result.andExpect(status().isOk())
            .andDo(print())
            .andDo(document("isDuplicateIdTest",
                requestParameters(
                    parameterWithName("loginId").description("회원 로그인 ID")
                ),
                responseFields(
                    fieldWithPath("isDuplicateId").type(JsonFieldType.BOOLEAN).description("로그인 ID 중복 여부")
                )
            ));
    }

    /**
     * 회원가입 테스트
     */
    @Test
    void signInTest() throws Exception {
        /* Given */
        UserCreateReqDto reqDto = new UserCreateReqDto("HIRING_TEST", "test@hiring.co.kr", "password1", "hiring", "01012341234", TelecomCode.LGU, "UUID");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        willReturn(1L).given(authService).validatePhoneAuth(any(), any(), any(), any());
        willDoNothing().given(authService).deletePhoneAuthHistory(any());

        /* When */
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/user")
            .headers(headers)
            .content(objectMapper.writeValueAsString(reqDto)));

        /* Then */
        result.andExpect(status().isOk())
            .andExpect(jsonPath("result", equalTo(true)))
            .andDo(print())
            .andDo(document("signInTest",
                requestFields(
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원 로그인 ID"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 비밀번호"),
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대전화 번호"),
                    fieldWithPath("telecomCode").type(JsonFieldType.STRING).description("통신사 코드 (통신사 코드표 참조)"),
                    fieldWithPath("authentication").type(JsonFieldType.STRING).description("휴대전화 인증 결과값")
                ),
                responseFields(
                    fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("회원가입 완료 여부")
                )
            ));
    }

    /**
     * 로그인 테스트
     */
    @Test
    void loginInTest() throws Exception {
        /* Given */
        LoginReqDto reqDto = new LoginReqDto("HIRING_TEST", "password1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        willReturn(0L).given(userService).login(reqDto);
        willReturn(new UserTokenDto(new User(), "access_token", "refresh_token", LocalDateTime.now())).given(userService).giveLoginAuthToken(any());
        willDoNothing().given(userTokenService).saveUserToken(any());

        /* When */
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/user/login")
            .headers(headers)
            .content(objectMapper.writeValueAsString(reqDto)));

        /* Then */
        result.andExpect(status().isOk())
            .andDo(print())
            .andDo(document("loginInTest",
                requestFields(
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원 로그인 ID"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 비밀번호")
                ),
                responseFields(
                    fieldWithPath("accessToken").type(JsonFieldType.STRING).description("회원 액세스 토큰 문자열")
                )
            ));
    }

    /**
     * 회원정보 확인 테스트
     */
    @Test
    void getUserInformationTest() throws Exception {
        /* Given */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("AccessToken");

        willReturn(true).given(authenticationInterceptor).preHandle(any(), any(), any());
        willReturn(new UserInfoRespDto("HIRING_TEST", "test@hiring.co.kr", "hiring", "01012341234")).given(userService).searchUserInfo(any());

        /* When */
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/user/info")
            .requestAttr("id", 0L)
            .headers(headers));

        /* Then */
        result.andExpect(status().isOk())
            .andDo(print())
            .andDo(document("getUserInformationTest",
                requestHeaders(headerWithName("Authorization").description("로그인 시 반환된 Access token")),
                responseFields(
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원 로그인 ID"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대전화 번호")
                )
            ));
    }

    /**
     * 비밀번호 찾기 테스트
     */
    @Test
    void resetPasswordTest() throws Exception {
        /* Given */
        PasswordResetReqDto reqDto = new PasswordResetReqDto("HIRING_TEST", "test@hiring.co.kr", "password1", "01012341234", TelecomCode.LGU, "UUID");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        willReturn(1L).given(authService).validatePhoneAuth(any(), any(), any(), any());
        willDoNothing().given(authService).deletePhoneAuthHistory(any());

        /* When */
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/user/password")
            .headers(headers)
            .content(objectMapper.writeValueAsString(reqDto)));

        /* Then */
        result.andExpect(status().isOk())
            .andExpect(jsonPath("result", equalTo(true)))
            .andDo(print())
            .andDo(document("resetPasswordTest",
                requestFields(
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원 로그인 ID"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 비밀번호"),
                    fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대전화 번호"),
                    fieldWithPath("telecomCode").type(JsonFieldType.STRING).description("통신사 코드 (통신사 코드표 참조)"),
                    fieldWithPath("authentication").type(JsonFieldType.STRING).description("휴대전화 인증 결과값")
                ),
                responseFields(
                    fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("비밀번호 변경 완료 여부")
                )
            ));
    }

}
