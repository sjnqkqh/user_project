# 유저 기능 프로젝트

### 사용 기술

- Java11, Spring Boot(2.6.12), lombok, Junit5
- H2, Spring Data JPA
- JWT, Spring Security, Bcrypt, AES256
- Spring Rest Docs

## 주요 기능 설명

### 휴대전화 인증

- 회원가입, 비밀번호 찾기 시 각각 휴대전화 인증 API가 구현되어 있으며, 각 모두 휴대전화 인증 API를 통해 개별적인 인증을 수행할 수 있습니다.
- 휴대전화 인증 [회원가입] API는 휴대전화 번호 11~12자리와 통신사 코드를 입력받으며, 휴대전화 인증[비밀번호 찾기] API는 휴대전화 번호와 통신사 코드, 아이디와 이메일을 추가로 입력받습니다.
- 인증번호는 사용자가 입력한 휴대전화 번호 뒷자리 6자입니다. (예. 01012123456 → 123456으로 인증)
- 휴대전화 인증 API는 결과 값으로 인증 성공 여부(isSuccess) 와 성공 시 authentication을 반환합니다. 이 중 authentication 값은 회원가입, 비밀번호 변경 API 호출 시 인자값으로 전달되며, 인증까지의 유효기간은 인증을 요청한 직후 3분이내, 인증을 완료한 이후 유효 기간은 30분 이내입니다.

### 회원가입

- 로그인 ID, Email, 비밀번호, 닉네임, 휴대전화 번호, 통신사 코드, 휴대전화 인증 결과값을 인자로 받습니다.
- 회원 가입 휴대전화 인증 API 실행당시 인증을 성공한 휴대전화 정보와 회원가입 시 입력한 정보가 다르다면 예외처리됩니다.
- 성공적으로 회원가입 시, 비밀번호는 Bcrypt 방식으로 해시 처리되어 저장되며, 휴대전화 번호는 AES256방식으로 암호화하여 DB에 저장합니다.

### 로그인 / 회원 정보 조회

- 회원가입이 완료된 계정 정보로 로그인 시, API 결과값으로 JWT AccessToken을 하나 발급합니다.
- 해당 토큰은 현재 유저 정보 조회 API에서만 요구하며, API 요청 시 HTTP Header의 Authorization 필드의 값으로 넣어 Request를 줘야합니다.
- 해당 토큰을 이용해 인터셉터에서 토큰의 유저 정보와 만료 여부를 확인 후, 회원 정보 조회 API 요청을 수행합니다.



### 예외 처리

- 프로젝트 내부 `ApiExceptionAdvice` 파일에서 Exception Handling을 수행합니다.
- 예상할 수 있는 예외 처리에 대해선 `RuntimeException` 클래스를 확장한 `CommonException` 클래스를 이용해 처리합니다.
- 에러 코드와 메시지는 `ApiExceptionCode` 에서 Enum으로 관리합니다.

### DB

- 데이터 베이스는 H2 메모리 데이터 베이스를 내장하여 사용합니다.
- 별도의 설정은 필요치 않으며, 스프링 어플리케이션 구동 시 자동으로 실행됩니다.
- 데이터 베이스 스키마는 resources/static/schema.sql에 선언되어 있습니다.
- Hibernate가 제공하는 DDL 생성 기능은 사용하지 않습니다.

### 문서화

- Spring Rest Docs 라이브러리를 사용하여 API 문서를 작성하였으며, 문서의 위치는 프로젝트 내부 “src/asciidoc/apiDocument.html” 이며, 접근이 어려우신 경우 **[Link](https://drive.google.com/file/d/1vRIBVKNxjIwv9lwyNuY5v04fdoBVG1xt/view?usp=sharing)** 에서 다운로드 할 수 있습니다

### 테스트

- 문서화 및 간단한 수준의 단위 테스트를 위해, MockMVC와 Mockito를 사용하여 각 API별 단위 테스트를 구현하였습니다.

**본 문서는 2022년 8월 23일 기준으로 작성되었습니다.**