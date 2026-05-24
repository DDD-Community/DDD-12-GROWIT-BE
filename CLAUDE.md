# CLAUDE.md — DDD-12-GROWIT-BE (Backend)

## 역할

GROWIT 백엔드 API 서버. Spring Boot 3.4.5 기반, DDD + Aggregate Root 아키텍처.

- RESTful API 제공 (FE/APP 클라이언트)
- 도메인 비즈니스 로직 처리
- OAuth2 인증 (Kakao, Apple) + JWT
- PostgreSQL 데이터 관리

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.4.5, Spring Security, Spring Batch |
| DB | PostgreSQL, Flyway (마이그레이션) |
| ORM | JPA, QueryDSL |
| Cache | Caffeine Cache |
| Auth | OAuth2 (Kakao, Apple) + JWT |
| Docs | RestDocs + OpenAPI3 + Swagger |
| Build | Gradle 8.x (version catalog) |
| Format | Google Java Format (Spotless), 2-space indent, LF |
| Test | JUnit 5, Fake Repository, Fixtures, AAA 패턴 |

---

## 아키텍처: DDD + Aggregate Root 기반

### 계층 구조

```
Controller → UseCase → Domain ← Infrastructure
```

- **Controller**: REST 엔드포인트, DTO 변환. 비즈니스 로직 없음.
- **UseCase**: 트랜잭션 경계, 비즈니스 액션 조합 (얇은 계층)
- **Domain**: Aggregate Root, Entity, Repository 인터페이스, Service, VO, Command/Query DTO. **순수 — 인프라 의존성 없음.**
- **Infrastructure**: Repository 구현체, JPA Entity, DB Mapper, 외부 클라이언트

### Aggregate Root 규칙

1. 각 도메인은 Aggregate Root 단위로 관리
2. Aggregate Root 단위 디렉토리 하나에 controller, service(usecase), repository 포함
3. 외부 도메인은 Aggregate Root의 **ID로만** 참조 (Entity 직접 참조 금지)
4. Aggregate Root 내부 상태 변경은 Aggregate Root의 메서드를 통해서만 가능
5. 하나의 트랜잭션은 하나의 Aggregate Root만 수정

---

## 패키지 구조

```
com.growit.app.{domain}/
├── controller/          ← REST 엔드포인트
│   ├── dto/request/     ← API 요청 DTO
│   ├── dto/response/    ← API 응답 DTO
│   └── mapper/          ← DTO <-> Command 변환
├── usecase/             ← 비즈니스 액션 (트랜잭션 경계)
│   └── dto/             ← UseCase 간 전달 DTO
├── domain/
│   └── {aggregate-root}/
│       ├── {AggregateRoot}.java          ← Aggregate Root Entity
│       ├── {AggregateRoot}Repository.java ← Repository 인터페이스
│       ├── service/     ← Domain Service, Validator, Query
│       ├── dto/         ← Command/Query DTO
│       └── vo/          ← Value Objects
└── infrastructure/
    ├── persistence/
    │   └── {aggregate-root}/
    │       ├── {AggregateRoot}RepositoryImpl.java
    │       ├── {AggregateRoot}DBMapper.java
    │       └── source/
    │           ├── DB{AggregateRoot}Repository.java     ← JPA Repository
    │           ├── DB{AggregateRoot}QueryRepository.java
    │           └── entity/
    │               └── {AggregateRoot}Entity.java       ← JPA @Entity
    └── client/          ← 외부 API 클라이언트
```

---

## 도메인 목록

| 도메인 | Aggregate Root | 하위 Aggregate | 설명 |
|--------|---------------|----------------|------|
| goal | Goal | Planet, GoalAnalysis | 목표 관리 (행성 시스템) |
| todo | ToDo | Routine | 할 일 / 루틴 관리 |
| user | User | UserToken, Promotion, UserAdviceStatus, UserStats | 인증, OAuth, 통계 |
| advice | ChatAdvice, MentorAdvice | Grorong | AI 조언 (멘토, 채팅, 사주, 그로롱) |
| retrospect | Retrospect | GoalRetrospect | 주간 회고, KPT |
| mission | Mission | - | 미션 트래킹 |
| resource | Saying | - | 명언, 초대, Discord |
| common | - | - | 설정, 예외, 유틸리티 |

---

## 커맨드 (Commands)

growit-lead 오케스트레이터에서 위임받아 실행하는 커맨드.

| 커맨드 | 역할 | 상세 |
|--------|------|------|
| `/research` | 요구사항 분석 & 변경 영향도 파악 | `.ai/commands/research.md` |
| `/plan` | 구현 계획 수립 | `.ai/commands/plan.md` |
| `/implement` | 코드 구현 실행 | `.ai/commands/implement.md` |
| `/review` | 품질 게이트 리뷰 | `.ai/commands/review.md` |
| `/pr` | PR 생성 & GitHub 푸시 | `.ai/commands/pr.md` |
| `/deploy-local` | 로컬 개발 서버 기동 | `.ai/commands/deploy-local.md` |
| `/deploy-dev` | DEV 환경 배포 | `.ai/commands/deploy-dev.md` |
| `/deploy-prod` | PROD 환경 배포 | `.ai/commands/deploy-prod.md` |

---

## 규칙 (Rules)

| 규칙 | 파일 | 용도 |
|------|------|------|
| **Architecture** | `.ai/rules/architecture.md` | DDD + Aggregate Root 아키텍처 규칙 |
| **Coding Standards** | `.ai/rules/coding-standards.md` | 코딩 컨벤션 & 스타일 가이드 |

---

## 산출물 경로

| 산출물 | 경로 | git |
|--------|------|-----|
| 리서치 결과 | `.research/{TICKET_ID}/research.md` | gitignored |
| 구현 계획 | `.plan/{TICKET_ID}/plan.md` | gitignored |

---

## GitHub

- Repository: `DDD-Community/DDD-12-GROWIT-BE`
- CI: `pr-ci.yml` (spotless + test)
- CD DEV: `cd-dev.yml` (ECR -> ECS)
- CD PROD: `cd.yml` (tag push)
- Release: `release.yml`

---

## 브랜치 네이밍

```
{feat|fix|modify}/{TICKET_ID}-{kebab-summary}
```

모든 repo에서 **동일한 브랜치명** 사용.

---

## 커밋 규칙

- 커밋 메시지는 **영어**, conventional commit 형식
- `git add`는 파일 지정 (`-A`, `.` 금지)

---

## .ai 디렉토리 구조

```
.ai/
├── commands/           ← 슬래시 커맨드 정의 (SSOT)
│   ├── _meta.yaml       (도구 권한, description)
│   └── *.md             (커맨드별 워크플로우)
├── rules/              ← 프로젝트 규칙
│   ├── _meta.yaml
│   └── *.md
└── sync.sh → sync.py  ← .ai/ → .cursor/ + .claude/skills/ 동기화
```

동기화: `bash .ai/sync.sh`
- `.cursor/commands/` — Cursor IDE 커맨드
- `.cursor/rules/` — Cursor IDE 규칙
- `.claude/skills/` — Claude Code SKILL.md
