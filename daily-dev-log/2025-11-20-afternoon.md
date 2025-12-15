# 개발 일지 - 2025년 11월 20일 (오후)

## 작업 목표
- User 엔티티 ID 타입 변경 (Long → Integer)으로 인한 컴파일 에러 해결
- 102개 테스트 실패 해결하여 clean build 달성
- TDD 워크플로우 준비

## 주요 작업 내용

### 1. User 엔티티 ID 타입 변경 관련 수정

#### 1.1 도메인 계층 수정
- **UserDataRepository 제네릭 타입 변경**
  - 파일: `domain/src/main/java/org/classreviewsite/domain/user/repository/UserDataRepository.java`
  - 변경: `JpaRepository<User, Long>` → `JpaRepository<User, Integer>`
  - 이유: User 엔티티의 @Id 필드인 userNumber가 int 타입이므로 Repository도 Integer를 사용해야 함

#### 1.2 애플리케이션 계층 수정
- **UserService.findUser() 메서드 시그니처 변경**
  - 파일: `application/src/main/java/org/classreviewsite/user/service/UserService.java`
  - 변경: `findUser(Long userNumber)` → `findUser(Integer userNumber)`
  
- **ReviewService 수정**
  - 파일: `application/src/main/java/org/classreviewsite/review/service/ReviewService.java`
  - 라인 108: `Long.valueOf(request.getUserNumber())` → `Integer.valueOf(request.getUserNumber())`
  - 라인 125: `Long.valueOf(userNumber)` → `userNumber` (이미 int 타입)

- **ClassReviewRequest DTO 수정**
  - 파일: `application/src/main/java/org/classreviewsite/review/controller/data/Request/ClassReviewRequest.java`
  - 필드 타입 변경: `private Long userNumber` → `private Integer userNumber`
  - from() 메서드: `Long.valueOf()` 제거
  - of() 메서드 파라미터: `Long userNumber` → `Integer userNumber`

### 2. 테스트 코드 수정

#### 2.1 Repository 테스트
- **LikesDataRepositoryTest**
  - UserDataRepository, LectureDataRepository, ClassReviewDataRepository 의존성 주입 추가
  - 테스트 메서드에서 엔티티를 먼저 저장한 후 조회하도록 수정
  - TransientObjectException 해결

- **UserDataRepositoryTest**
  - `Long userNumber` → `int userNumber`
  - 테스트 데이터를 먼저 저장하도록 수정

- **ReviewLogicTest**
  - ClassReviewRequest.of() 호출 시 `20191434L` → `20191434`

- **모든 Repository 테스트에서 @AutoConfigureTestDatabase 제거**
  - LikesDataRepositoryTest
  - ClassReviewDataRepositoryTest
  - UserDataRepositoryTest
  - LectureDataRepositoryTest
  - ImageUrlDataRepositoryTest
  - EnrollmentDataRepositoryTest
  - ReviewLogicTest

#### 2.2 Service 테스트
- **UserServiceTest 전면 수정**
  - 모든 테스트 메서드에서 `Long userNumber` → `Integer userNumber`
  - `.intValue()` 호출 제거
  - 총 6개 테스트 메서드 수정

- **ReviewServiceTest**
  - ClassReviewRequest 생성자 호출 시 모든 Long 리터럴을 Integer로 변경
  - 수정된 라인: 199, 234, 567, 572, 644, 647, 650
  - findUser() 호출 시 `20230857L` → `20230857`

- **StarRatingTest**
  - ClassReviewRequest.of() 호출 시 `20201234L` → `20201234`

#### 2.3 Controller 테스트
- **ReviewControllerTest**
  - ClassReviewRequest 생성자: `20230857L` → `20230857`

### 3. 테스트 환경 설정 변경

#### 3.1 application-test.yml 수정
- MySQL에서 H2 인메모리 데이터베이스로 변경
  ```yaml
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
- `defer-datasource-initialization` 제거
- naming strategy 설정 제거

### 4. 엔티티 테이블명 변경
- **User 엔티티**
  - `@Table(name = "User")` → `@Table(name = "Users")`
  - SQL 예약어 충돌 방지

- **Lecture 엔티티**
  - `@Table(name = "Lecture")` → `@Table(name = "Lectures")`
  - 일관성 유지 및 잠재적 충돌 방지

## 현재 상태

### ✅ 완료된 작업
1. User 엔티티 ID 타입 변경에 따른 모든 컴파일 에러 해결
2. 테스트 환경을 H2 인메모리 데이터베이스로 전환
3. 총 30개 이상의 파일 수정 완료

### ⚠️ 남은 문제
- **102개 테스트 실패** (243개 중 141개 통과)
  - 주요 실패 원인:
    1. NullPointerException (Service 테스트의 mock 설정 불완전)
    2. AssertionError (예상 값과 실제 값 불일치)
    3. IllegalStateException (H2 데이터베이스 컨텍스트 로딩 실패)

### 🔍 실패 테스트 분류
1. **ClassListAndDetailServiceTest**: 4개 실패 (NullPointerException)
2. **EnrollmentDataServiceTest**: 3개 실패 (AssertionError)
3. **LikeDataServiceTest**: 1개 실패 (AssertionError)
4. **ReviewServiceTest**: 3개 실패 (NullPointerException, AssertionFailedError)
5. **Repository 테스트들**: 다수 실패 (H2 설정 문제)

## 다음 단계

### 우선순위 1: Repository 테스트 안정화
- H2 데이터베이스 스키마 초기화 문제 해결
- 엔티티 간 관계 설정 검증

### 우선순위 2: Service 테스트 Mock 설정 보완
- ClassListAndDetailService의 mock 반환값 설정
- ReviewService의 의존성 mock 설정 완성

### 우선순위 3: TDD 워크플로우 시작
- 모든 테스트 통과 후 `.custom-commands/check-tests.md` 실행
- `plan.md`에 따라 다음 기능 개발 시작

## 기술적 의사결정

### User ID 타입을 Integer로 유지하는 이유
- 학번은 일반적으로 8자리 정수 (예: 20230857)
- Integer 범위 (-2,147,483,648 ~ 2,147,483,647)로 충분히 표현 가능
- Long을 사용할 필요가 없어 메모리 효율성 향상

### H2 데이터베이스 선택 이유
- 테스트 격리성 보장 (각 테스트마다 깨끗한 상태)
- 빠른 테스트 실행 속도
- CI/CD 환경에서 외부 데이터베이스 의존성 제거

## 학습 내용
- JPA Repository의 제네릭 타입은 엔티티의 @Id 필드 타입과 일치해야 함
- Spring Boot 테스트에서 @AutoConfigureTestDatabase(replace = NONE)은 실제 데이터베이스를 사용하도록 강제함
- H2의 create-drop 모드는 각 테스트 실행 후 스키마를 자동으로 정리함

## 작업 시간
- 시작: 14:54
- 종료: 15:02
- 소요 시간: 약 8분 (집중 작업)
