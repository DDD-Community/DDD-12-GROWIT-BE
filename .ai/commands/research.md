# /research — 요구사항 분석 & 변경 영향도 파악

## 개요

growit-lead 오케스트레이터에서 위임받아 BE 코드베이스를 분석한다.
티켓 요구사항을 기반으로 변경 영향도를 파악하고 리서치 결과를 산출한다.

---

## 입력

오케스트레이터가 주입하는 컨텍스트:

- `TICKET_ID`: 티켓 ID (예: GRO-42)
- `TICKET_TITLE`: 티켓 제목
- `TICKET_TYPE`: feat | fix | modify
- `TICKET_SUMMARY`: 요구사항 요약
- `TICKET_REQUIREMENTS`: 상세 요구사항

---

## 워크플로우

### Step 1: 영향 도메인 식별

요구사항을 분석하여 영향받는 도메인 모듈을 식별한다.

```
도메인 목록: goal, todo, user, advice, retrospect, mission, resource, common
경로: app/src/main/java/com/growit/app/{domain}/
```

각 도메인 디렉토리 구조를 확인하여 관련 Aggregate Root를 파악한다.

### Step 2: 현재 상태 분석

영향받는 각 도메인에 대해:

**엔티티 분석**
- Domain Entity (Aggregate Root): `domain/{aggregate}/` 하위 파일
- JPA Entity: `infrastructure/persistence/**/entity/` 하위 파일
- Value Objects: `domain/**/vo/` 하위 파일

**API 엔드포인트 분석**
- Controller: `controller/` 하위 파일
- Request/Response DTO: `controller/dto/` 하위 파일
- 현재 API 경로 및 HTTP 메서드 확인

**DB 스키마 분석**
- Flyway 마이그레이션: `app/src/main/resources/db/migration/` 하위 `.sql` 파일
- 최신 마이그레이션 버전 번호 확인
- 관련 테이블 구조 파악

**Repository 분석**
- Domain Repository 인터페이스: `domain/{aggregate}/{Aggregate}Repository.java`
- JPA Repository: `infrastructure/persistence/**/source/DB*.java`
- QueryDSL Repository: `infrastructure/persistence/**/source/DB*QueryRepository.java`

### Step 3: 크로스 도메인 의존성 확인

- 다른 도메인의 Aggregate Root ID를 참조하는 곳 확인
- UseCase에서 다른 도메인 Repository를 주입받는 곳 확인
- 공유 VO나 Enum 확인 (`common/` 패키지)

### Step 4: 기존 테스트 확인

```
테스트 경로: app/src/test/java/com/growit/app/{domain}/
```

- 관련 도메인의 기존 테스트 파일 목록
- Fixture 클래스 확인
- Fake Repository 확인
- Controller 테스트 (RestDocs) 확인

### Step 5: FE API 계약 확인

- 현재 API 응답 형태가 FE/APP에서 사용되는지 판단
- 변경 시 하위 호환성 깨짐 여부 평가

---

## 산출물

파일: `.research/{TICKET_ID}/research.md`

```markdown
# Research: {TICKET_ID} — {TICKET_TITLE}

## 요약
{한 줄 요약}

## 영향 도메인
- [ ] {domain1} — {영향 설명}
- [ ] {domain2} — {영향 설명}

## 현재 상태 분석

### 엔티티 & Aggregate Root
{관련 엔티티 목록, 필드, 관계}

### API 엔드포인트
{관련 엔드포인트 목록, 요청/응답 형태}

### DB 스키마
{관련 테이블 구조, 최신 마이그레이션 버전}

### Repository
{관련 Repository 목록, 주요 쿼리}

## 변경 영향 평가
- 신규 생성: {새로 만들어야 할 것}
- 수정: {기존 코드 변경사항}
- 삭제: {제거할 것}

## 리스크
- {리스크 항목 1}
- {리스크 항목 2}

## 다른 레포 의존성
- FE: {FE에 영향을 주는 API 변경사항}
- APP: {APP에 영향을 주는 API 변경사항}

## 참고 파일
- {중요 파일 경로 목록}
```

---

## 주의사항

- **코드를 수정하지 않는다.** 읽기 전용 분석만 수행.
- Flyway 마이그레이션 파일에서 최신 버전 번호를 반드시 확인한다.
- Aggregate Root 경계를 명확히 식별한다.
- 기존 패턴을 존중한다 — 새로운 패턴을 도입하지 않는다.
