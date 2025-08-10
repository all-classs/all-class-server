# 🎯 batch-dummy 모듈

더미 수강후기 데이터를 주입하는 독립적인 배치 모듈입니다.

## 📁 **프로젝트 구조**

```
batch-dummy/
├── build.gradle          # 빌드 설정
├── README.md
└── src/main/java/classreviewsite/batchdummy/
    ├── BatchApplication.java     # 메인 실행 클래스
    ├── model/
    │   └── DummyReviewData.java  # 더미데이터 모델
    └── service/
        └── DummyDataService.java # 더미데이터 주입 로직
```

## 🚀 **실행 방법**

### **개발환경에서 실행**
```bash
# Gradle로 직접 실행 (dev 프로파일)
./gradlew :batch-dummy:bootRun --args='--spring.profiles.active=dev'

# 또는 IDE에서 BatchApplication.main() 실행
# VM options: -Dspring.profiles.active=dev
```

### **프로덕션 환경에서 실행**
```bash
# JAR 파일 빌드
./gradlew :batch-dummy:bootJar

# 빌드된 JAR 실행 (prod 프로파일)
java -jar batch-dummy/build/libs/batch-dummy-*.jar --spring.profiles.active=prod

# 환경변수와 함께 실행
SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/review2 \
SPRING_DATASOURCE_USERNAME=prod_user \
SPRING_DATASOURCE_PASSWORD=prod_password \
java -jar batch-dummy/build/libs/batch-dummy-*.jar --spring.profiles.active=prod
```

### **Docker 환경에서 실행**
```bash
# 기존 application과 동일한 네트워크에서 실행
docker run --network class-review-network \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e MYSQL_USER=user \
  -e MYSQL_PASSWORD=password \
  your-registry/batch-dummy:latest

# docker-compose에 추가하여 실행
```

## 🔧 **활성화/비활성화**

### **더미데이터 주입 활성화**
`BatchApplication.java`의 `run()` 메소드에서 주석 해제:

```java
// 이 부분의 주석을 제거하세요
/*
log.info("======== 더미 수강후기 데이터 주입 시작 ========");
// ... 더미데이터 주입 로직
*/
```

### **더미데이터 주입 비활성화** (현재 상태)
`run()` 메소드의 로직이 주석 처리되어 있어 실행되지 않습니다.

## 📊 **기능**

### **1. 데이터베이스 준비 상태 확인**
- 최대 30초간 데이터베이스 연결 대기
- 기본 테이블 존재 여부 확인

### **2. 중복 방지**
- 더미데이터가 이미 존재하는지 확인
- 중복 주입 방지

### **3. 더미데이터 주입**
- 30개의 실제 수강후기 데이터 주입
- 강의별 평점 자동 계산
- 실패한 데이터는 로그로 기록

## ⚙️ **설정**

### **프로파일별 설정**

#### **dev 프로파일** (`application-dev.yml`)
- 로컬 개발용 설정
- 상세한 로깅 (DEBUG 레벨)
- SQL 쿼리 출력 활성화
- 로컬 MySQL 연결

#### **prod 프로파일** (`application-prod.yml`)  
- 프로덕션용 설정
- 최소한의 로깅 (INFO/WARN 레벨)
- 환경변수 기반 DB 연결
- 성능 최적화된 커넥션 풀

#### **docker 프로파일** (`application-docker.yml`)
- Docker 컨테이너 환경용
- mysql 서비스명으로 DB 연결
- 컨테이너 최적화된 설정

### **데이터베이스 연결**

#### **환경변수로 설정** (prod 프로파일)
```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-server:3306/review2
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=prod_password
```

#### **Docker 환경변수** (docker 프로파일)
```bash
export MYSQL_USER=user
export MYSQL_PASSWORD=password
```

### **배치 자동 종료 설정**
- **`spring.main.web-application-type: none`**: 웹 서버 비활성화
- **`System.exit(exitCode)`**: 작업 완료 후 자동 종료
- **Exit Code 0**: 성공적 완료
- **Exit Code 1**: 오류 발생

## 🔍 **로그 예시**

### **비활성화 상태 (현재)**
```
더미데이터 주입이 비활성화되어 있습니다. (소스코드 주석 처리됨)
```

### **활성화 상태**
```
======== 더미 수강후기 데이터 주입 시작 ========
데이터베이스 준비 완료 확인됨
10건의 더미 리뷰 주입 완료
20건의 더미 리뷰 주입 완료
30건의 더미 리뷰 주입 완료
더미데이터 주입 완료 - 성공: 30건, 실패: 0건
======== 더미 수강후기 데이터 주입 완료 ========
```

### **이미 존재할 때**
```
더미데이터가 이미 존재합니다 (총 30개 리뷰)
```

## 💡 **사용 시나리오**

### **1. 개발/테스트 환경 초기화**
```bash
# 데이터베이스 초기화 후
./gradlew :batch-dummy:bootRun
```

### **2. CI/CD 파이프라인**
```yaml
# GitHub Actions 예시
- name: Inject dummy data
  run: |
    # 주석 해제 후 실행
    sed -i 's|/\*|//\*|g' batch-dummy/src/main/java/classreviewsite/batchdummy/BatchApplication.java
    ./gradlew :batch-dummy:bootRun
```

### **3. 스케줄러 기반 주기적 실행**
```bash
# cron에 등록하여 주기적 실행
0 3 * * * cd /path/to/project && ./gradlew :batch-dummy:bootRun
```

## ⚠️ **주의사항**

1. **프로덕션 환경**: 더미데이터 주입 전 반드시 주석 해제 확인
2. **중복 실행**: 이미 데이터가 있으면 추가 주입하지 않음
3. **데이터베이스 준비**: 애플리케이션이 먼저 실행되어 스키마가 생성되어야 함
4. **의존성**: application 모듈의 엔티티와 서비스에 의존

## 🔗 **관련 모듈**

- `application`: 메인 애플리케이션 (엔티티, 서비스 제공)
- `batch-student`: 학생 데이터 배치
- `batch-lecture`: 강의 데이터 배치  
- `batch-enrollment`: 수강신청 데이터 배치