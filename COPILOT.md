# COPILOT.md

이 파일은 이 저장소의 코드 작업 시 COPILOT Code(copilot.ai/code)에 대한 지침을 제공합니다.

# 답변 규칙

- 모든 답변은 **한국어**로 작성되어야 합니다.
- API 문서 및 외부 연동 레퍼런스는 **영어**로 유지할 수 있습니다.
- 코드 자동화 시 Swagger/OpenAPI 명세 기반 생성을 우선시합니다.

## 빌드/테스트 명령어
- 빌드: `./gradlew build`
- 테스트: `./gradlew test`

## 코드 스타일

- 줄 길이: 100자 이하 권장
- 클래스/인터페이스: PascalCase
- 변수/함수: camelCase
- 상수: UPPER_SNAKE_CASE
- 패키지 구조: `com.growit.{layer}.{domain}`
- 메서드 길이: 30줄 이하 권장
- 중첩 depth: 최대 2단계
- 주석: JavaDoc 또는 inline 주석은 필요한 경우에만 작성

### Spotless 코드 포맷터 적용 기준

본 프로젝트는 [Spotless](https://github.com/diffplug/spotless)를 이용하여 Java 코드 스타일을 자동화합니다.

#### 포맷팅 세부 기준

- **Formatter**: Google Java Format v1.27.0 사용
- **불필요한 import 제거**: `removeUnusedImports()` 자동 수행
- **import 순서 정리**: `importOrder()` 적용, 알파벳 순서 기반
- **줄 끝 공백 제거**: `trimTrailingWhitespace()` 자동 적용
- **파일 마지막 줄 개행 보장**: `endWithNewline()` 적용
- 코드를 커밋하기 전에 아래 명령어를 실행해주세요:
  - `./gradlew spotlessApply --init-script gradle/init.gradle.kts --no-configuration-cache`
  - `./gradlew spotlessCheck --init-script gradle/init.gradle.kts --no-configuration-cache`

## 프로젝트 구조 (패키지 구조 기준)

본 프로젝트는 도메인 중심 설계(DDD)와 수직 계층 분리를 바탕으로, 각 Aggregate Root 단위로 디렉토리를 구성합니다. 예를 들어 `goal`, `user`, `common`과 같은 도메인별
디렉토리가 있으며, 각 디렉토리는 다음과 같은 서브 디렉토리 구조를 가집니다:

- `controller`: REST API 진입점
  - `dto.request`: 클라이언트 요청 모델
  - `dto.response`: 응답 모델
  - `mapper`: DTO ↔ 도메인 매핑
- `usecase`: 도메인 유스케이스 처리 계층 (Application Layer)
- `domain`: 비즈니스 도메인 로직 계층
  - `command`: 유스케이스를 위한 입력 모델
  - `service`: 핵심 비즈니스 정책 및 규칙 처리
  - `vo`: 값 객체(Value Object) 및 Aggregate Root
  - `GoalRepository`: 도메인 관점 저장소 인터페이스
- `infrastructure`: 외부 시스템 연동 및 구현체 계층
  - `external`: AWS, Email 등 외부 API 연동
  - `internal`: 내부 시스템 간 연동
  - `persistence.{domain}`: 영속성 계층 구현체
    - `source`: JPA 구현체 및 DB ↔ 도메인 매핑

이 구조는 각 도메인을 수직적으로 독립 유지하며, 모듈화된 설계를 통해 확장성과 유지보수성을 확보합니다.

## 프로젝트 구성요소

각 도메인({domain}) 디렉토리는 독립적인 루트 애그리거트로 구성되며, 다음과 같은 계층별 클래스를 포함합니다:

### 📂 controller

- **{domain}Controller**
  REST API 진입점으로 클라이언트 요청을 처리하고 UseCase를 호출합니다.

#### 📁 dto

- **Create{domain}Request**
  클라이언트로부터 받은 요청 데이터를 담는 DTO입니다.
- **{domain}Response**
  도메인 객체를 응답 형태로 변환한 DTO입니다.

#### 📁 mapper

- **{domain}Mapper**
  DTO ↔ Command ↔ Domain 간 매핑을 담당합니다.

---

### 📂 domain

#### 📁 command

- **Create{domain}Command**
  유스케이스 수행을 위한 도메인 입력 명령 객체입니다.

#### 📁 service

- **{domain}Service**
  핵심 비즈니스 로직을 처리합니다.
- **{domain}Calculator**, **{domain}Validator**
  계산, 검증 등 보조 비즈니스 정책을 분리한 객체입니다.

#### 📁 vo

- **{domain}**
  도메인의 Aggregate Root로 상태와 행위를 포함합니다.
- **Email** 등
  관련된 값 객체(Value Object)입니다.

- **{domain}Repository**
  도메인 관점의 추상 저장소 인터페이스입니다.

- **{domain}**
  도메인 data class. **{domain}**.from() 과같이 static 을 통한 자기자신 생성 책임을 담당합니다.

---

### 📂 usecase

- **Get{domain}UseCase**
  도메인 유스케이스를 실행하는 Application Layer 계층입니다.

---

### 📂 infrastructure

#### 📁 external

- **aws**, **email**
  외부 시스템 또는 외부 API와의 연동 책임을 가집니다.

#### 📁 internal

- 내부 시스템 또는 다른 도메인과의 연동 책임을 가집니다.

#### 📁 persistence.{domain}

- 📁 source
  영속성 계층의 실제 구현 클래스가 위치합니다.

  - 📁 entity
    - **{domain}Entity**: DB 테이블과 매핑되는 JPA 엔티티입니다.
    - **{domain}Dao**: Spring Data JPA 또는 MyBatis 등 기술 기반 DAO 인터페이스입니다.

  - **{domain}RepositoryImpl**: {domain}Repository 인터페이스의 실제 구현체입니다.
  - **{domain}DbMapper**: Entity ↔ Domain 간 변환을 담당합니다.

---

이 구조는 도메인 단위의 수직적 독립성과 계층 간 책임 분리를 통해 유지보수성과 확장성을 고려하여 설계되었습니다.

## 서비스 아키텍처

- **레이어드 아키텍처 + DDD 패턴 일부 적용**
- 각 도메인은 독립적으로 의존 관계를 가짐
- Controller → UseCase → Domain → Infra 흐름 유지
- `@Transactional`은 Service 또는 UseCase 레이어에서만 사용

## 의존성 관리

- Gradle 기반 멀티 모듈 구성 지원 (필요 시)
- 의존성은 `build.gradle.kts`에 명시
- 주요 라이브러리 버전은 루트 `build.gradle.kts`에서 통합 관리

## 코딩 원칙

- 모든 코드는 DRY, KISS, YAGNI 원칙에 입각하여 작성
- 기능 단위로 명확히 분리, 추후 유지보수 용이성 고려
- 예외는 도메인 기반의 Custom Exception을 사용하여 처리
- 상태 값은 enum을 우선 사용하며, 상태 전이는 도메인 내부에서만 허용

## 개발 작업 체크 포인트

- restdoc 테스트를 추가
- 필요한 도메인 모델, DTO 정의
- UseCase 또는 Service 클래스에서 로직 구현
- Controller에서 DTO를 매핑하여 입출력 처리
- 작성한 기능은 단위 테스트 및 통합 테스트 작성 후 PR
- 코드리뷰 이후 merge 시 모든 테스트가 통과해야 함

## 구현 세부 사항 예시 Root Aggregate 단위 디렉토리 구조

### 📘 도메인: goal

본 도메인은 사용자 샘플 정보를 다루는 단위로, 다음과 같은 구성 요소로 개발되었습니다.

---

### ✅ controller

- **GoalController**
  - `/goal` 엔드포인트의 진입점으로 동작
  - 요청을 받아 DTO → Command 변환 후 UseCase 호출

#### 📁 dto.request

- **CreateGoalRequest**
  - 샘플 생성 요청을 담는 요청 바디 모델

#### 📁 dto.response

- **GoalResponse**
  - 생성 또는 조회된 샘플 데이터를 클라이언트에게 반환

#### 📁 mapper

- **GoalMapper**
  - `CreateSampleRequest` → `CreateGoalCommand` 변환
  - `Goal` → `GoalResponse` 변환 책임

---

### ✅ domain

#### 📁 command

- **CreateGoalCommand**
  - 유스케이스에서 사용할 입력 모델로, 요청 파라미터를 캡슐화

#### 📁 service

- **GoalService**
  - 도메인의 주요 비즈니스 로직을 처리 (e.g. 생성, 조회)
- **GoalValidator**
  - 입력값의 형식이나 유효성을 검증

#### 📁 vo

- **Email**
  - 이메일 값 객체. 내부 정규식 검증 포함
- **Goal**
  - Aggregate Root. 자체 정적 팩토리 메서드 `from()`을 통해 생성됨
- **GoalRepository**
  - 저장소 추상화 인터페이스. 구현체는 Infra 레이어에 존재

---

### ✅ usecase

- **CreateGoalUseCase**
  - 트랜잭션을 관리하며 비즈니스 로직의 유스케이스를 캡슐화
  - Validator → Service → Repository 흐름을 조합

---

### ✅ infrastructure

#### 📁 external / internal

- 향후 AWS, 이메일 등 외부 시스템 연동 구현 예정

#### 📁 persistence.goal.source.entity

- **GoalEntity**
  - JPA 기반 DB 테이블 매핑 클래스
- **DBGoalRepository**
  - Spring Data JPA 인터페이스. DB 접근 기능 제공

#### 📁 persistence.sample

- **GoalRepositoryImpl**
  - 도메인의 `GoalRepository` 인터페이스 구현
- **GoalDbMapper**
  - Entity ↔ Domain 간 변환 기능 담당

---

이러한 구조를 통해 Goal 도메인은 다음과 같은 특징을 가집니다:

- 책임 분리와 캡슐화를 통한 테스트 용이성
- 계층 간 명확한 의존 흐름 유지
- 도메인 중심의 확장성과 유지보수성 확보

---

## 🧪 테스트 작성 지침서

아래 Goal 테스트와 같은 형태로 신규 도메인 테스트 지침서를 따라 코드를 작성해주세요!!!.

### 1. 📁 `fake/goal`

- `FakeGoalRepository`: 실제 DB 접근 없이 도메인 테스트를 수행할 수 있는 인메모리 저장소입니다.
  - 중복 저장 방지, 조회, 삭제 등의 기본 동작을 구현합니다.
- `GoalFixture.java`: 테스트용 Goal 도메인 객체를 생성하는 빌더 또는 정적 헬퍼 클래스를 정의합니다.
  - ex) `GoalFixture.aGoal()`, `GoalFixture.withUserId(...)`

### 2. 📁 `goal/controller`

- `GoalControllerTest`: RestDocs 문서화 및 API 입력/출력 검증을 수행합니다.
  - `@WebMvcTest` 또는 `@SpringBootTest`를 사용
  - `MockMvc`로 실제 API 호출 흐름을 테스트
  - 요청 JSON → DTO 매핑, 응답 JSON 스키마 검증 포함
  - RestDocs 스니펫 생성 (`andDo(document(...))`)

### 3. 📁 `goal/domain/goal/service`

- `GoalServiceTest`: GoalService 내 주요 비즈니스 로직을 검증합니다.
  - 순수 단위 테스트이며 외부 의존성 없음
  - 검증/계산 관련 로직은 가능한 한 서비스로 분리하여 테스트 커버리지 확보

### 4. 📁 `goal/domain/goal/vo`

- `GoalDurationTest`: 값 객체(Value Object)의 불변성, 생성 제약, equals/hashcode 등을 테스트합니다.
  - 생성 실패 케이스도 반드시 포함 (`IllegalArgumentException` 등)

### 5. 📁 `goal/usecase`

- `DeleteGoalUseCaseTest`, `GetUserGoalsUseCaseTest`: 실제 유스케이스 계층의 트랜잭션 흐름과 비즈니스 결과를 검증합니다.
  - `FakeGoalRepository`와 `GoalFixture` 조합하여 테스트 구성
  - 유스케이스의 입력(Command) → 기대 결과 또는 예외 처리 흐름까지 테스트
  - `@Transactional` 기반 흐름이 포함된 경우도 검증

### ✅ 공통 규칙

- 테스트 클래스명은 `~Test` 접미사 사용
- 테스트 메서드는 `should~when~` 또는 `given~when~then~` 네이밍 규칙
- AAA (Arrange-Act-Assert) 패턴을 명확히 구분하여 작성
- 실패 케이스 (예외 발생) 테스트도 반드시 포함
- 테스트는 서로 독립적으로 동작하도록 구성 (상태 공유 금지)
