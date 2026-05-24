# /pr — PR 생성 & GitHub 푸시 (BE)

## 개요

구현 완료된 코드를 GitHub에 PR로 생성한다.
**PR 생성 전 CI/CD 동일 체크를 반드시 실행하고, 실패 시 PR을 생성하지 않는다.**

---

## 입력

- `TICKET_ID`: 티켓 ID
- `TICKET_TITLE`: 티켓 제목
- `TICKET_TYPE`: feat | fix | modify

---

## 워크플로우

### Step 1: Pre-flight CI Gate (필수)

> **이 단계를 통과하지 못하면 PR을 생성하지 않는다.**
> GitHub Actions pr-ci.yml과 동일한 체크를 로컬에서 실행한다.

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE

# 1. Spotless 포맷 체크 (pr-ci.yml Step 6과 동일)
./gradlew spotlessCheck --init-script gradle/init.gradle.kts --no-configuration-cache

# 2. 테스트 실행 (pr-ci.yml Step 7과 동일)
./gradlew test --no-configuration-cache

# 3. 빌드 검증
./gradlew build --no-configuration-cache
```

#### 실패 시 처리

```
## PR 생성 차단

CI/CD Pre-flight 체크 실패:

| Check | Status | Detail |
|-------|--------|--------|
| spotlessCheck | FAIL | {상세} |
| test | PASS | - |
| build | FAIL | {상세} |

### 해결 방법
1. `./gradlew spotlessApply --init-script gradle/init.gradle.kts --no-configuration-cache`
2. {빌드 에러 수정 안내}

→ 수정 후 `/pr` 재실행하세요.
```

> **FAIL이 하나라도 있으면 여기서 중단. Step 2로 진행하지 않는다.**

---

### Step 2: 아키텍처 위반 Quick Check

> Gate 1 핵심 항목만 빠르게 재검증 (review에서 이미 수행했더라도 최종 확인)

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE

# Domain 계층 순수성 — Spring/JPA import 금지
VIOLATIONS=$(grep -rn "import jakarta.persistence\|import javax.persistence\|import org.springframework" app/src/main/java/com/growit/app/*/domain/ 2>/dev/null | wc -l)

if [ "$VIOLATIONS" -gt 0 ]; then
  echo "BLOCKED: Domain 계층에 금지된 import $VIOLATIONS건 발견"
  grep -rn "import jakarta.persistence\|import javax.persistence\|import org.springframework" app/src/main/java/com/growit/app/*/domain/
  exit 1
fi
```

> **위반 발견 시 PR 생성 차단. `/review`를 다시 실행하여 수정하도록 안내.**

---

### Step 3: 변경 사항 확인 & 커밋

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE

git status
git diff --name-only
git diff --cached --name-only
```

커밋되지 않은 변경이 있으면:

```bash
# 포맷 적용
./gradlew spotlessApply --init-script gradle/init.gradle.kts --no-configuration-cache

# 파일별 스테이징 (git add -A, git add . 금지)
git add {specific files}

# Conventional commit
git commit -m "{type}: {description}"
```

---

### Step 4: 리모트 브랜치 푸시

```bash
git branch --show-current
git push -u origin $(git branch --show-current)
```

---

### Step 5: PR 생성

```bash
gh pr create \
  --title "{type}: {TICKET_TITLE}" \
  --body "$(cat <<'EOF'
## Summary

{변경 사항 요약 (1-3 bullet points)}

## Affected Domains

{영향받는 도메인 목록}

## Changes

### New Files
{신규 파일 목록}

### Modified Files
{수정 파일 목록}

## Pre-merge CI Verification

| Check | Status |
|-------|--------|
| spotlessCheck | PASS |
| test | PASS |
| build | PASS |
| Architecture | PASS (Domain layer 순수성 검증 완료) |

## Related

- Ticket: {TICKET_ID}
- FE Impact: {있으면 기술, 없으면 None}
- APP Impact: {있으면 기술, 없으면 None}
EOF
)"
```

---

### Step 6: 결과 보고

```
## PR 생성 완료

| 항목 | 값 |
|------|------|
| PR URL | {pr-url} |
| Branch | {branch-name} |
| Title | {pr-title} |
| Affected Domains | {domains} |

### Pre-merge CI Results
- spotlessCheck: PASS
- test: PASS ({N} tests passed)
- build: PASS
- Architecture: PASS
```

---

## 주의사항

- **CI Pre-flight 실패 시 PR을 절대 생성하지 않는다.**
- **아키텍처 위반 발견 시 PR을 절대 생성하지 않는다.**
- PR 제목은 conventional commit 형식: `{feat|fix|modify}: {description}`
- `git add -A` 또는 `git add .` 를 사용하지 않는다.
- PR body에 CI 검증 결과를 반드시 포함한다.
- FE/APP에 영향을 주는 API 변경이 있으면 명시한다.
