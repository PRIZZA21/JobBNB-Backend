# Implementation Plan: Deployment Guide

This plan outlines the steps to deploy the JobBNB Backend to a production environment.

## 1. Prerequisites
- [x] Push code to GitHub (Done: `PRIZZA21/JobBNB-Backend`)
- [x] Database: NeonDB PostgreSQL (Done & Configured)
- [x] Config: Environment variable support for `JWT_SECRET` (Done)

## 2. Recommended Platform: Render / Railway
These platforms are the easiest for Spring Boot Gradle projects.

### Step A: Preparation
- Ensure `build.gradle` has `id 'org.springframework.boot'` and `id 'io.spring.dependency-management'`.
- The entry point should be clear (using `bootJar`).

### Step B: Deployment Process (Render Example)
1. **Create New Web Service**: Link your GitHub repository.
2. **Runtime**: Select `Docker` or `Java`.
   - If Java:
     - **Build Command**: `./gradlew build -x test`
     - **Start Command**: `java -jar build/libs/dev-0.0.1-SNAPSHOT.jar`
3. **Environment Variables**: Add the following:
   - `JWT_SECRET`: (Your secure prod secret)
   - `SPRING_DATASOURCE_URL`: (Your NeonDB URL)
   - `SPRING_DATASOURCE_USERNAME`: (Your NeonDB Username)
   - `SPRING_DATASOURCE_PASSWORD`: (Your NeonDB Password)

## 3. Post-Deployment Checks
- [ ] Verify logs show Flyway migrations running successfully.
- [ ] Test the `/api/health` or `GET /api/jobs` endpoint.
- [ ] Ensure CORS policy includes the frontend production domain.

## 4. Alternative: Docker Deployment
- Create a `Dockerfile`.
- Build image: `docker build -t jobbnb-backend .`.
- Push to Docker Hub or AWS ECR.
