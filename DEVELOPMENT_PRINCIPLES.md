# 개발 원칙 및 가이드라인

## 핵심 철학

### 1. 비즈니스 우선, 이론 후순위
- ❌ **지양**: "클린 아키텍처니까 이렇게 해야 해", "DDD 원칙상 이래야 해"
- ✅ **지향**: "이 비즈니스 요구사항을 해결하려면 이 구조가 적합해"
- **원칙**: 이론은 도구일 뿐, 비즈니스 문제 해결이 목적

### 2. 도메인 그 자체에 집중
- ❌ **지양**: 추상화를 위한 추상화, 패턴을 위한 패턴
- ✅ **지향**: 실제 비즈니스 도메인 로직을 명확하게 표현하는 코드
- **원칙**: 코드는 도메인 전문가가 읽어도 이해할 수 있어야 함

### 3. Spring/JVM 생태계의 실용적 활용
- ❌ **지양**: 프레임워크를 무시한 순수 객체지향 집착
- ✅ **지향**: Spring의 장점(DI, AOP, Transaction 등)을 적극 활용
- **원칙**: 프레임워크를 선택한 이유가 있는 코드 작성

---

## 구체적 개발 가이드라인

### A. 비즈니스 로직 작성 시

#### 1. 명확한 의도 표현
```java
// ❌ 나쁜 예: 기술 용어 중심
public class ReviewAggregateRoot {
    private ReviewValueObject review;
    // 도메인 전문가가 이해하기 어려움
}

// ✅ 좋은 예: 비즈니스 용어 중심
public class Review {
    private String title;
    private String content;
    private Double starRating;
    // 실제 비즈니스 개념을 그대로 표현
}
```

#### 2. 과도한 추상화 지양
```java
// ❌ 나쁜 예: 불필요한 인터페이스
public interface ReviewRepository extends Repository<Review, Long> {}
public class ReviewRepositoryImpl implements ReviewRepository {}
// Spring Data JPA가 이미 제공하는데 굳이?

// ✅ 좋은 예: 필요한 만큼만
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Spring Data JPA의 장점을 그대로 활용
}
```

#### 3. 실용적인 계층 분리
```java
// ❌ 나쁜 예: 과도한 계층
Controller → UseCase → DomainService → Repository → Entity
// 5단계 거쳐서 단순 조회?

// ✅ 좋은 예: 비즈니스 복잡도에 맞춘 계층
Controller → Service → Repository
// 복잡한 로직이면 Service를 세분화 (Writer, Finder 등)
```

---

### B. Spring Framework 활용

#### 1. @Transactional 적극 활용
```java
// ✅ Spring의 선언적 트랜잭션 활용
@Transactional
public void writeReview(ReviewRequest request) {
    // 비즈니스 로직에 집중
    // 트랜잭션 관리는 Spring에게 위임
}

// ✅ 읽기 전용 최적화
@Transactional(readOnly = true)
public List<Review> findAll() {
    // DB 성능 최적화
}
```

#### 2. Spring Data JPA의 강력한 기능 활용
```java
// ✅ Query Method 활용
List<Review> findByLectureIdOrderByStarRatingDesc(Long lectureId);

// ✅ @Query로 복잡한 쿼리 해결
@Query("SELECT r FROM Review r JOIN FETCH r.lecture WHERE r.id = :id")
Review findByIdWithLecture(@Param("id") Long id);

// ✅ @Modifying으로 Atomic Update
@Modifying
@Query("UPDATE Lecture l SET l.starRating.reviewCount = l.starRating.reviewCount + 1 WHERE l.id = :id")
int incrementReviewCount(@Param("id") Long id);
```

#### 3. 의존성 주입의 명확한 활용
```java
// ✅ 생성자 주입 + Lombok
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final LectureRepository lectureRepository;
    // final로 불변성 보장, 테스트 용이
}
```

---

### C. JVM 성능 최적화 및 안티패턴 방지

#### 1. 불필요한 객체 생성 지양
```java
// ❌ 나쁜 예: 루프 내 객체 생성
for (Review review : reviews) {
    String message = new String("처리 중"); // 매번 객체 생성
}

// ✅ 좋은 예: 재사용
String message = "처리 중";
for (Review review : reviews) {
    // 동일 객체 재사용
}
```

#### 2. Stream API의 적절한 사용
```java
// ❌ 나쁜 예: 단순 반복에 Stream 남용
reviews.stream().forEach(r -> r.setViewed(true)); // 오버헤드

// ✅ 좋은 예: 적재적소 사용
for (Review review : reviews) {
    review.setViewed(true); // 단순 반복은 for문이 효율적
}

// ✅ Stream이 적합한 경우
List<ReviewDto> dtos = reviews.stream()
    .filter(r -> r.getStarRating() >= 4.0)
    .map(ReviewDto::from)
    .collect(Collectors.toList());
```

#### 3. N+1 문제 즉시 감지 및 해결
```java
// ❌ 안티패턴: N+1 쿼리
List<Review> reviews = reviewRepository.findAll();
for (Review review : reviews) {
    Lecture lecture = review.getLecture(); // N번 쿼리 발생
}

// ✅ 해결: Fetch Join
@Query("SELECT r FROM Review r JOIN FETCH r.lecture")
List<Review> findAllWithLecture();
```

#### 4. 불필요한 Reflection 지양
```java
// ❌ 나쁜 예: 성능 저하
Method method = obj.getClass().getMethod("getName");
method.invoke(obj);

// ✅ 좋은 예: 직접 호출
obj.getName();
```

#### 5. String 연결 최적화
```java
// ❌ 나쁜 예: 루프 내 String 연결
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i; // 매번 새 String 객체 생성
}

// ✅ 좋은 예: StringBuilder 사용
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);
}
String result = sb.toString();
```

---

### D. 실용적 코드 작성 원칙

#### 1. YAGNI (You Aren't Gonna Need It)
```java
// ❌ 나쁜 예: 미래를 위한 과도한 설계
public interface ReviewStrategy {}
public class SimpleReviewStrategy implements ReviewStrategy {}
public class ComplexReviewStrategy implements ReviewStrategy {}
// 현재 요구사항에 1개만 필요한데 확장성을 위해?

// ✅ 좋은 예: 현재 필요한 것만
public class ReviewService {
    public void writeReview(ReviewRequest request) {
        // 필요할 때 리팩토링
    }
}
```

#### 2. 비즈니스 로직의 명확한 위치
```java
// ❌ 나쁜 예: 로직이 여기저기 분산
// Controller에서 별점 계산
// Service에서 검증
// Entity에서 또 다른 검증

// ✅ 좋은 예: 책임의 명확한 분리
@Entity
public class Lecture {
    public void addStarRating(Double rating) {
        // 별점 계산 로직은 Lecture 도메인에
    }
}

@Service
public class ReviewService {
    public void writeReview(ReviewRequest request) {
        // 비즈니스 흐름 제어
        validator.validate(request);
        lecture.addStarRating(request.getRating());
    }
}
```

#### 3. 예외 처리의 실용성
```java
// ❌ 나쁜 예: 과도한 커스텀 예외
public class ReviewNotFoundException extends RuntimeException {}
public class ReviewNotFoundByIdException extends ReviewNotFoundException {}
public class ReviewNotFoundByUserException extends ReviewNotFoundException {}
// 세분화가 과하면 관리 부담

// ✅ 좋은 예: 적절한 수준의 예외
public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
// 메시지로 구분 가능
```

---

## 코드 리뷰 체크리스트

### 필수 확인 사항
- [ ] **비즈니스 의도가 명확한가?** (코드만 봐도 무엇을 하는지 이해 가능)
- [ ] **Spring의 기능을 적절히 활용했는가?** (프레임워크와 싸우지 않기)
- [ ] **JVM 안티패턴이 없는가?** (N+1, 불필요한 객체 생성 등)
- [ ] **과도한 추상화가 없는가?** (YAGNI 원칙 준수)
- [ ] **성능 이슈가 없는가?** (특히 반복문, 쿼리)

### 즉시 보고 대상
- 🚨 **N+1 쿼리 발견**
- 🚨 **루프 내 객체 생성 (특히 String)**
- 🚨 **불필요한 Reflection 사용**
- 🚨 **@Transactional 누락 (데이터 변경 시)**
- 🚨 **비즈니스 로직이 Controller에 있음**

---

## 요약

### 우리의 개발 철학
1. **비즈니스가 먼저, 이론은 도구**
2. **도메인 전문가가 이해할 수 있는 코드**
3. **Spring/JVM을 제대로 활용하는 코드**
4. **현재 필요한 것에 집중 (YAGNI)**
5. **성능 문제는 즉시 해결**

### 판단 기준
- "이 코드가 비즈니스 문제를 명확히 해결하는가?"
- "Spring을 선택한 이유가 코드에 드러나는가?"
- "JVM에서 효율적으로 동작하는가?"
- "6개월 후 내가 봐도 이해할 수 있는가?"

---

**Remember**: 좋은 코드는 "멋진 패턴을 쓴 코드"가 아니라, "비즈니스 문제를 명확하고 효율적으로 해결하는 코드"입니다.
