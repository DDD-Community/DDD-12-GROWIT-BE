# Coding Standards — GROWIT BE

## 개요

GROWIT 백엔드 코딩 컨벤션 및 스타일 가이드.

---

## 포맷팅

| 항목 | 규칙 |
|------|------|
| Formatter | Google Java Format v1.27.0 (Spotless 자동 적용) |
| Indent | 2-space (`.editorconfig`) |
| Line limit | 100 characters |
| Line ending | LF |
| Encoding | UTF-8 |
| Import | 알파벳순, 미사용 import 금지 |

### Spotless 실행

```bash
# 포맷 적용
./gradlew spotlessApply

# 포맷 검증
./gradlew spotlessCheck
```

---

## 네이밍 컨벤션

| 대상 | 규칙 | 예시 |
|------|------|------|
| Class | PascalCase | `GoalController`, `CreateGoalUseCase` |
| Method | camelCase | `findById`, `createGoal` |
| Variable | camelCase | `goalRepository`, `userId` |
| Constant | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE` |
| Package | lowercase, dot-separated | `com.growit.app.goal.controller` |
| JPA Entity | `{Name}Entity` | `GoalEntity` |
| Repository (Domain) | `{Name}Repository` | `GoalRepository` |
| Repository (Impl) | `{Name}RepositoryImpl` | `GoalRepositoryImpl` |
| JPA Repository | `DB{Name}Repository` | `DBGoalRepository` |
| QueryDSL Repo | `DB{Name}QueryRepository` | `DBGoalQueryRepository` |
| DB Mapper | `{Name}DBMapper` | `GoalDBMapper` |
| UseCase | `{Action}{Domain}UseCase` | `CreateGoalUseCase` |
| Controller | `{Domain}Controller` | `GoalController` |
| Request DTO | `{Action}{Domain}Request` | `CreateGoalRequest` |
| Response DTO | `{Domain}Response` | `GoalResponse` |
| Command DTO | `{Action}{Domain}Command` | `CreateGoalCommand` |
| Query DTO | `{Action}{Domain}Query` | `GetGoalQuery` |
| Value Object | 의미 기반 | `GoalStatus`, `PlanetType` |
| Service | `{Domain}Service` | `GoalService` |
| Validator | `{Domain}Validator` | `GoalValidator` |
| Fixture | `{Domain}Fixture` | `GoalFixture` |
| Fake Repo | `Fake{Name}Repository` | `FakeGoalRepository` |
| Test | `{Class}Test` | `GoalServiceTest`, `GoalControllerTest` |

---

## 코드 품질

| 항목 | 규칙 |
|------|------|
| 메서드 길이 | 최대 30줄 권장 |
| 중첩 깊이 | 최대 2단계 |
| 매직 넘버 | 금지 (상수 사용) |
| Null 반환 | `Optional` 사용 권장 |
| 비즈니스 로직 위치 | Domain Service에만 |

---

## Lombok 사용 규칙

| 어노테이션 | 용도 | 대상 |
|-----------|------|------|
| `@Getter` | 필드 getter | Domain Entity, JPA Entity |
| `@RequiredArgsConstructor` | final 필드 생성자 | Service, UseCase, Controller, Repository |
| `@NoArgsConstructor(access = AccessLevel.PROTECTED)` | JPA용 기본 생성자 | JPA Entity |
| `@Builder` | 빌더 패턴 | 필요 시 (DTO, Entity) |
| `@Setter` | **금지** (Domain Entity) | 사용하지 않음 |
| `@Data` | **금지** | 사용하지 않음 |
| `@AllArgsConstructor` | 제한적 사용 | factory method 대신 사용 금지 |

---

## DTO 규칙

### Record 사용

```java
// Request DTO
public record CreateGoalRequest(
  @NotBlank String title,
  @NotNull Long userId,
  @Size(max = 200) String description
) { }

// Response DTO
public record GoalResponse(
  Long id,
  String title,
  String description,
  LocalDateTime createdAt
) { }

// Command DTO
public record CreateGoalCommand(
  String title,
  Long userId,
  String description
) { }
```

- Request: 검증 어노테이션 포함 (`@NotBlank`, `@NotNull`, `@Size` 등)
- Response: 검증 없음, 조회 결과 전달
- Command: 비즈니스 의미 기반, 검증 없음 (이미 Controller에서 검증됨)

---

## Enum 규칙

```java
@Getter
@RequiredArgsConstructor
public enum GoalStatus {
  ACTIVE("활성"),
  COMPLETED("완료"),
  DELETED("삭제");

  private final String description;
}
```

- `@Getter`, `@RequiredArgsConstructor`
- 한국어 description 필드 포함
- Domain 패키지 내 `vo/` 디렉토리에 위치

---

## 테스트 규칙

### 네이밍 패턴

```java
// should ~ when ~ 패턴
@Test
void shouldCreateGoal_whenValidCommand() { ... }

// given ~ when ~ then ~ 패턴
@Test
void givenValidUser_whenCreateGoal_thenSuccess() { ... }
```

### AAA 패턴 (Arrange-Act-Assert)

```java
@Test
void shouldCreateGoal_whenValidCommand() {
  // Arrange (Given)
  var command = new CreateGoalCommand("목표", 1L, "설명");
  var fakeRepository = new FakeGoalRepository();
  var service = new GoalService(fakeRepository);

  // Act (When)
  var result = service.create(command);

  // Assert (Then)
  assertThat(result).isNotNull();
  assertThat(result.getTitle()).isEqualTo("목표");
}
```

### Fixture 클래스

```java
public class GoalFixture {
  public static Goal createGoal() {
    return Goal.from(1L, "테스트 목표", 1L, GoalStatus.ACTIVE, ...);
  }

  public static Goal createGoal(Long id) {
    return Goal.from(id, "테스트 목표", 1L, GoalStatus.ACTIVE, ...);
  }

  public static Goal createGoalWithUser(Long userId) {
    return Goal.from(1L, "테스트 목표", userId, GoalStatus.ACTIVE, ...);
  }
}
```

### Fake Repository

```java
public class FakeGoalRepository implements GoalRepository {
  private final Map<Long, Goal> store = new HashMap<>();
  private Long sequence = 1L;

  @Override
  public Goal save(Goal goal) {
    if (goal.getId() == null) {
      // 신규: ID 자동 생성
      Goal saved = Goal.from(sequence++, ...);
      store.put(saved.getId(), saved);
      return saved;
    }
    store.put(goal.getId(), goal);
    return goal;
  }

  @Override
  public Optional<Goal> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }
}
```

### Controller 테스트 (RestDocs)

```java
class GoalControllerTest extends BaseControllerTest {
  @MockitoBean
  private CreateGoalUseCase createGoalUseCase;

  @Test
  void shouldCreateGoal_whenValidRequest() throws Exception {
    // given
    var request = new CreateGoalRequest("목표", 1L, "설명");
    given(createGoalUseCase.execute(any()))
      .willReturn(GoalFixture.createGoal());

    // when & then
    mockMvc.perform(post("/api/v1/goals")
        .contentType(APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andDo(document("goal-create",
        requestFields(
          fieldWithPath("title").description("목표 제목"),
          fieldWithPath("userId").description("사용자 ID"),
          fieldWithPath("description").description("목표 설명")
        ),
        responseFields(
          fieldWithPath("id").description("목표 ID"),
          fieldWithPath("title").description("목표 제목")
        )
      ));
  }
}
```

---

## Git 규칙

### Conventional Commits

| 타입 | 용도 |
|------|------|
| `feat:` | 새 기능 |
| `fix:` | 버그 수정 |
| `refactor:` | 리팩토링 (동작 변경 없음) |
| `test:` | 테스트 추가/수정 |
| `docs:` | 문서 |
| `chore:` | 기타 (빌드, 설정 등) |

- 커밋 메시지는 **영어**로 작성
- 첫 글자 소문자
- 동사 현재형 (add, fix, update, remove)

```
feat: add goal creation endpoint
fix: resolve null pointer in goal query
refactor: extract goal validation logic
test: add unit tests for goal service
```

### 브랜치 네이밍

```
{feat|fix|modify}/{TICKET_ID}-{kebab-summary}
```

예시:
```
feat/GRO-42-add-goal-sharing
fix/GRO-55-fix-auth-token-refresh
modify/GRO-60-update-retrospect-api
```

### Git Add 규칙

```bash
# 좋은 예: 파일 지정
git add app/src/main/java/com/growit/app/goal/controller/GoalController.java
git add app/src/test/java/com/growit/app/goal/GoalControllerTest.java

# 나쁜 예: 전체 추가 (금지)
git add -A   # 금지!
git add .    # 금지!
```

---

## API 응답 형식

```java
// 성공 응답
{
  "success": true,
  "data": { ... },
  "error": null
}

// 에러 응답
{
  "success": false,
  "data": null,
  "error": {
    "code": "GOAL_NOT_FOUND",
    "message": "목표를 찾을 수 없습니다."
  }
}
```

`ApiResponse` 래퍼를 사용하여 일관된 응답 형식을 유지한다.
