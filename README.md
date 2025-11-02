## Architecture Angular + Spring Boot

This repository now contains a full-stack example that wires a modern Angular front-end with a Spring Boot back-end. The goal is to demonstrate Google OAuth2/OpenID Connect authentication and fine-grained role-based authorization across both tiers.

### Front-end (`frontend/`)

* Angular 16 application structured by domains (`app/core`, `app/features`, `app/shared`, `app/state`).
* Google login powered by [`angular-oauth2-oidc`](https://github.com/manfredsteyer/angular-oauth2-oidc) configured for the authorization code + PKCE flow.
* Guards (`AuthGuard`, `RoleGuard`) protect the routing, while an HTTP interceptor transparently injects the API bearer token.
* Feature modules showcase role-aware navigation (`admin`, `users`, `dashboard`). Shared UI pieces such as `RoleBadgeComponent` make it easy to reuse visuals across the app.

### Back-end (`backend/`)

* Spring Boot 3 resource server validating Google JWTs through a custom `GoogleTokenVerifier`.
* Security rules defined with Spring Security (stateless sessions, method-level `@PreAuthorize`) plus configurable CORS support.
* Persistence layer using Spring Data JPA with an in-memory H2 database that stores synchronized users and roles (defaulting to the `USER` role when Google claims are absent).
* REST endpoints:
  * `POST /api/v1/auth/google` validates the Google ID token, persists the user, and returns application roles.
  * `GET /api/v1/users` & `GET /api/v1/admin/status` restricted to `ADMIN`.

### Development

* Install Angular dependencies inside `frontend` (`npm install`) and use `npm start` to run the SPA.
* Build or run the Spring Boot API from `backend` (`./mvnw spring-boot:run` or `mvn spring-boot:run`).
* Execute `./mvnw test` to run backend persistence tests that cover role synchronization logic.
* Update `backend/src/main/resources/application.yml` to configure allowed CORS origins or Google issuer/JWKS overrides.
* Update the Google OAuth client id in `frontend/src/environments/*` and configure allowed origins in the Google console.

The original AngularJS ES6 sample remains untouched under `src/` as a reference.

License
=======

This project is licensed under public domain or [CC0](https://creativecommons.org/publicdomain/zero/1.0/).
