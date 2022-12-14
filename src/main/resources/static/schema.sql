DROP TABLE IF EXISTS TB_USER;
DROP TABLE IF EXISTS TB_PHONE_AUTH;

DROP INDEX IF EXISTS EMAIL_IDX;
DROP INDEX IF EXISTS ACCESS_TOKEN_IDX;
DROP INDEX IF EXISTS PHONE_INFO_IDX;
DROP INDEX IF EXISTS AUTHENTICATION_IDX;

CREATE TABLE TB_USER(
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    login_id VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    enc_password VARCHAR(100) NOT NULL,
    nickname VARCHAR(40) NOT NULL,
    enc_phone VARCHAR(50) NOT NULL,
    access_token VARCHAR(300) NULL,
    access_expired_at DATETIME NULL ,
    delete_yn VARCHAR(1) NOT NULL DEFAULT 'N',
    created_at DATETIME NOT NULL  DEFAULT SYSTIMESTAMP,
    updated_at DATETIME NOT NULL  DEFAULT SYSTIMESTAMP
);

CREATE INDEX EMAIL_IDX ON TB_USER(login_id);
CREATE INDEX ACCESS_TOKEN_IDX ON TB_USER(access_token);

CREATE TABLE TB_PHONE_AUTH(
    auth_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NULL,
    enc_phone VARCHAR(50) NOT NULL,
    telecom_code ENUM('SKT','KT','LGU','SAVE_SKT','SAVE_KT','SAVE_LGU'),
    auth_value VARCHAR(20) NOT NULL,
    authentication VARCHAR(200) NULL,
    AUTHORIZED_YN CHAR(1) NOT NULL DEFAULT 'N',
    auth_type ENUM('SIGN_IN', 'PASSWORD_RESET'),
    auth_until DATETIME NOT NULL DEFAULT SYSTIMESTAMP,
    guarantee_until DATETIME NULL,
    created_at DATETIME NOT NULL  DEFAULT SYSTIMESTAMP,
    updated_at DATETIME NOT NULL  DEFAULT SYSTIMESTAMP,
    CONSTRAINT PHONE_AUTH_REFERENCE FOREIGN KEY (user_id) references (user_id)
);

CREATE INDEX PHONE_INFO_IDX ON TB_PHONE_AUTH(enc_phone, telecom_code, auth_type);
CREATE INDEX AUTHENTICATION_IDX ON TB_PHONE_AUTH(auth_type, authentication);
