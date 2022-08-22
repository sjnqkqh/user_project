package com.challenge.ably.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.challenge.ably.config.EnableMockMvcUTF8;
import com.challenge.ably.config.RestDocConfiguration;
import com.challenge.ably.domain.User;
import com.challenge.ably.dto.auth.req.CheckPhoneAuthReqDto;
import com.challenge.ably.dto.auth.req.PasswordResetPhoneAuthReqDto;
import com.challenge.ably.dto.auth.req.SignInPhoneAuthReqDto;
import com.challenge.ably.dto.auth.resp.CheckPhoneAuthRespDto;
import com.challenge.ably.service.AuthService;
import com.challenge.ably.service.UserService;
import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.TelecomCode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class TestAuthenticationAPI {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    /**
     * 휴대폰 인증 요청 [회원가입] 테스트
     */
    @Test
    void testPhoneAuthenticationSignIn() throws Exception {

        /* Given */
        SignInPhoneAuthReqDto reqDto = new SignInPhoneAuthReqDto(TelecomCode.LGU, "01012341234", AuthTypeCode.SIGN_IN);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        /* When */
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/phone/sign-in")
            .headers(headers)
            .content(objectMapper.writeValueAsString(reqDto)));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("result", equalTo(true)))
            .andDo(print())
            .andDo(document("testPhoneAuthenticationSignIn",
                requestFields(
                    fieldWithPath("telecomCode").type(JsonFieldType.STRING).description("통신사 코드 (통신사 코드표 참조)"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화 번호 (숫자만 11~12자리)"),
                    fieldWithPath("authTypeCode").type(JsonFieldType.STRING).description("휴대전화 인증 유형 (인증 유형 코드표 참조)")
                ),
                responseFields(
                    fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("인증 요청 완료 여부")
                )
            ));
    }

    /**
     * 휴대폰 인증 요청 [비밀번호 찾기] 테스트
     */
    @Test
    void testPhoneAuthenticationPasswordReset() throws Exception {

        /* Given */
        PasswordResetPhoneAuthReqDto reqDto
            = new PasswordResetPhoneAuthReqDto("HIRING_TEST", "test@hiring.co.kr", TelecomCode.LGU, "01012341234", AuthTypeCode.PASSWORD_RESET);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        willReturn(new User()).given(userService).searchUserByLoginIdAndEmail(any(), any());
        willDoNothing().given(authService).passwordResetPhoneAuthentication(any(), any());
        /* When */
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/phone/password-reset")
            .headers(headers)
            .content(objectMapper.writeValueAsString(reqDto)));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("result", equalTo(true)))
            .andDo(print())
            .andDo(document("testPhoneAuthenticationPasswordReset",
                requestFields(
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원 로그인 ID"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                    fieldWithPath("telecomCode").type(JsonFieldType.STRING).description("통신사 코드 (통신사 코드표 참조)"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화 번호 (숫자만 11~12자리)"),
                    fieldWithPath("authTypeCode").type(JsonFieldType.STRING).description("휴대전화 인증 유형 (인증 유형 코드표 참조)")
                ),
                responseFields(
                    fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("인증 요청 완료 여부")
                )
            ));
    }

    /**
     * 휴대폰 인증 확인
     */
    @Test
    void testCheckPhoneAuthentication() throws Exception {

        /* Given */
        CheckPhoneAuthReqDto reqDto = new CheckPhoneAuthReqDto(TelecomCode.LGU, "01012341234", AuthTypeCode.SIGN_IN, "authValue");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        willReturn(CheckPhoneAuthRespDto.builder().isSuccess(true).authentication("UUID").build()).given(authService).phoneAuthentication(any());

        /* When */
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/auth/phone")
            .headers(headers)
            .content(objectMapper.writeValueAsString(reqDto)));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("isSuccess", equalTo(true)))
            .andExpect(jsonPath("authentication", notNullValue()))
            .andDo(print())
            .andDo(document("testCheckPhoneAuthentication",
                requestFields(
                    fieldWithPath("telecomCode").type(JsonFieldType.STRING).description("통신사 코드 (통신사 코드표 참조)"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대전화 번호 (숫자만 11~12자리)"),
                    fieldWithPath("authTypeCode").type(JsonFieldType.STRING).description("휴대전화 인증 유형 (인증 유형 코드표 참조)"),
                    fieldWithPath("authValue").type(JsonFieldType.STRING).description("휴대전화 인증 번호 (휴대전화 번호 뒤 6자리)")
                ),
                responseFields(
                    fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("인증 성공 여부"),
                    fieldWithPath("authentication").type(JsonFieldType.STRING).description("휴대전화 인증 성공 코드 (회원가입, 비밀번호 찾기 API 호출시 사용)")
                )
            ));
    }
}
