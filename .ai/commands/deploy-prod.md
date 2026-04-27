# /deploy-prod — PROD 환경 배포

## 개요

릴리스 태그를 생성하여 PROD 환경 배포를 트리거한다.
GitHub Actions `cd.yml`이 tag push 이벤트로 자동 실행된다.

---

## 워크플로우

### Step 1: 사전 검증

```bash
# main 브랜치 확인
git checkout main
git pull origin main

# DEV 배포가 성공했는지 확인
gh run list --workflow=cd-dev.yml --limit=1

# 최신 테스트 통과 확인
./gradlew test
```

**PROD 배포 전 반드시 확인:**
- [ ] DEV 환경 배포 성공
- [ ] DEV 환경 QA 통과
- [ ] 테스트 전체 통과

### Step 2: 버전 결정

```bash
# 기존 태그 확인
git tag --sort=-v:refname | head -5
```

버전 규칙:
- `v{MAJOR}.{MINOR}.{PATCH}`
- feat → MINOR 증가
- fix → PATCH 증가
- BREAKING CHANGE → MAJOR 증가

### Step 3: 릴리스 태그 생성

```bash
# 태그 생성
git tag v{VERSION}

# 태그 푸시
git push origin v{VERSION}
```

### Step 4: PROD 배포 모니터링

태그 푸시 후 `cd.yml` GitHub Actions가 자동 실행된다.

```bash
# workflow run 확인
gh run list --workflow=cd.yml --limit=1

# 실행 상태 확인
gh run view {run-id}

# 실시간 로그 확인
gh run watch {run-id}
```

### Step 5: GitHub Release 생성 (선택)

```bash
gh release create v{VERSION} \
  --title "v{VERSION}" \
  --notes "$(cat <<'EOF'
## Changes
- {변경사항 1}
- {변경사항 2}

## Affected Domains
- {도메인 목록}
EOF
)"
```

### Step 6: 배포 결과 보고

```
Version: v{VERSION}
Tag: v{VERSION}
Workflow: cd.yml
Run: {run-url}
Status: {success | failure}
```

---

## 롤백 절차

PROD 배포 후 문제 발생 시:

```bash
# 이전 버전 태그로 재배포
# 방법 1: ECS 서비스에서 이전 task definition으로 롤백
# 방법 2: 이전 버전 Docker 이미지로 ECS 업데이트

# 핫픽스가 필요한 경우
git checkout main
git checkout -b fix/{TICKET_ID}-hotfix-description
# 수정 후 PR → merge → 태그
```

---

## 주의사항

- DEV 배포 성공 없이 PROD 배포하지 않는다.
- 태그는 main 브랜치에서만 생성한다.
- 롤백 계획을 항상 확인한 후 배포한다.
