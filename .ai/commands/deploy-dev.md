# /deploy-dev — DEV 환경 배포

## 개요

PR을 main 브랜치에 머지하여 DEV 환경 배포를 트리거한다.
GitHub Actions `cd-dev.yml`이 main push 이벤트로 자동 실행된다.

---

## 워크플로우

### Step 1: PR 상태 확인

```bash
# 현재 브랜치의 PR 확인
gh pr status

# PR의 CI 체크 확인
gh pr checks
```

- PR이 존재하는지 확인
- CI 체크 (pr-ci.yml)가 통과했는지 확인
- 리뷰 승인이 필요한 경우 확인

### Step 2: CI 체크 대기

```bash
# CI 체크가 아직 실행 중이면 대기
gh pr checks --watch
```

모든 체크가 통과해야 머지 가능.

### Step 3: PR 머지

```bash
# Squash merge + 브랜치 삭제
gh pr merge --squash --delete-branch
```

### Step 4: DEV 배포 모니터링

main에 머지되면 `cd-dev.yml` GitHub Actions가 자동 실행된다.

```bash
# 최신 workflow run 확인
gh run list --workflow=cd-dev.yml --limit=1

# 실행 상태 확인
gh run view {run-id}

# 실시간 로그 확인 (필요 시)
gh run watch {run-id}
```

### Step 5: 배포 결과 보고

```
Branch: main
Merge: squash
Workflow: cd-dev.yml
Run: {run-url}
Status: {success | failure}
```

---

## 배포 파이프라인 (cd-dev.yml)

```
main push → Build → Docker Image → ECR Push → ECS Deploy
```

1. Gradle build
2. Docker 이미지 빌드
3. AWS ECR에 이미지 푸시
4. AWS ECS 서비스 업데이트 (rolling deployment)

---

## 실패 시 대응

| 단계 | 실패 원인 | 대응 |
|------|----------|------|
| Build | 컴파일/테스트 에러 | 로컬에서 재현 후 fix 커밋 |
| Docker | Dockerfile 문제 | Dockerfile 확인 |
| ECR Push | 인증 문제 | AWS 자격 증명 확인 |
| ECS Deploy | 헬스체크 실패 | 로그 확인, 롤백 검토 |
