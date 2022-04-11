# Project Sea

서비스 목록

- Notification Service : 특정 이벤트들에 대해 발생한 정보를 모아두는 서비스
- Member Service : 회원의 정보 저장 및 인증에 대한 서비스
- Project Service : 주요 서비스로서 프로젝트(학술제, 멘토멘티, 협업 등)을 하고자 하는사람들끼리 연결해주는 서비스
- Team Service: 위 Project Service에서 구성원들을 묶어서 저장하는 서비스
- WeClass Service : 프로젝트가 시작할경우 구성원들끼리 파일 공유 및 공지관련 등의 커뮤니티 서비스
- Board Service : 공지사항, 자유게시판, 학과게시판, 댓글 등의 게시판 서비스
- API Server : 여러 서비스들의 창구역할을 하는 GraphQL기반의 API Server

구현 상황

- Notification Service : 이벤트 발생 시 알림 저장 및 해당 내용 이메일 전송기능 구현
- Member Service : OAuth2를 이용한 인증,인가 구현(password, refresh_token), 이메일 인증 및 프로필 수정 등 구현
- Project Service : 사가패턴을 이용한 프로젝트 생성, 시작, 취소, 수정 구현
- Team Service : 프로젝트 구성원 참가 및 탈퇴 프로젝트 시작 찬반투표 구현
- WeClass Service : 클래스 공지사항, 보고서 작성, 확인 등 구현
- Board Service : 이미지 업로드, 게시판 생성, 확인, 수정, 추천, 삭제, 검색, 댓글 및 대댓글 구현
- API Server : Apollo-Server를 이용한 GraphQL, Node 서버 구축

사용한 기술

- Spring Boot & Scheduler
- JPA & QueryDSL & MariaDB
- Eventuate Tram & Kafka
- GraphQL & GraphQL-Yoga(express) & Typescript
