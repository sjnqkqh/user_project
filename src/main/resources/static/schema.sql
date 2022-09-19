DROP TABLE IF EXISTS TB_USER_TOKEN;
DROP TABLE IF EXISTS TB_PHONE_AUTH;
DROP TABLE IF EXISTS TB_BOARD_FILE;
DROP TABLE IF EXISTS TB_BOARD;
DROP TABLE IF EXISTS TB_USER;


/*======================================================================================================================================*/

CREATE TABLE TB_USER
(
    USER_ID      BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    LOGIN_ID     VARCHAR(100) NOT NULL,
    EMAIL        VARCHAR(100) NOT NULL,
    ENC_PASSWORD VARCHAR(100) NOT NULL,
    NICKNAME     VARCHAR(40)  NOT NULL,
    ENC_PHONE    VARCHAR(50)  NOT NULL,
    DELETE_YN    VARCHAR(1)   NOT NULL DEFAULT 'N',
    CREATED_AT   DATETIME     NOT NULL DEFAULT NOW(),
    UPDATED_AT   DATETIME     NOT NULL DEFAULT NOW()
);

CREATE INDEX EMAIL_IDX ON TB_USER (login_id);

/*======================================================================================================================================*/

CREATE TABLE TB_USER_TOKEN
(
    TOKEN_ID                 BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID                  BIGINT              NOT NULL REFERENCES TB_USER (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    ACCESS_TOKEN             VARCHAR(500) UNIQUE NOT NULL,
    REFRESH_TOKEN            VARCHAR(500) UNIQUE NOT NULL,
    REFRESH_TOKEN_EXPIRED_AT DATETIME            NOT NULL DEFAULT NOW(),
    CREATED_AT               DATETIME            NOT NULL DEFAULT NOW(),
    UPDATED_AT               DATETIME            NOT NULL DEFAULT NOW()
);

CREATE INDEX ACCESS_TOKEN_IDX on TB_USER_TOKEN (access_token);

insert into tb_user (user_id, created_at, updated_at, delete_yn, email, enc_password, enc_phone, login_id, nickname)
values (default, default, default, 'N', 'test@hiring.co.kr', '$2a$10$34Exz.d/pt1LHB/30jQazuRYQpuhSpT7ihmiL1SOT3t9APv6dVSMK', 'k8VwutUqQwM0PPiGCtHjVg', NOW(), NOW());

/*======================================================================================================================================*/

CREATE TABLE TB_PHONE_AUTH
(
    AUTH_ID         BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    USER_ID         BIGINT       NULL REFERENCES TB_USER (user_id) ON UPDATE CASCADE ON DELETE CASCADE,
    ENC_PHONE       VARCHAR(50)  NOT NULL,
    TELECOM_CODE    ENUM ('SKT','KT','LGU','SAVE_SKT','SAVE_KT','SAVE_LGU'),
    AUTH_VALUE      VARCHAR(20)  NOT NULL,
    AUTHENTICATION  VARCHAR(200) NULL,
    AUTHORIZED_YN   CHAR(1)      NOT NULL DEFAULT 'N',
    AUTH_TYPE       ENUM ('SIGN_IN', 'PASSWORD_RESET'),
    AUTH_UNTIL      DATETIME     NOT NULL DEFAULT NOW(),
    GUARANTEE_UNTIL DATETIME     NULL,
    CREATED_AT      DATETIME     NOT NULL DEFAULT NOW(),
    UPDATED_AT      DATETIME     NOT NULL DEFAULT NOW()
);

CREATE INDEX PHONE_INFO_IDX ON TB_PHONE_AUTH (enc_phone, telecom_code, auth_type);
CREATE INDEX AUTHENTICATION_IDX ON TB_PHONE_AUTH (auth_type, authentication);

/*======================================================================================================================================*/

CREATE TABLE TB_BOARD
(
    POST_ID    BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    USER_ID    BIGINT UNSIGNED NOT NULL,
    TITLE      VARCHAR(255)    NOT NULL,
    CONTENT    TEXT            NOT NULL,
    CREATED_AT DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (USER_ID) REFERENCES TB_USER (USER_ID) ON UPDATE CASCADE ON DELETE CASCADE
);

/*======================================================================================================================================*/

CREATE TABLE TB_BOARD_FILE
(
    POST_FILE_ID      BIGINT UNSIGNED PRIMARY KEY,
    POST_ID           BIGINT UNSIGNED NOT NULL REFERENCES TB_BOARD (POST_ID) ON UPDATE CASCADE ON DELETE CASCADE,
    ORIGIN_FILE_NAME  VARCHAR(255)    NOT NULL,
    RESTORE_FILE_NAME VARCHAR(255)    NOT NULL,
    CREATED_AT        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
);
