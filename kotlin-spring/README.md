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

## MEMO 
### API 호출 제한
- chatGPT API 호출시 분당 3500 회로 호출 제한이 있다. 때문에 아래 두개 라이브러리를 사용하여 호출 제한기능을 테스트 해 보았다.  
### bucket4j
### WebFlux