jobbnb-backend
│
├── src/main/java/com/jobbnb
│   │
│   ├── JobbnbApplication.java
│   │
│   ├── common
│   │   ├── config
│   │   │   ├── SecurityConfig.java
│   │   │   ├── JwtConfig.java
│   │   │   ├── WebConfig.java
│   │   │   └── RedisConfig.java
│   │   │
│   │   ├── exception
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── ApiError.java
│   │   │   └── BusinessException.java
│   │   │
│   │   ├── response
│   │   │   ├── ApiResponse.java
│   │   │   └── PageResponse.java
│   │   │
│   │   ├── util
│   │   │   ├── JwtUtil.java
│   │   │   ├── DateUtil.java
│   │   │   └── FileUtil.java
│   │   │
│   │   └── event
│   │       ├── DomainEvent.java
│   │       └── EventPublisher.java
│   │
│   ├── auth
│   │   ├── controller
│   │   │   └── AuthController.java
│   │   │
│   │   ├── service
│   │   │   └── AuthService.java
│   │   │
│   │   ├── security
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   └── CustomUserDetailsService.java
│   │   │
│   │   ├── dto
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   └── AuthResponse.java
│   │   │
│   │   └── model
│   │       └── RefreshToken.java
│   │
│   ├── user
│   │   ├── controller
│   │   │   └── UserController.java
│   │   │
│   │   ├── service
│   │   │   └── UserService.java
│   │   │
│   │   ├── repository
│   │   │   └── UserRepository.java
│   │   │
│   │   ├── dto
│   │   │   ├── UserResponse.java
│   │   │   └── UpdateProfileRequest.java
│   │   │
│   │   └── model
│   │       ├── User.java
│   │       └── Role.java
│   │
│   ├── profile
│   │   ├── controller
│   │   │   └── ProfileController.java
│   │   │
│   │   ├── service
│   │   │   └── ProfileService.java
│   │   │
│   │   ├── repository
│   │   │   ├── CandidateProfileRepository.java
│   │   │   └── CompanyProfileRepository.java
│   │   │
│   │   ├── model
│   │   │   ├── CandidateProfile.java
│   │   │   └── CompanyProfile.java
│   │   │
│   │   └── dto
│   │       └── ProfileResponse.java
│   │
│   ├── job
│   │   ├── controller
│   │   │   └── JobController.java
│   │   │
│   │   ├── service
│   │   │   └── JobService.java
│   │   │
│   │   ├── repository
│   │   │   └── JobRepository.java
│   │   │
│   │   ├── dto
│   │   │   ├── CreateJobRequest.java
│   │   │   ├── JobResponse.java
│   │   │   └── JobSearchRequest.java
│   │   │
│   │   └── model
│   │       ├── Job.java
│   │       ├── JobStatus.java
│   │       └── JobType.java
│   │
│   ├── application
│   │   ├── controller
│   │   │   └── ApplicationController.java
│   │   │
│   │   ├── service
│   │   │   └── ApplicationService.java
│   │   │
│   │   ├── repository
│   │   │   └── JobApplicationRepository.java
│   │   │
│   │   ├── dto
│   │   │   └── ApplicationResponse.java
│   │   │
│   │   └── model
│   │       ├── JobApplication.java
│   │       └── ApplicationStatus.java
│   │
│   ├── notification
│   │   ├── service
│   │   │   └── NotificationService.java
│   │   │
│   │   ├── listener
│   │   │   └── JobAppliedListener.java
│   │   │
│   │   └── model
│   │       └── Notification.java
│   │
│   ├── messaging          (Phase 2)
│   │   ├── controller
│   │   ├── service
│   │   ├── model
│   │   └── websocket
│   │
│   └── admin
│       ├── controller
│       └── service
│
└── src/main/resources
    ├── application.yml
    ├── application-dev.yml
    ├── application-prod.yml
    └── db/migration
