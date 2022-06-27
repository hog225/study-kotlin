# Kotlin Spring 

1. domain 

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


2. 기능
- User 생성, 삭제, 수정, 조회
- Department 로 User 초대, 탈퇴
- User 권한 부여 (Department 기본 권한 자동 부여)
- Department 에 속한 User 조회
- 권한에 의한 Contents 접근 제한 