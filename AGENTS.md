# Repository Guidelines

## Project Structure & Module Organization

This repository contains the backend portion of the Kevin project. Backend code is a multi-module Maven project: `ruoyi-admin` is the Spring Boot entrypoint, `ruoyi-framework` holds web/security configuration, `ruoyi-system` contains business logic and MyBatis mappers, `ruoyi-quartz` contains scheduled jobs, `ruoyi-generator` contains code generation, and `ruoyi-common` contains shared utilities. Backend resources live under each module's `src/main/resources`; main configuration is in `ruoyi-admin/src/main/resources/application.yml` and `application-druid.yml`. SQL bootstrap files are in `sql/`. The Vue 2 frontend now lives in the sibling workspace directory `../kevin-web/`.

## Build, Test, and Development Commands

Run backend commands from this directory.

- `mvn clean package`: builds all Java modules and packages the backend.
- `mvn test`: runs Maven tests for all modules.
- `mvn -pl ruoyi-admin spring-boot:run`: starts the backend locally.
- `cd ../kevin-web && npm install`: installs frontend dependencies.
- `cd ../kevin-web && npm run dev`: starts the Vue CLI dev server; API calls proxy to `http://localhost:8080`.
- `cd ../kevin-web && npm run build:prod`: creates the production frontend bundle in `dist/`.

## Coding Style & Naming Conventions

Use Java 8 and keep package names under `com.ruoyi`. Follow the existing Java brace style where class and method braces open on the next line, and use suffixes such as `Controller`, `Service`, `ServiceImpl`, `Mapper`, and `Domain`. Keep MyBatis XML names aligned with Java mapper interfaces. Frontend code uses Vue 2, Vuex, Element UI, `@` alias imports, single quotes, and two-space indentation.

## Testing Guidelines

There are currently no checked-in `src/test` files. Add Java tests under the matching module's `src/test/java` package when changing backend behavior, and name them after the class or feature under test, for example `SysUserServiceTest`. Run `mvn test` before submitting backend changes. For frontend changes, at minimum run `npm run build:prod`.

## Commit & Pull Request Guidelines

Recent history uses short, imperative Chinese summaries, for example `优化代码` or `升级项目相关依赖到最新`. Keep commits focused and describe the user-visible change. Pull requests should include a summary, affected modules, database or configuration changes, verification steps, and screenshots for UI changes. Do not commit generated output such as `target/`, `dist/`, logs, or local upload files.

## Security & Configuration Tips

Do not commit real database passwords, Redis credentials, JWT secrets, or environment-specific upload paths. Keep local overrides outside tracked files, and document required SQL migrations from `sql/` when schema changes are needed.
