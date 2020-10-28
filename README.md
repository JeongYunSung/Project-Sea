# Project Sea

서비스 목록

- Notification Service : 특정 이벤트들에 대해 발생한 정보를 모아두는 서비스
- Member Service : 회원의 정보 저장 및 인증에 대한 서비스
- Project Service : 주요 서비스로서 프로젝트(학술제, 멘토멘티, 협업 등)을 하고자 하는사람들끼리 연결해주는 서비스
- Team Service: 위 Project Service에서 구성원들을 묶어서 저장하는 서비스
- WeClass Service : 프로젝트가 시작할경우 구성원들끼리 파일 공유 및 공지관련 등의 커뮤니티 서비스
- QnA Service : 프로젝트가 시작하기전에 궁금한점을 물어보는 서비스
- Board Service : 공지사항, 자유게시판, 학과게시판등의 게시판 서비스이다
- Comment Service : 게시판에 댓글을 담당하는 서비스이다
- API Server : 여러 서비스들의 창구역할을 하는 GraphQL기반의 API Server이다

현재 상황

- Notification Service : 특정 이벤트에 대해 알림을 발생 및 읽기, 이메일 전송 구현 어플에 알림푸쉬해주는 기능 미구현
- Member Service : stateless와 MSA환경을 위해 OAuth2를 이용한 가입 및 로그인 구현 ( password, refresh_token 방식 ) 이메일보내기, 프로필 수정, 확인 추가 예정
- Project Service : 프로젝트 생성(사가), 프로젝트 시작(사가), 프로젝트 거절, 프로젝트 취소(사가), 프로젝트 변경(사가)
- Team Service : 팀 생성, 입장, 투표, 탈퇴, 조회 등 구현
- WeClass Service : 위클래스 생성, 보고서 작성, 확인, 수정, 삭제 구현
- Board Service : Resource서버, 이미지 업로드, 게시판 생성, 확인, 수정, 추천, 삭제, 검색 구현
- Comment Service : 구현 중
- QnA Service : 설계단계
- API Server : 구현 중

사용한 기술

- Spring Boot
- JPA & MariaDB
- EventuateTram & Kafka
- GraphQL
- NodeJS & Express

도입예정 기술

- Redis & CQRS
- Docker & Kubernets
