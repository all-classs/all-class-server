all-class 서버 구조도
---

<img width="1358" height="834" alt="image" src="https://github.com/user-attachments/assets/b6969111-9ccb-44b1-bea4-b638a04b5354" />

프로젝트 목표
---
- 학부연구실 창업동아리에서 시작하여, 교내학과 50여명이 직접 수강후기를 작성하여 공유하였습니다. 예비창업을 준비하기 위해 개발한 수업리뷰를 관리하는 시스템입니다.
- 부산지역 14개의 대학의 강의 정보, 수강후기 정보를 제공하는 서비스를 구현해 내는 것이 목표입니다.
- 단순한 기능 구현뿐 아니라, test-driven에 초점을 두어 작업자 외에 인원이 보았을때 읽기 좋은 간결한 코드를 만들어 내는 것이 목표입니다.
- 객체지향 원리와 스프링 프레임워크에서 제공하는 구성요소들을 활용하여 코드를 작성하는 것이 목표입니다.
- 문서화, 단위테스트는 높은 우선순위를 두어 작성하였고, CI/CD를 통한 자동화를 구현하여 쉽게 배포를 관리할 수 있도록 프로젝트를 만들었습니다.

기술적 issue 해결 과정
---

- [#6] Open AI API IO 에러 디버깅 과정 <br/>
  https://johnsnote.vercel.app/posts/vilification-shooting
    
- [#5] 스프링 배치를 통해 엑셀데이터를 주입하기 <br/>
  https://johnsnote.vercel.app/posts/spring-batch-trouble
  
- [#4] 실제 데이터구조에 맞춰 데이터 구조를 변경하는 과정 <br/>
  https://johnsnote.vercel.app/posts/2025-07-05
  
- [#3] 도메인 클래스 중복 사용의 일관성을 어떻게 유지할까? <br/>
  https://johnsnote.vercel.app/posts/2025-07-04
  
- [#2] 간결한 Service 로직 만들기 <br/>
https://johnsnote.vercel.app/posts/service-logic-simple

- [#1] 전체 후기 조회시 N+1로 응답하는 쿼리 해결하기 <br/>
https://johnsnote.vercel.app/posts/nplusone


DB ERD
---

<img width="1314" height="951" alt="image" src="https://raw.githubusercontent.com/all-classs/all-class-server/refs/heads/main/images/erd.png" />

Use Case
---

https://github.com/all-classs/class-review/wiki/Use-Case

Front
---

https://github.com/all-classs/All-Class
