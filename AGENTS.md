# Repository Guidelines

## Project Structure & Module Organization

This workspace is split into two top-level projects:

- `kevin-server/`: backend Spring Boot / RuoYi multi-module Maven project.
- `kevin-web/`: frontend Vue 2 / Element UI project.
- `kevin-doc/`: requirement and demo documentation for the virtual simulation training platform.

Backend modules live under `kevin-server/`: `ruoyi-admin` is the Spring Boot entrypoint and controller layer, `ruoyi-framework` holds web/security configuration, `ruoyi-system` contains business logic and MyBatis mappers, `ruoyi-quartz` contains jobs, `ruoyi-generator` contains code generation, and `ruoyi-common` contains utilities. SQL bootstrap files live in `kevin-server/sql/`.

The current business feature is the virtual simulation training platform. Backend controllers are in `kevin-server/ruoyi-admin/src/main/java/com/ruoyi/web/controller/virtual/`; domain, service, and mapper code is under `kevin-server/ruoyi-system/src/main/java/com/ruoyi/system/` using the `virtualdomain`, `virtualservice`, and `virtualmapper` packages; MyBatis XML files are in `kevin-server/ruoyi-system/src/main/resources/mapper/virtual/`. Frontend virtual simulation APIs live in `kevin-web/src/api/virtual/`, portal APIs in `kevin-web/src/api/portal/`, and pages in `kevin-web/src/views/virtual/`. Frontend static assets live under `kevin-web/public/`.

## Build, Test, and Development Commands

Run backend commands from `kevin-server/`.

- `mvn clean package`: builds all Java modules and packages the backend.
- `mvn test`: runs Maven tests for all modules.
- `mvn -pl ruoyi-admin test`: runs the backend entrypoint module tests, including the virtual controller smoke test.
- `mvn -pl ruoyi-admin spring-boot:run`: starts the backend entrypoint locally.

Run frontend commands from `kevin-web/`.

- `npm install`: installs frontend dependencies.
- `npm run dev`: starts the Vue CLI dev server; it proxies API calls to `http://localhost:8080`.
- `npm run build:prod`: creates the production frontend bundle in `dist/`.

Local demo setup is documented in `kevin-doc/demo-guide.md`: create the MySQL database `ry-vue`, import `kevin-server/sql/ry_20260417.sql` and `kevin-server/sql/quartz.sql`, start Redis on `localhost:6379`, start the backend on port `8080`, then start the frontend. The development API prefix is `/dev-api` and is proxied by `kevin-web/vue.config.js`.

## Coding Style & Naming Conventions

Use Java 8, matching `kevin-server/pom.xml`, and keep backend package names under `com.ruoyi`. Follow the existing Java brace style where class and method braces open on the next line, and use suffixes such as `Controller`, `Service`, `ServiceImpl`, `Mapper`, and domain names. Keep MyBatis XML names aligned with Java mapper interfaces. For virtual simulation backend changes, keep the existing `Vt*` class naming and the `virtualdomain` / `virtualservice` / `virtualmapper` package split unless a broader refactor is requested.

Frontend code uses Vue 2, Vuex, Element UI, `@` alias imports, single quotes, and two-space indentation. Keep request modules under `src/api/` aligned with page modules under `src/views/`.

## Testing Guidelines

There is currently a checked-in virtual controller smoke test at `kevin-server/ruoyi-admin/src/test/java/com/ruoyi/web/controller/virtual/VirtualControllerSmokeTest.java`. Add Java tests under the matching backend module's `src/test/java` package when changing behavior, and name them after the class or feature under test, for example `SysUserServiceTest` or `VtDashboardServiceTest`. Run `mvn test` before backend changes when practical; for entrypoint-only backend checks, run `mvn -pl ruoyi-admin test`. For frontend changes, at minimum run `npm run build:prod`.

## Commit & Pull Request Guidelines

Recent history uses short, imperative Chinese summaries, for example `优化代码` or `升级项目相关依赖到最新`. Keep commits focused and describe the user-visible change. Pull requests should include a summary, affected modules, database or configuration changes, verification steps, and screenshots for UI changes. Do not commit generated output such as `target/`, `dist/`, logs, or local upload files.

## Security & Configuration Tips

Do not commit real database passwords, Redis credentials, JWT secrets, or environment-specific upload paths. Existing demo values in `application-druid.yml` and `application.yml` are for local RuoYi-style setup; keep real overrides outside tracked files. When schema changes are needed, update and document the required SQL under `kevin-server/sql/`, especially virtual simulation tables such as `vt_lab`, `vt_device`, `vt_resource`, `vt_share_apply`, `vt_course`, `vt_experiment`, `vt_teaching_plan`, and `vt_training_record`.

## Agent-Specific Instructions

必须使用中文回复用户，除非用户明确要求使用其他语言。
