CREATE TABLE temp(
    id BIGINT
);

CREATE TABLE TB_USER(
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    enc_password VARCHAR(100) NOT NULL,
    user_name VARCHAR(40) NOT NULL,
    enc_tel VARCHAR(50) NOT NULL,
    refresh_token VARCHAR(100) NULL,
    created_at DATETIME NOT NULL  DEFAULT SYSTIMESTAMP,
    updated_at DATETIME NOT NULL  DEFAULT SYSTIMESTAMP
);

CREATE INDEX email_idx ON TB_USER(email);