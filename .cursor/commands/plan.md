# /plan — 구현 계획 수립

## 개요

리서치 결과를 기반으로 구현 계획을 수립한다.
엔티티 변경, API 설계, 마이그레이션, 테스트 전략을 포함한 체계적 계획을 산출한다.

---

## 입력

- `TICKET_ID`: 티켓 ID
- 리서치 결과: `.research/{TICKET_ID}/research.md`
- 아키텍처 규칙: `.ai/rules/architecture.md`
- 코딩 표준: `.ai/rules/coding-standards.md`

---

## 워크플로우

### Step 1: 리서치 결과 로드

`.research/{TICKET_ID}/research.md`를 읽고 영향 범위를 확인한다.

### Step 2: 엔티티 변경 계획

- 신규 Aggregate Root 필요 여부
- 기존 Aggregate Root 수정 사항
- 새로운 Value Object 필요 여부
- Domain Entity 필드 추가/수정
- JPA Entity 매핑 변경

각 엔티티에 대해 필드 목록, 타입, 제약조건을 명시한다.

### Step 3: API 엔드포인트 계획

각 엔드포인트에 대해:

```
- HTTP Method + Path
- Request DTO (필드, 타입, 검증)
- Response DTO (필드, 타입)
- 인증/인가 요구사항
- 페이징 여부
```

### Step 4: DB 마이그레이션 계획

- Flyway 스크립트 이름: `V{next}__description.sql`
- CREATE TABLE / ALTER TABLE 구문
- 인덱스 추가
- 제약조건 (FK, UNIQUE, NOT NULL)
- 데이터 마이그레이션 필요 여부

현재 최신 마이그레이션 버전을 확인하여 다음 번호를 결정한다.

### Step 5: 테스트 전략

- **단위 테스트**: Domain Service, Validator, Entity 메서드
- **UseCase 테스트**: Fake Repository를 사용한 비즈니스 로직 검증
- **Controller 테스트**: RestDocs를 활용한 API 문서화 겸 테스트
- **Fixture 클래스**: 테스트 데이터 생성 헬퍼
- **Fake Repository**: 메모리 기반 Repository 구현

### Step 6: 구현 순서 결정

엄격한 순서를 따른다:

```
1. DB 마이그레이션 (Flyway V{next}__description.sql)
2. Domain 계층
   a. Value Objects (enum, record)
   b. Aggregate Root Entity
   c. Repository 인터페이스
   d. Command/Query DTO
   e. Domain Service / Validator / Query
3. Infrastructure 계층
   a. JPA Entity
   b. JPA Repository (Spring Data)
   c. QueryDSL Repository (필요 시)
   d. DB Mapper
   e. Repository 구현체
   f. 외부 클라이언트 (필요 시)
4. UseCase 계층
   a. UseCase 클래스
   b. UseCase DTO (필요 시)
5. Controller 계층
   a. Request/Response DTO
   b. DTO Mapper
   c. Controller
6. 테스트
   a. Fixture 클래스
   b. Fake Repository
   c. Domain 단위 테스트
   d. UseCase 테스트
   e. Controller 테스트 (RestDocs)
```

---

## 산출물

파일: `.plan/{TICKET_ID}/plan.md`

```markdown
# Plan: {TICKET_ID} — {TICKET_TITLE}

## 요약
{구현 범위 한 줄 요약}

## 엔티티 변경

### {AggregateRoot} (신규/수정)
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| ... | ... | ... | ... |

## API 엔드포인트

### {Method} {Path}
- 설명: ...
- 인증: 필요/불필요
- Request:
  ```json
  { ... }
  ```
- Response:
  ```json
  { ... }
  ```

## DB 마이그레이션

### V{next}__{description}.sql
```sql
-- 마이그레이션 SQL
```

## 테스트 계획
- [ ] {TestClass}#{testMethod} — {검증 내용}

## 구현 체크리스트

### 1. 마이그레이션
- [ ] `V{next}__{description}.sql`

### 2. Domain
- [ ] `{path}/{File}.java` — {설명}

### 3. Infrastructure
- [ ] `{path}/{File}.java` — {설명}

### 4. UseCase
- [ ] `{path}/{File}.java` — {설명}

### 5. Controller
- [ ] `{path}/{File}.java` — {설명}

### 6. 테스트
- [ ] `{path}/{File}Test.java` — {설명}

## 예상 파일 변경
- 신규: {N}개
- 수정: {N}개
- 삭제: {N}개
```

---

## 주의사항

- **코드를 작성하지 않는다.** 계획만 수립.
- 기존 코드 패턴을 분석하여 일관된 패턴을 유지한다.
- Aggregate Root 경계를 명확히 한다.
- 마이그레이션 버전 충돌을 방지한다.
- FE/APP에 영향을 주는 API 변경은 명시적으로 표기한다.
