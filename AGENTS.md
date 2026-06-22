# Repository Guidelines

## Project Structure & Module Organization

This workspace is split into two top-level projects:

- `kevin-server/`: backend Spring Boot / RuoYi multi-module Maven project.
- `kevin-web/`: frontend Vue 2 / Element UI project.

Backend modules live under `kevin-server/`: `ruoyi-admin` is the Spring Boot entrypoint, `ruoyi-framework` holds web/security configuration, `ruoyi-system` contains business logic and MyBatis mappers, `ruoyi-quartz` contains jobs, `ruoyi-generator` contains code generation, and `ruoyi-common` contains utilities. SQL bootstrap files live in `kevin-server/sql/`. Frontend source lives in `kevin-web/src/`, with static assets under `kevin-web/public/`.

## Build, Test, and Development Commands

Run backend commands from `kevin-server/`.

- `mvn clean package`: builds all Java modules and packages the backend.
- `mvn test`: runs Maven tests for all modules.
- `mvn -pl ruoyi-admin spring-boot:run`: starts the backend entrypoint locally.

Run frontend commands from `kevin-web/`.

- `npm install`: installs frontend dependencies.
- `npm run dev`: starts the Vue CLI dev server; it proxies API calls to `http://localhost:8080`.
- `npm run build:prod`: creates the production frontend bundle in `dist/`.

## Coding Style & Naming Conventions

Use Java 17 and keep backend package names under `com.ruoyi`. Follow the existing Java brace style where class and method braces open on the next line, and use suffixes such as `Controller`, `Service`, `ServiceImpl`, `Mapper`, and `Domain`. Keep MyBatis XML names aligned with Java mapper interfaces. Frontend code uses Vue 2, Vuex, Element UI, `@` alias imports, single quotes, and two-space indentation.

## Testing Guidelines

There are currently no checked-in `src/test` files. Add Java tests under the matching backend module's `src/test/java` package when changing behavior, and name them after the class or feature under test, for example `SysUserServiceTest`. Run `mvn test` before backend changes. For frontend changes, at minimum run `npm run build:prod`.

## Commit & Pull Request Guidelines

Recent history uses short, imperative Chinese summaries, for example `优化代码` or `升级项目相关依赖到最新`. Keep commits focused and describe the user-visible change. Pull requests should include a summary, affected modules, database or configuration changes, verification steps, and screenshots for UI changes. Do not commit generated output such as `target/`, `dist/`, logs, or local upload files.

## Security & Configuration Tips

Do not commit real database passwords, Redis credentials, JWT secrets, or environment-specific upload paths. Keep local overrides outside tracked files, and document required SQL migrations from `kevin-server/sql/` when schema changes are needed.

## Agent-Specific Instructions

必须使用中文回复用户，除非用户明确要求使用其他语言。
