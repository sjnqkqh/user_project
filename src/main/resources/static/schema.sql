DROP TABLE IF EXISTS TB_USER_TOKEN;
DROP TABLE IF EXISTS TB_PHONE_AUTH;
DROP TABLE IF EXISTS TB_USER;

DROP INDEX IF EXISTS EMAIL_IDX;
DROP INDEX IF EXISTS ACCESS_TOKEN_IDX;
DROP INDEX IF EXISTS PHONE_INFO_IDX;
DROP INDEX IF EXISTS AUTHENTICATION_IDX;

/*======================================================================================================================================*/

CREATE TABLE TB_USER
(
    user_id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    login_id     VARCHAR(100) NOT NULL,
    email        VARCHAR(100) NOT NULL,
    enc_password VARCHAR(100) NOT NULL,
    nickname     VARCHAR(40)  NOT NULL,
    enc_phone    VARCHAR(50)  NOT NULL,
    delete_yn    VARCHAR(1)   NOT NULL DEFAULT 'N',
    created_at   DATETIME     NOT NULL DEFAULT SYSTIMESTAMP,
    updated_at   DATETIME     NOT NULL DEFAULT SYSTIMESTAMP
);

CREATE INDEX EMAIL_IDX ON TB_USER (login_id);

/*======================================================================================================================================*/

CREATE TABLE TB_USER_TOKEN
(
    token_id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id                  BIGINT            NOT NULL REFERENCES TB_USER (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    access_token             VARCHAR(500) UNIQUE NOT NULL,
    refresh_token            VARCHAR(500) UNIQUE NOT NULL,
    refresh_token_expired_at DATETIME          NOT NULL DEFAULT SYSTIMESTAMP,
    created_at               DATETIME          NOT NULL DEFAULT SYSTIMESTAMP,
    updated_at               DATETIME          NOT NULL DEFAULT SYSTIMESTAMP
);

CREATE INDEX ACCESS_TOKEN_IDX on TB_USER_TOKEN (access_token);

/*======================================================================================================================================*/

CREATE TABLE TB_PHONE_AUTH
(
    auth_id         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT       NULL REFERENCES TB_USER (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    enc_phone       VARCHAR(50)  NOT NULL,
    telecom_code    ENUM ('SKT','KT','LGU','SAVE_SKT','SAVE_KT','SAVE_LGU'),
    auth_value      VARCHAR(20)  NOT NULL,
    authentication  VARCHAR(200) NULL,
    AUTHORIZED_YN   CHAR(1)      NOT NULL DEFAULT 'N',
    auth_type       ENUM ('SIGN_IN', 'PASSWORD_RESET'),
    auth_until      DATETIME     NOT NULL DEFAULT SYSTIMESTAMP,
    guarantee_until DATETIME     NULL,
    created_at      DATETIME     NOT NULL DEFAULT SYSTIMESTAMP,
    updated_at      DATETIME     NOT NULL DEFAULT SYSTIMESTAMP
);

CREATE INDEX PHONE_INFO_IDX ON TB_PHONE_AUTH (enc_phone, telecom_code, auth_type);
CREATE INDEX AUTHENTICATION_IDX ON TB_PHONE_AUTH (auth_type, authentication);

/*======================================================================================================================================*/

