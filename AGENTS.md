# AGENTS.md — stone-ocean

## What this project is

Spring Boot 3.5.4 REST API with JWT auth, GitHub OAuth2, MySQL (MyBatis-Plus), WebSocket + SSE, and file uploads. A voting/game social app. Java 21, Gradle build, Jenkins CI/CD to Docker on Alibaba Cloud.

## Setup requirements (you WILL fail without these)

- **Java 21** — toolchain is pinned in `build.gradle`; Gradle auto-downloads if missing
- **`.env` file** (root) — required for local dev. Variables consumed via `spring-dotenv`:
  - `DATASOURCE_URL`, `DATASOURCE_USR`, `DATASOURCE_PSW` — MySQL connection
  - `GITHUB_CLIENT_ID`, `GITHUB_CLIENT_SECRET` — OAuth2
  - `FILE_PATH` — file upload storage directory
- **RSA key pair** in `src/main/resources/keyPair/` — `app.key` + `app.pub`. Gitignored; Jenkins injects them in CI. For local dev, generate yourself:
  ```bash
  openssl genrsa -out src/main/resources/keyPair/app.key 2048
  openssl rsa -in src/main/resources/keyPair/app.key -pubout -out src/main/resources/keyPair/app.pub
  ```
- **MySQL** — app won't start without a reachable database. Schema SQL in `src/main/resources/db/` (createTable.sql, initData.sql)

## Build & test commands

```bash
./gradlew build -x test --no-daemon   # build, skip tests
./gradlew test --no-daemon             # run all tests (needs DB + key pair)
./gradlew bootRun                      # start local dev server
```

Tests are `@SpringBootTest(RANDOM_PORT)` and need the full environment (DB, keys, .env). Isolated unit tests don't exist yet — all tests are integration.

## Architecture (single module, no monorepo)

**Root package**: `com.example.stoneocean`

```
config/          — Spring config beans (Security, CORS, MybatisPlus pagination, interceptors, WebSocket)
controller/      — @RestController endpoints (11 controllers)
entity/          — domain models + dto/ + fishing/ sub-packages
  dto/           — request/response DTOs
  fishing/       — game-related entities (Game, GameType)
mapper/          — MyBatis-Plus mapper interfaces (extends BaseMapper<T>)
service/         — IXxxService interfaces
  impl/          — XxxServiceImpl (extends ServiceImpl<Mapper, Entity>)
Interceptor/     — Spring MVC interceptors (UPPERCASE package name — non-standard)
Util/            — validators & tools (UPPERCASE package name — non-standard)
```

**Entrypoint**: `StoneOceanApplication.java` — `@SpringBootApplication` + `@MapperScan("com.example.stoneocean.mapper")`

## Key conventions an agent would miss

- **Soft delete**: all tables use `deleted_time` column (NULL = alive). Queries use `queryWrapper.isNull("deleted_time")`, not a MyBatis-Plus logical-delete annotation.
- **Table naming**: `t_` prefix for base tables, `t_vote4fun_` for voting module, `t_fishing_` for game module. Entity `@TableName` maps explicitly.
- **Response wrapper**: every controller returns `ApiResponse<T>` (statusCode/message/data). `success()` → 200, `failed()` → 500.
- **Context path**: all endpoints are under `/api` (server.servlet.context-path). Internal `@RequestMapping` paths like `/rankList/page` become `/api/rankList/page` externally.
- **Auth pattern**: JWT (RSA key pair) via `oauth2ResourceServer`. User ID extracted from JWT claims: `((Jwt)auth.getCredentials()).getClaims().get("userId")` — note: uses `getCredentials()`, not `getPrincipal()`.
- **Package naming**: `Interceptor/` and `Util/` are uppercase — don't "fix" this to lowercase, it's intentional project style.
- **Dual transport**: WebSocket (`@ServerEndpoint("/ws/{userId}")`) and SSE/WebFlux (`/sse/webflux`). WebSocket uses `jakarta.websocket`, SSE uses `reactor.core.publisher.Flux`.
- **MyBatis-Plus mapper queries**: custom queries use `@Select`/`@Update` annotations on mapper interfaces. XML files in `src/main/resources/mapper/` exist but are empty shells — don't add XML queries, use annotations instead.
- **Constructor injection**: all controllers/services use constructor injection (no `@Autowired` on fields). Follow this pattern.
- **Active User service**: `DBUserDetailsManagerService` is the real implementation (extends `ServiceImpl`, implements `UserDetailsService`). `UserServiceImpl` has `@Service` commented out — it is dead code. **Do not add User features to UserServiceImpl.**
- **File upload**: `FileManagerController` stores files at `${FILE_PATH}` with a 1MB max-file-size limit. Files are renamed with UUID.

## Configuration notes

- **No Spring profiles** — single `application.yml` only. Environment differences are handled entirely via `.env` (local) and Jenkins credentials (CI). Don't create `application-dev.yml` or similar.
- All sensitive values in `application.yml` are `${...}` placeholders resolved from env vars via `spring-dotenv`.

## Gradle quirks

- Gradle wrapper uses **9.2.0-milestone-2** (not a stable release) with a **Tencent mirror** (`mirrors.cloud.tencent.com/gradle`). If the mirror is down, uncomment the official URL in `gradle-wrapper.properties`.
- `--no-daemon` is used in CI. Local dev can omit it for faster builds.
- Code generation (`mybatis-plus-generator` + `freemarker`) is `testAndDevelopmentOnly` — not in production classpath. The backup generator is in `src/test/.../CodeGenerator_bak.java`.

## CI/CD (Jenkins)

Pipeline: Build → Test → Deploy. See `jenkins/Jenkinsfile`.
- Test stage copies JWT key pair from Jenkins credentials into `src/main/resources/keyPair/`
- Deploy builds Docker image (openjdk:21), SCPs to Alibaba Cloud, runs container on port 8101 with volume mount for file storage

## What NOT to do

- Don't add MyBatis-Plus `@TableLogic` annotation for soft delete — this project uses manual `isNull("deleted_time")` queries
- Don't rename `Interceptor/` or `Util/` packages to lowercase
- Don't hardcode DB credentials — always use env vars / .env
- Don't put secrets in application.yml — all sensitive values are `${...}` placeholders
- Don't confuse Swagger version: this uses `io.swagger:swagger-annotations:1.6.16` (v1), not springdoc-openapi (v2/v3)
- Don't add features to `UserServiceImpl` — it's dead code (`@Service` commented out). Use `DBUserDetailsManagerService` instead
- Don't add MyBatis-Plus XML queries in `src/main/resources/mapper/` — those files are empty shells; use `@Select`/`@Update` annotations on mapper interfaces