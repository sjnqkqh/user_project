package com.login.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.login.service.AuthService;
import com.login.service.UserService;
import com.login.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@EnableMockMvcUTF8
@WebAppConfiguration
@SpringBootTest
public class BeanExtends {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserService userService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected UserTokenService userTokenService;

    @MockBean
    protected AuthenticationInterceptor authenticationInterceptor;
}
