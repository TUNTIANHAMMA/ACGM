---

Always response in Chineses

Before each git commit set user.name to CuteHamma and user.email to codex@example.com, then after committing restore user.name to TUNTIANHAMMA and user.email to 2649483064@qq.com.

---

# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java/com/acgm/acgmmediatracker` holds Spring controllers, services, entities, and MyBatis mappers; keep layers separated to preserve clear data flow.
- `src/main/resources/mapper` stores XML mapper definitions paired with interfaces in `mapper/`; update both whenever a query signature changes.
- Configuration files (e.g., `application.properties`) and SQL templates live under `src/main/resources`; never hard-code credentials in Java classes.
- Tests mirror the production package tree in `src/test/java/com/acgm/acgmmediatracker`, enabling Spring’s component scanning to find fixtures automatically.

## Build, Test, and Development Commands
- `./mvnw clean verify` performs a full compile, unit-test run, and plugin checks; run before pushing.
- `./mvnw spring-boot:run` launches the API with hot reload via DevTools—ideal for local iteration.
- `./mvnw test -Dspring.profiles.active=test` executes the JUnit/Spring Boot suite against the test profile.
- `./mvnw package -DskipTests` creates an executable JAR in `target/` when you only need an artifact.

## Coding Style & Naming Conventions
- Target Java 17, 4-space indentation, and Lombok annotations (`@Data`, `@Builder`) to reduce boilerplate.
- Controllers end with `Controller`, services with `Service`, and mapper interfaces with `Mapper`; align XML file names the same way.
- Favor constructor injection, `ResponseEntity` responses, and DTOs that live under `entity/` or a dedicated `dto/` package if added.

## Testing Guidelines
- Use `@SpringBootTest` for integration coverage and Mockito-based slices for unit logic when practical.
- Name tests after the behavior under test (e.g., `MediaControllerTests.fetchItemsReturns200`).
- Add regression tests adjacent to the class under change, and keep coverage high on mapper SQL since MyBatis errors surface at runtime.
- Never set database-managed columns (e.g., `users.email_norm`, `media_items.finish_month`, auto timestamps) inside Java code or tests; rely on MySQL defaults/GENERATED columns instead.

## Commit & Pull Request Guidelines
- Follow the existing imperative style (`Initial commit`): short subject (<72 chars) plus optional detail body.
- Reference issues in the subject or body (`[#123] Fix media status filter`).
- PRs must describe the change, validation steps (`./mvnw clean verify` output), and screenshots for user-facing APIs if applicable.

## Security & Configuration Tips
- Store database secrets in environment variables or profile-specific property files excluded from VCS.
- Default `application.properties` is safe for local H2/MySQL stubs; provide sanitized examples when updating connection info.
