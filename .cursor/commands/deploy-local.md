# /deploy-local — 로컬 개발 서버 기동

## 개요

로컬 환경에서 Spring Boot 개발 서버를 기동한다.

---

## 워크플로우

### Step 1: PostgreSQL 확인

```bash
# PostgreSQL이 실행 중인지 확인
pg_isready -h localhost -p 5432
```

실행 중이 아니면:
```bash
# macOS (Homebrew)
brew services start postgresql@16
```

### Step 2: 데이터베이스 확인

```bash
# growit_dev 데이터베이스 존재 확인
psql -h localhost -U postgres -c "SELECT datname FROM pg_database WHERE datname = 'growit_dev';"
```

없으면:
```bash
createdb -h localhost -U postgres growit_dev
```

### Step 3: 환경 변수 확인

필요한 환경 변수 또는 application-local.yml 설정:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/growit_dev
spring.datasource.username=postgres
spring.datasource.password={local_password}
spring.profiles.active=local
```

### Step 4: 서버 기동

```bash
cd /Users/sagwangjin/Desktop/growit/DDD-12-GROWIT-BE && ./gradlew bootRun --args='--spring.profiles.active=local'
```

### Step 5: Health Check

서버가 기동되면:

```bash
curl -s http://localhost:8081/actuator/health | python3 -m json.tool
```

기대 응답:
```json
{
  "status": "UP"
}
```

### Step 6: 상태 보고

```
Server: http://localhost:8081
Profile: local
Database: postgresql://localhost:5432/growit_dev
Health: UP
Swagger: http://localhost:8081/swagger-ui/index.html
```

---

## 트러블슈팅

| 증상 | 원인 | 해결 |
|------|------|------|
| Port 8081 already in use | 다른 프로세스 사용 중 | `lsof -i :8081` 후 프로세스 종료 |
| DB connection refused | PostgreSQL 미실행 | `brew services start postgresql@16` |
| Flyway migration failed | 마이그레이션 충돌 | `./gradlew flywayRepair` 후 재시도 |
| Build failed | 의존성 문제 | `./gradlew clean build --refresh-dependencies` |
