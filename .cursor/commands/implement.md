# /implement — 코드 구현 실행

## 개요

구현 계획을 기반으로 실제 코드를 작성한다.
DDD 아키텍처 규칙을 엄격히 준수하며, 구현 순서를 따른다.

---

## 입력

- `TICKET_ID`: 티켓 ID
- 구현 계획: `.plan/{TICKET_ID}/plan.md`
- 아키텍처 규칙: `.ai/rules/architecture.md`
- 코딩 표준: `.ai/rules/coding-standards.md`

---

## 사전 준비

### 브랜치 생성

```bash
git checkout -b {feat|fix|modify}/{TICKET_ID}-{kebab-summary}
```

### 규칙 로드

반드시 아래 파일을 먼저 읽는다:
- `.ai/rules/architecture.md` — Aggregate Root 패턴, 계층 규칙
- `.ai/rules/coding-standards.md` — 네이밍, 포맷, 테스트 패턴

---

## 구현 순서

**반드시 이 순서를 따른다. 건너뛰지 않는다.**

### Phase 1: DB 마이그레이션

```
app/src/main/resources/db/migration/V{next}__{description}.sql
```

- 기존 마이그레이션 파일 목록을 확인하여 다음 버전 번호 결정
- DDL 작성 (CREATE TABLE, ALTER TABLE, CREATE INDEX)
- 제약조건 포함 (FK, UNIQUE, NOT NULL, CHECK)

### Phase 2: Domain 계층

이 계층에는 **Spring/JPA 의존성이 없다.** 순수 Java 코드.

**2a. Value Objects**
```java
// record 또는 enum 사용
public record {Name}({Type} value) { }
public enum {Status} { VALUE1, VALUE2 }
```

**2b. Aggregate Root Entity**
```java
// JPA 어노테이션 없음, Lombok @Getter + @RequiredArgsConstructor
// factory method: static from()
// 비즈니스 메서드 포함
public class {AggregateRoot} {
  private final Long id;
  // ...
  public static {AggregateRoot} from(...) { ... }
  public void update{Field}(...) { ... }
}
```

**2c. Repository 인터페이스**
```java
// 인터페이스만 정의. 구현은 Infrastructure에서.
public interface {AggregateRoot}Repository {
  {AggregateRoot} save({AggregateRoot} entity);
  Optional<{AggregateRoot}> findById(Long id);
  // ...
}
```

**2d. Command/Query DTO**
```java
// record 사용
public record Create{Domain}Command(
  {Type} field1,
  {Type} field2
) { }
```

**2e. Domain Service / Validator / Query**
```java
// 비즈니스 로직 조합
@RequiredArgsConstructor
public class {Domain}Service {
  private final {AggregateRoot}Repository repository;
  // ...
}
```

### Phase 3: Infrastructure 계층

**3a. JPA Entity**
```java
@Entity
@Table(name = "{table_name}")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class {AggregateRoot}Entity extends BaseTimeEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  // DB 컬럼 매핑
}
```

**3b. JPA Repository**
```java
public interface DB{AggregateRoot}Repository extends JpaRepository<{AggregateRoot}Entity, Long> {
  // Spring Data JPA 쿼리 메서드
}
```

**3c. QueryDSL Repository** (복잡한 조회 시)
```java
public interface DB{AggregateRoot}QueryRepository {
  // QueryDSL 메서드 시그니처
}
```

**3d. DB Mapper**
```java
// Domain Entity <-> JPA Entity 변환
public class {AggregateRoot}DBMapper {
  public static {AggregateRoot} toDomain({AggregateRoot}Entity entity) { ... }
  public static {AggregateRoot}Entity toEntity({AggregateRoot} domain) { ... }
}
```

**3e. Repository 구현체**
```java
@Repository
@RequiredArgsConstructor
public class {AggregateRoot}RepositoryImpl implements {AggregateRoot}Repository {
  private final DB{AggregateRoot}Repository dbRepository;
  // Domain Repository 인터페이스 구현
}
```

### Phase 4: UseCase 계층

```java
@Service
@RequiredArgsConstructor
@Transactional
public class Create{Domain}UseCase {
  private final {AggregateRoot}Repository repository;
  private final {Domain}Service service;

  public {Result} execute({Command} command) {
    // 1. 검증
    // 2. 비즈니스 로직 위임
    // 3. 저장
    // 4. 결과 반환
  }
}
```

### Phase 5: Controller 계층

**5a. Request/Response DTO**
```java
// record 사용, 검증 어노테이션 포함
public record Create{Domain}Request(
  @NotBlank String field1,
  @NotNull Long field2
) { }
```

**5b. DTO Mapper**
```java
public class {Domain}DtoMapper {
  public static Create{Domain}Command toCommand(Create{Domain}Request request) { ... }
  public static {Domain}Response toResponse({Domain} domain) { ... }
}
```

**5c. Controller**
```java
@RestController
@RequestMapping("/api/v1/{domains}")
@RequiredArgsConstructor
public class {Domain}Controller {
  private final Create{Domain}UseCase createUseCase;

  @PostMapping
  public ApiResponse<{Domain}Response> create(@Valid @RequestBody Create{Domain}Request request) {
    // DTO -> Command 변환 -> UseCase 호출 -> Response 반환
  }
}
```

### Phase 6: 테스트

**6a. Fixture 클래스**
```java
public class {Domain}Fixture {
  public static {AggregateRoot} create{AggregateRoot}() { ... }
  public static {AggregateRoot} create{AggregateRoot}(Long id) { ... }
}
```

**6b. Fake Repository**
```java
public class Fake{AggregateRoot}Repository implements {AggregateRoot}Repository {
  private final Map<Long, {AggregateRoot}> store = new HashMap<>();
  private Long sequence = 1L;
  // 메모리 기반 구현
}
```

**6c. 단위 테스트 (AAA 패턴)**
```java
class {Domain}ServiceTest {
  @Test
  void should{Action}_when{Condition}() {
    // Arrange
    var entity = {Domain}Fixture.create{AggregateRoot}();

    // Act
    var result = service.someMethod(entity);

    // Assert
    assertThat(result).isNotNull();
  }
}
```

**6d. Controller 테스트 (RestDocs)**
```java
class {Domain}ControllerTest extends BaseControllerTest {
  @Test
  void should{Action}_when{Condition}() throws Exception {
    // given
    var request = new Create{Domain}Request(...);

    // when & then
    mockMvc.perform(post("/api/v1/{domains}")
        .contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andDo(document("{domain}-create",
        requestFields(...),
        responseFields(...)
      ));
  }
}
```

---

## 검증 단계

구현 완료 후 반드시 실행:

### 1. 코드 포맷팅

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE && ./gradlew spotlessApply
```

### 2. 테스트 실행

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE && ./gradlew test
```

### 3. 빌드 검증

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE && ./gradlew build
```

### 4. 실패 시

- 에러 메시지를 분석하고 수정
- spotlessApply → test → build 순서로 재실행
- 모두 통과할 때까지 반복

---

## 커밋

모든 검증 통과 후 커밋한다.

```bash
git add {specific files}
git commit -m "{type}: {description}"
```

- `feat:` 새 기능
- `fix:` 버그 수정
- `refactor:` 리팩토링
- `test:` 테스트 추가/수정
- `docs:` 문서
- `chore:` 기타

**`git add -A` 또는 `git add .` 금지. 파일 지정만 사용.**

---

## 주의사항

- 계획에 없는 코드를 작성하지 않는다.
- 기존 코드 패턴을 따른다 (새 패턴 도입 금지).
- Aggregate Root 경계를 넘는 직접 참조를 하지 않는다.
- Domain 계층에 Spring/JPA 의존성을 넣지 않는다.
- 한 번에 하나의 Aggregate Root만 수정한다.
