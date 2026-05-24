# /review — 코드 리뷰 & 품질 게이트 검수 (BE)

## 개요

구현된 코드를 리뷰하고 품질 게이트를 통과하는지 검수한다.
**모든 게이트 + CI 체크를 통과해야 PASS.** 하나라도 실패하면 FAIL → PR 생성 불가.

---

## 워크플로우

### Step 1: 변경 파일 수집

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE

# main 대비 변경 파일
git diff --name-only main...HEAD

# 변경 통계
git diff --stat main...HEAD
```

변경된 Java 파일 목록을 수집하고, 각 파일을 읽어 리뷰를 수행한다.
변경 파일이 없으면 리뷰를 스킵한다.

---

## Gate 1: 아키텍처 준수 (Architecture Compliance)

> **자동 검증 필수** — 수동 확인이 아닌 코드 검색으로 위반 탐지

### 1-1. 계층 의존성 위반 탐지

**Domain 계층 순수성 검증** — Domain에 Spring/JPA import 금지:

```bash
# domain/ 패키지 내 금지 import 탐지
grep -rn "import jakarta.persistence" app/src/main/java/com/growit/app/*/domain/ || true
grep -rn "import javax.persistence" app/src/main/java/com/growit/app/*/domain/ || true
grep -rn "import org.springframework" app/src/main/java/com/growit/app/*/domain/ || true
grep -rn "import com.growit.app.*.infrastructure" app/src/main/java/com/growit/app/*/domain/ || true
grep -rn "import com.growit.app.*.controller" app/src/main/java/com/growit/app/*/domain/ || true
```

> 위 결과에 매칭이 있으면 **FAIL**. 각 위반 파일:라인 번호를 기록.

**Controller → UseCase만 호출 검증:**

```bash
# Controller에서 Domain/Infrastructure 직접 import 탐지
grep -rn "import com.growit.app.*.domain" app/src/main/java/com/growit/app/*/controller/ || true
grep -rn "import com.growit.app.*.infrastructure" app/src/main/java/com/growit/app/*/controller/ || true
```

**UseCase → Infrastructure 직접 호출 금지:**

```bash
# UseCase에서 Infrastructure 직접 import 탐지
grep -rn "import com.growit.app.*.infrastructure" app/src/main/java/com/growit/app/*/usecase/ || true
grep -rn "import com.growit.app.*.controller" app/src/main/java/com/growit/app/*/usecase/ || true
```

### 1-2. Aggregate Root 경계 검증

**외부 도메인 Entity 직접 참조 탐지:**

변경된 파일 중 domain/ 패키지 파일에서 다른 도메인의 Entity를 직접 import하는지 확인.

```bash
# 예: goal 도메인에서 user 도메인 Entity 직접 참조
# 각 변경된 domain/ 파일에 대해:
# - 같은 도메인 외 다른 도메인의 domain/{aggregate}/*.java import 확인
# - ID 참조 (Long, String)가 아닌 Entity 객체 참조인지 확인
```

**@Setter 사용 금지 (Domain Entity):**

```bash
grep -rn "@Setter" app/src/main/java/com/growit/app/*/domain/ || true
```

**판정:** 위반 0건 → PASS, 1건 이상 → FAIL (위반 목록 첨부)

---

## Gate 2: 네이밍 컨벤션 (Naming Convention)

변경된 파일만 대상으로 파일 위치 규칙 확인:

| 파일 패턴 | 위치 규칙 |
|-----------|----------|
| `*Entity.java` | `infrastructure/persistence/**/entity/` 하위 |
| `*Repository.java` (interface) | `domain/` 하위 |
| `*RepositoryImpl.java` | `infrastructure/persistence/` 하위 |
| `DB*Repository.java` | `infrastructure/persistence/**/source/` 하위 |
| `*DBMapper.java` | `infrastructure/persistence/` 하위 |
| `*UseCase.java` | `usecase/` 하위 |
| `*Controller.java` | `controller/` 하위 |
| `*Request.java` | `controller/dto/request/` 하위 |
| `*Response.java` | `controller/dto/response/` 하위 |
| `*Command.java` | `domain/**/dto/` 하위 |
| VO (enum/record) | `domain/**/vo/` 하위 |

**검증 방법:** 변경된 파일의 이름과 경로가 위 규칙과 일치하는지 확인.

**판정:** 위반 0건 → PASS, 1건 이상 → FAIL (위반 목록 첨부)

---

## Gate 3: 코드 품질 (Code Quality)

### 비즈니스 로직 위치 검증

- [ ] Controller에 비즈니스 로직이 없는가? (DTO 변환 + UseCase 호출만)
- [ ] Infrastructure에 비즈니스 로직이 없는가? (DB 작업만)
- [ ] UseCase가 얇은가? (검증 → 위임 → 저장 패턴)

### 객체 설계 검증

```bash
# Domain Entity에 @Setter 확인 (Gate 1과 중복이지만 명확성을 위해)
grep -rn "@Setter" app/src/main/java/com/growit/app/*/domain/ || true

# DTO에 record 사용 확인 (신규 DTO만)
# record가 아닌 class로 된 DTO가 있는지 확인
```

- [ ] Domain Entity에 @Setter가 없는가?
- [ ] VO는 불변인가? (record 또는 final 필드)
- [ ] 신규 DTO는 record를 사용하는가?
- [ ] Aggregate Root에 factory method(static from())가 있는가?

### 예외 처리 검증

```bash
# RuntimeException 직접 throw 탐지
grep -rn "throw new RuntimeException" app/src/main/java/com/growit/app/ || true

# NullPointerException 수동 throw 탐지
grep -rn "throw new NullPointerException" app/src/main/java/com/growit/app/ || true
```

### 일반 품질

- [ ] 메서드 길이 30줄 이하인가?
- [ ] 중첩 깊이 2단계 이하인가?
- [ ] 매직 넘버가 없는가? (상수 사용)

**판정:** 중대 위반 → FAIL, 경미한 위반 → WARN

---

## Gate 4: 테스트 (Testing)

- [ ] 새로운 Domain 로직에 단위 테스트가 있는가?
- [ ] 새로운 엔드포인트에 Controller 테스트 (RestDocs)가 있는가?
- [ ] 새로운 Entity에 Fixture 클래스가 있는가?
- [ ] 새로운 Repository에 Fake Repository가 있는가?
- [ ] 테스트가 AAA 패턴을 따르는가?
- [ ] 테스트 메서드 이름이 `should~when~` 또는 `given~when~then~` 패턴인가?

**판정:** 테스트 누락 → FAIL, 패턴 미준수 → WARN

---

## Gate 5: 보안 (Security)

- [ ] 인증이 필요한 엔드포인트에 Security 설정이 있는가?
- [ ] 민감한 데이터 (비밀번호, 토큰 등)가 응답에 포함되지 않는가?
- [ ] 입력 값 검증 (@Valid, @NotNull 등)이 적절한가?
- [ ] SQL Injection 위험이 없는가? (QueryDSL/JPA 사용 확인)

```bash
# 민감 필드 응답 노출 검사 (Response DTO에 password, secret, token 필드)
grep -rn "password\|secret\|token" app/src/main/java/com/growit/app/*/controller/dto/response/ || true
```

**판정:** 보안 위반 → FAIL

---

## Gate 6: CI/CD Pre-flight (GitHub Actions 동일 체크)

> **핵심: pr-ci.yml과 동일한 명령을 로컬에서 실행한다.**
> 이 게이트를 통과하지 못하면 GitHub Actions PR 체크도 실패한다.

### 6-1. Spotless 포맷 체크

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE
./gradlew spotlessCheck --init-script gradle/init.gradle.kts --no-configuration-cache
```

- **PASS**: exit code 0
- **FAIL**: 포맷 위반 파일 목록 출력 → `./gradlew spotlessApply`로 수정 안내

### 6-2. 테스트 실행

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE
./gradlew test --no-configuration-cache
```

- **PASS**: 전체 테스트 통과
- **FAIL**: 실패한 테스트 목록 + 에러 메시지 출력

### 6-3. 빌드 검증

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE
./gradlew build --no-configuration-cache
```

- **PASS**: 빌드 성공
- **FAIL**: 컴파일 에러 또는 빌드 실패 상세 출력

**판정:** 3개 모두 PASS → PASS, 하나라도 FAIL → 전체 FAIL

---

## 산출물

### 전체 PASS

```
## Review Result: PASS

| # | Gate | Status |
|---|------|--------|
| 1 | Architecture Compliance | PASS |
| 2 | Naming Convention | PASS |
| 3 | Code Quality | PASS |
| 4 | Testing | PASS |
| 5 | Security | PASS |
| 6 | CI/CD Pre-flight | PASS |

### CI/CD Checks
- spotlessCheck: PASS
- test: PASS (N tests, N passed)
- build: PASS

→ PR 생성 가능. `/pr` 실행하세요.
```

### FAIL

```
## Review Result: FAIL

| # | Gate | Status | Issues |
|---|------|--------|--------|
| 1 | Architecture | FAIL | Domain에 Spring import 발견 |
| 2 | Naming | PASS | - |
| 3 | Code Quality | WARN | 메서드 길이 초과 1건 |
| 4 | Testing | FAIL | 새 UseCase에 테스트 누락 |
| 5 | Security | PASS | - |
| 6 | CI/CD Pre-flight | FAIL | spotlessCheck 실패 |

### 수정 필요 사항
1. `app/.../domain/goal/Goal.java:15` — `import org.springframework.stereotype.Service` 제거
2. `CreateGoalUseCase` 단위 테스트 추가 필요
3. `./gradlew spotlessApply` 실행하여 포맷 수정

### CI/CD Checks
- spotlessCheck: FAIL (3 files with formatting issues)
- test: PASS
- build: FAIL (compile error)

→ 수정 후 `/review` 재실행하세요. PR 생성이 차단됩니다.
```

---

## 주의사항

- 코드를 직접 수정하지 않는다. 리뷰 피드백만 제공.
- **모든 Gate + CI/CD 체크가 PASS여야 `/pr` 진행 가능.**
- FAIL 시 구체적인 파일:라인번호와 수정 가이드를 제공한다.
- 기존 코드의 패턴 위반은 이번 리뷰 범위에서 제외한다 (새로 작성/변경된 코드만).
- CI/CD Pre-flight 실패 시 GitHub Actions에서도 동일하게 실패하므로 반드시 해결해야 한다.
