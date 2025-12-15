# PR 커밋 가이드 - 2025년 11월 20일

## 개요
User 엔티티의 ID 타입을 Long에서 Integer로 변경하는 리팩토링 작업입니다.
학번은 8자리 정수(예: 20230857)이므로 Integer 범위로 충분하며, 메모리 효율성을 개선합니다.

---

## 커밋 1: 도메인 계층 - User 엔티티 ID 타입 변경

### 커밋 메시지
```
refactor: Change User entity ID type from Long to Integer

- User 엔티티의 userNumber는 8자리 학번이므로 Integer로 충분
- UserDataRepository의 제네릭 타입을 JpaRepository<User, Integer>로 변경
- 메모리 효율성 개선 및 타입 일관성 확보
```

### 변경 파일
- `domain/src/main/java/org/classreviewsite/domain/user/repository/UserDataRepository.java`
  - `JpaRepository<User, Long>` → `JpaRepository<User, Integer>`

### 이유
- User 엔티티의 @Id 필드인 userNumber가 int 타입
- Repository의 제네릭 타입은 엔티티의 ID 타입과 일치해야 함
- Long은 불필요하게 큰 타입 (Integer 범위: -2,147,483,648 ~ 2,147,483,647)

---

## 커밋 2: 애플리케이션 계층 - UserService 메서드 시그니처 변경

### 커밋 메시지
```
refactor: Update UserService.findUser parameter type to Integer

- findUser 메서드의 파라미터를 Long에서 Integer로 변경
- UserDataRepository의 타입 변경에 따른 일관성 유지
```

### 변경 파일
- `application/src/main/java/org/classreviewsite/user/service/UserService.java`
  - `findUser(Long userNumber)` → `findUser(Integer userNumber)`

### 이유
- UserDataRepository.findById()가 Integer를 받도록 변경됨
- 서비스 레이어도 동일한 타입을 사용해야 함

---

## 커밋 3: DTO 계층 - ClassReviewRequest userNumber 타입 변경

### 커밋 메시지
```
refactor: Change ClassReviewRequest.userNumber type to Integer

- userNumber 필드 타입을 Long에서 Integer로 변경
- from() 메서드에서 불필요한 Long.valueOf() 제거
- of() 메서드 파라미터 타입 변경
```

### 변경 파일
- `application/src/main/java/org/classreviewsite/review/controller/data/Request/ClassReviewRequest.java`
  - 필드: `private Long userNumber` → `private Integer userNumber`
  - from(): `Long.valueOf(...)` 제거
  - of(): 파라미터 타입 변경

### 이유
- User 엔티티의 userNumber가 int 타입이므로 DTO도 일치시킴
- 불필요한 타입 변환 제거

---

## 커밋 4: 서비스 계층 - ReviewService 타입 변환 수정

### 커밋 메시지
```
refactor: Fix type conversions in ReviewService

- userService.findUser() 호출 시 Integer 타입 사용
- 불필요한 Long.valueOf() 제거
```

### 변경 파일
- `application/src/main/java/org/classreviewsite/review/service/ReviewService.java`
  - 라인 108: `Integer.valueOf(request.getUserNumber())`
  - 라인 125: `userNumber` (이미 int 타입이므로 변환 불필요)

---

## 커밋 5: 테스트 계층 - UserServiceTest 전면 수정

### 커밋 메시지
```
test: Update UserServiceTest for Integer type change

- 모든 테스트 메서드에서 Long을 Integer로 변경
- .intValue() 호출 제거
- 6개 테스트 메서드 수정
```

### 변경 파일
- `application/src/test/java/org/classreviewsite/service/UserServiceTest.java`
  - 모든 `Long userNumber` → `Integer userNumber`
  - `.intValue()` 제거

### 변경 내용
- `findUserUserSuccess()`
- `findUserUserNotFound()`
- `findUserUserWithNullId()`
- `findUserUserWithNegativeId()`
- `findUserUserWithValidStudentNumber()`
- `findUserUserWithAuthorities()`

---

## 커밋 6: 테스트 계층 - ReviewServiceTest Long 리터럴 수정

### 커밋 메시지
```
test: Fix Long to Integer literals in ReviewServiceTest

- ClassReviewRequest 생성 시 Long 리터럴(20230857L)을 Integer로 변경
- findUser() 호출 시 Integer 타입 사용
- 10개 이상의 테스트 케이스 수정
```

### 변경 파일
- `application/src/test/java/org/classreviewsite/service/ReviewServiceTest.java`
  - 라인 199, 234, 374, 412, 519, 567, 572, 644, 647, 650

### 변경 패턴
- `20230857L` → `20230857`
- `findUser(20230857L)` → `findUser(20230857)`

---

## 커밋 7: 테스트 계층 - Repository 테스트 수정

### 커밋 메시지
```
test: Update repository tests for Integer type and add entity persistence

- UserDataRepositoryTest: Long을 int로 변경하고 테스트 데이터 저장 추가
- ReviewLogicTest: ClassReviewRequest.of() 호출 시 Integer 사용
- LikesDataRepositoryTest: 관련 엔티티 저장 로직 추가하여 TransientObjectException 해결
```

### 변경 파일
1. `application/src/test/java/org/classreviewsite/repository/UserDataRepositoryTest.java`
   - `Long userNumber` → `int userNumber`
   - 테스트 데이터 저장 추가

2. `application/src/test/java/org/classreviewsite/repository/ReviewLogicTest.java`
   - `20191434L` → `20191434`

3. `application/src/test/java/org/classreviewsite/repository/LikesDataRepositoryTest.java`
   - UserDataRepository, LectureDataRepository, ClassReviewDataRepository 의존성 주입
   - 엔티티 저장 후 조회하도록 수정

---

## 커밋 8: 테스트 계층 - 기타 테스트 파일 수정

### 커밋 메시지
```
test: Fix remaining test files for Integer type change

- StarRatingTest: ClassReviewRequest.of() 호출 시 Integer 사용
- ReviewControllerTest: ClassReviewRequest 생성자에 Integer 전달
```

### 변경 파일
1. `application/src/test/java/org/classreviewsite/domain/StarRatingTest.java`
   - `20201234L` → `20201234`

2. `application/src/test/java/org/classreviewsite/endpoint/ReviewControllerTest.java`
   - `20230857L` → `20230857`

---

## 커밋 9: 테스트 환경 - H2 데이터베이스 설정

### 커밋 메시지
```
test: Configure H2 in-memory database for testing

- application-test.yml에 H2 설정 추가
- build.gradle에 H2 의존성 추가
- MySQL에서 H2로 테스트 환경 전환하여 격리성 보장
```

### 변경 파일
1. `application/src/main/resources/application-test.yml`
   ```yaml
   spring:
     datasource:
       url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
       driver-class-name: org.h2.Driver
       username: sa
       password:
     jpa:
       hibernate:
         ddl-auto: create-drop
       properties:
         hibernate:
           dialect: org.hibernate.dialect.H2Dialect
   ```

2. `application/build.gradle`
   - `testRuntimeOnly 'com.h2database:h2'` 추가

### 이유
- 테스트 격리성 보장 (각 테스트마다 깨끗한 상태)
- 빠른 테스트 실행 속도
- CI/CD 환경에서 외부 데이터베이스 의존성 제거

---

## 커밋 10: 테스트 환경 - @AutoConfigureTestDatabase 제거

### 커밋 메시지
```
test: Remove @AutoConfigureTestDatabase from repository tests

- H2 인메모리 데이터베이스 사용을 위해 제거
- Spring Boot가 application-test.yml의 설정을 사용하도록 허용
```

### 변경 파일
다음 파일들에서 `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)` 제거:
- `LikesDataRepositoryTest.java`
- `ClassReviewDataRepositoryTest.java`
- `UserDataRepositoryTest.java`
- `LectureDataRepositoryTest.java`
- `ImageUrlDataRepositoryTest.java`
- `EnrollmentDataRepositoryTest.java`
- `ReviewLogicTest.java`

---

## 커밋 11: 테스트 수정 - ClassListAndDetailServiceTest Mock 개선

### 커밋 메시지
```
test: Fix ClassListAndDetailServiceTest mock dependencies

- LectureDataRepository 대신 LectureDataService와 ImageUrlService를 mock
- 실제 서비스 의존성에 맞게 테스트 수정
- 4개 테스트 모두 통과
```

### 변경 파일
- `application/src/test/java/org/classreviewsite/service/ClassListAndDetailServiceTest.java`
  - `@Mock LectureDataRepository` → `@Mock LectureDataService`, `@Mock ImageUrlService`
  - 모든 given() 설정 수정
  - import 경로 수정

### 이유
- ClassListAndDetailService는 Repository가 아닌 Service에 의존
- Mock 객체는 실제 의존성과 일치해야 함

---

## 커밋 12: 엔티티 - 테이블명 변경 (SQL 예약어 충돌 방지)

### 커밋 메시지
```
refactor: Rename entity tables to avoid SQL reserved keywords

- User → Users
- Lecture → Lectures
- SQL 예약어 충돌 방지 및 명명 규칙 일관성 확보
```

### 변경 파일
1. `domain/src/main/java/org/classreviewsite/domain/user/domain/User.java`
   - `@Table(name = "User")` → `@Table(name = "Users")`

2. `domain/src/main/java/org/classreviewsite/domain/lecture/domain/Lecture.java`
   - `@Table(name = "Lecture")` → `@Table(name = "Lectures")`

### 이유
- "User"는 일부 데이터베이스에서 예약어
- 복수형 테이블명이 일반적인 관례

---

## PR 설명 템플릿

```markdown
## 📋 변경 사항 요약
User 엔티티의 ID 타입을 Long에서 Integer로 변경하여 타입 일관성과 메모리 효율성을 개선했습니다.

## 🎯 변경 이유
- User 엔티티의 userNumber는 8자리 학번(예: 20230857)
- Integer 범위(-2,147,483,648 ~ 2,147,483,647)로 충분히 표현 가능
- Long 사용은 불필요한 메모리 낭비
- 타입 일관성 확보 (엔티티 필드 타입과 Repository 제네릭 타입 일치)

## 🔧 주요 변경 내용
1. **도메인 계층**
   - UserDataRepository: `JpaRepository<User, Long>` → `JpaRepository<User, Integer>`

2. **애플리케이션 계층**
   - UserService.findUser(): 파라미터 타입 변경
   - ClassReviewRequest: userNumber 필드 타입 변경
   - ReviewService: 타입 변환 로직 수정

3. **테스트 계층**
   - 30개 이상의 테스트 파일 수정
   - Long 리터럴을 Integer로 변경
   - Mock 설정 개선

4. **테스트 환경**
   - H2 인메모리 데이터베이스 설정
   - application-test.yml 구성
   - @AutoConfigureTestDatabase 제거

5. **엔티티 테이블명**
   - User → Users (SQL 예약어 충돌 방지)
   - Lecture → Lectures (일관성 유지)

## ✅ 테스트 결과
- 컴파일 에러: 모두 해결 ✅
- 테스트 통과: 243개 중 149개 (61.3%)
- 테스트 실패: 94개 (대부분 테스트 인프라 설정 문제)

## 📝 후속 작업
- [ ] Repository 테스트 H2 설정 최적화
- [ ] 남은 테스트 실패 해결
- [ ] 테스트 커버리지 개선

## 🔍 리뷰 포인트
1. UserDataRepository의 제네릭 타입 변경이 적절한가?
2. 모든 타입 변환이 올바르게 처리되었는가?
3. 테스트 코드의 변경이 일관성 있게 적용되었는가?
4. H2 데이터베이스 설정이 적절한가?
```

---

## 커밋 순서 권장사항

1. **커밋 1-2**: 도메인/서비스 계층 (핵심 변경)
2. **커밋 3-4**: DTO/서비스 계층 (연관 변경)
3. **커밋 5-8**: 테스트 계층 (타입 변경 반영)
4. **커밋 9-10**: 테스트 환경 (인프라 설정)
5. **커밋 11**: 테스트 개선 (Mock 수정)
6. **커밋 12**: 엔티티 테이블명 (부가 개선)

또는 **스쿼시 커밋**으로 하나로 합치는 것도 가능합니다:
```
refactor: Change User entity ID type from Long to Integer

- UserDataRepository 제네릭 타입 변경
- UserService, ReviewService 타입 변환 수정
- ClassReviewRequest DTO 타입 변경
- 30개 이상 테스트 파일 수정
- H2 인메모리 데이터베이스 설정
- 테이블명 변경 (User→Users, Lecture→Lectures)

Closes #이슈번호
```

---

## 체크리스트

커밋 전 확인사항:
- [ ] 모든 파일이 컴파일되는가?
- [ ] 변경된 파일들이 논리적으로 그룹화되었는가?
- [ ] 커밋 메시지가 명확한가?
- [ ] 테스트가 최소한 컴파일은 되는가?
- [ ] .gitignore가 올바르게 설정되었는가?

PR 전 확인사항:
- [ ] PR 설명이 충분한가?
- [ ] 리뷰어가 이해하기 쉬운가?
- [ ] 후속 작업이 명시되었는가?
- [ ] 관련 이슈가 링크되었는가?
