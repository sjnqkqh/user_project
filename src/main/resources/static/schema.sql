DROP TABLE IF EXISTS TB_USER_TOKEN;
DROP TABLE IF EXISTS TB_PHONE_AUTH;
DROP TABLE IF EXISTS TB_BOARD_FILE;

DROP TABLE IF EXISTS TB_USER_FAVORITE_CATEGORY_MAPPING;
DROP TABLE IF EXISTS TB_POST_CATEGORY_MAPPING;
DROP TABLE IF EXISTS TB_CATEGORY;

DROP TABLE IF EXISTS TB_PROFILE_IMG;
DROP TABLE IF EXISTS TB_USER_RELATION;
DROP TABLE IF EXISTS TB_PROFILE;

DROP TABLE IF EXISTS TB_BOARD;
DROP TABLE IF EXISTS TB_USER;


/*======================================================================================================================================*/

CREATE TABLE TB_USER
(
    USER_ID      BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    LOGIN_ID     VARCHAR(100) NOT NULL,
    EMAIL        VARCHAR(100) NOT NULL,
    ENC_PASSWORD VARCHAR(100) NOT NULL,
    ENC_PHONE    VARCHAR(50)  NOT NULL,
    USE_YN       CHAR(1)      NOT NULL DEFAULT 'Y',
    CREATED_AT   DATETIME     NOT NULL DEFAULT NOW(),
    UPDATED_AT   DATETIME     NOT NULL DEFAULT NOW()
);

CREATE INDEX EMAIL_IDX ON TB_USER (login_id);

CREATE TABLE TB_PROFILE
(
    PROFILE_ID       BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    USER_ID          BIGINT UNSIGNED REFERENCES TB_USER (USER_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    NICKNAME         VARCHAR(40)   NOT NULL,
    PROFILE_IMG_NAME VARCHAR(255),
    INTRODUCE        VARCHAR(3000) NOT NULL DEFAULT '',
    USE_YN           CHAR(1)       NOT NULL DEFAULT 'Y',
    CREATED_AT       DATETIME      NOT NULL DEFAULT NOW(),
    UPDATED_AT       DATETIME      NOT NULL DEFAULT NOW()
);

CREATE TABLE TB_USER_RELATION
(
    PROFILE_OWNER_ID  BIGINT UNSIGNED REFERENCES TB_USER (USER_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    PROFILE_VIEWER_ID BIGINT UNSIGNED REFERENCES TB_USER (USER_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    PROFILE_ID        BIGINT UNSIGNED REFERENCES TB_PROFILE (PROFILE_ID) ON DELETE CASCADE ON UPDATE CASCADE,
    CREATED_AT        DATETIME NOT NULL DEFAULT NOW(),
    UPDATED_AT        DATETIME NOT NULL DEFAULT NOW(),
    PRIMARY KEY (PROFILE_OWNER_ID, PROFILE_VIEWER_ID)
);
CREATE INDEX TB_PROFILE_IDX on TB_USER_RELATION (PROFILE_ID);


CREATE TABLE TB_PROFILE_IMAGE
(
    IMG_ID               BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    PROFILE_ID           BIGINT UNSIGNED NOT NULL REFERENCES TB_PROFILE ON DELETE CASCADE ON UPDATE CASCADE,
    IMG_FILE_NAME        VARCHAR(255)    NOT NULL,
    IMG_FILE_ORIGIN_NAME VARCHAR(255)    NOT NULL,
    USE_YN               CHAR(1)         NOT NULL DEFAULT 'Y',
    CREATED_AT           DATETIME        NOT NULL DEFAULT NOW(),
    UPDATED_AT           DATETIME        NOT NULL DEFAULT NOW()
);

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

insert into tb_user (user_id, created_at, updated_at, use_yn, email, enc_password, enc_phone, login_id)
values (default, default, default, 'N', 'test@hiring.co.kr', '$2a$10$34Exz.d/pt1LHB/30jQazuRYQpuhSpT7ihmiL1SOT3t9APv6dVSMK', NOW(), NOW());

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
    USE_YN     CHAR(1)         NOT NULL DEFAULT 'Y',
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

/*======================================================================================================================================*/

CREATE TABLE TB_CATEGORY
(
    CATEGORY_ID   BIGINT UNSIGNED NOT NULL PRIMARY KEY,
    CATEGORY_NAME VARCHAR(50)     NOT NULL,
    USE_YN        CHAR(1)         NOT NULL DEFAULT 'Y',
    CREATED_AT    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE TB_USER_FAVORITE_CATEGORY_MAPPING
(
    USER_ID     BIGINT UNSIGNED REFERENCES TB_USER (USER_ID),
    CATEGORY_ID BIGINT UNSIGNED REFERENCES TB_CATEGORY (CATEGORY_ID),
    CREATED_AT  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (USER_ID, CATEGORY_ID)
);

CREATE TABLE TB_POST_CATEGORY_MAPPING
(
    POST_ID     BIGINT UNSIGNED NOT NULL REFERENCES TB_USER (USER_ID),
    CATEGORY_ID BIGINT UNSIGNED NOT NULL REFERENCES TB_CATEGORY (CATEGORY_ID),
    CREATED_AT  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (POST_ID, CATEGORY_ID)
);