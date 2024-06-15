# Kotlin Spring 

## domain 

- user
  - id
  - name
  - roleIds
  - departmentIds
- department
  - id
  - name : shot, light 
- role
  - id
  - name: external, staff
- contents
  - contents
  - roleId


## 기능
- User 생성, 삭제, 수정, 조회
- Department 로 User 초대, 탈퇴
- User 권한 부여 (Department 기본 권한 자동 부여)
- Department 에 속한 User 조회
- 권한에 의한 Contents 접근 제한 

## test
- api test : python3 -m http.server 8000 또는 python3 simple-server/python3-simple-server.py


## API 호출 제한 (throttling)
- chatGPT API 호출시 분당 3500 회로 호출 제한이 있다. 때문에 아래 두개 라이브러리를 사용하여 호출 제한기능을 테스트 해 보았다. 
- kotlin-spring/src/main/kotlin/org/yg/kotlinspring/throttling 참고 
### bucket4j
### WebFlux

## SSE (Server Sent Event)
- kotlin-spring/src/main/kotlin/org/yg/kotlinspring/sse 참고 
- Server Side Event 를 사용하여 Progress Bar 기능 구현 
- Multi Client 에 대해 데이터를 줄 수 있도록 개발 

## ChatGpt API 

ChatGpt API 사용을 위해선 하기 파일이 필요하다. 
- kotlin-spring/src/main/resources/application-chat-gpt.yml

```yaml
spring:

  h2:
    console:
      path: /h2
      enabled: true
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:mem:finches;MODE=MariaDB;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;NON_KEYWORDS=USER;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: none

chat-gpt:
  token: {token}
  org: {org}
```

## Excel Download
- SXSSF 에서는 XSSFRichTextString 를 지원하지 않는것 같다. 
- XSSF 는 메모리 이슈가 있을 수 있다. 

```bash
curl --location 'http://localhost:9797/study-kotlin/v1/excel?round=4&maxSceneCount=5'
```

- TestExcelMakerTest
  - Test Excel 파일 생성 tmp 하위에 