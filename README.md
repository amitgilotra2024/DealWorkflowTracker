                              [ Client / Frontend ]
                                        │
                                        │ (HTTP REST / Bearer Token)
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           SPRING BOOT 3 BACKEND SYSTEM                          │
│                                                                                 │
│   ┌─────────────────────────────────────────────────────────────────────────┐   │
│   │                         Presentation Layer                              │   │
│   │  - AuthController (/api/auth)                                           │   │
│   │  - UserController (/api/users)                                          │   │
│   │  - DealCardController (/api/deal-cards)                                 │   │
│   └────────────────────────────────────┬────────────────────────────────────┘   │
│                                        │                                        │
│                                        ▼                                        │
│   ┌─────────────────────────────────────────────────────────────────────────┐   │
│   │                           Security Layer                                │   │
│   │  - JwtAuthenticationFilter (Extracts Bearer token, sets SecurityContext)│   │
│   │  - SecurityConfig (@PreAuthorize Role Guards: ADMIN, ANALYST, VIEWER)    │   │
│   └────────────────────────────────────┬────────────────────────────────────┘   │
│                                        │                                        │
│                                        ▼                                        │
│   ┌─────────────────────────────────────────────────────────────────────────┐   │
│   │                         Business Service Layer                          │   │
│   │  - DealCardService / UserService                                        │   │
│   └───────────────────┬─────────────────────────────────┬───────────────────┘   │
│                       │                                 │                       │
│                       ▼                                 ▼                       │
│   ┌───────────────────────────────┐   ┌─────────────────────────────────────┐   │
│   │       Persistence Layer       │   │        Event Pipeline Layer         │   │
│   │  - UserRepository             │   │  - DealEventProducer                │   │
│   │  - DealCardRepository         │   │    [@CircuitBreaker]                │   │
│   │  - PostgreSQL Driver          │   │    [@Retry + Backoff + Jitter]      │   │
│   └───────────────┬───────────────┘   └──────────────────┬──────────────────┘   │
└───────────────────┼──────────────────────────────────────┼──────────────────────┘
                    │                                      │
                    ▼                                      ▼
           ┌─────────────────┐                    ┌─────────────────┐
           │ PostgreSQL DB   │                    │ Apache Kafka    │
           │ (port 5432)     │                    │ Broker Container│
           │                 │                    │ (port 9092)     │
           └─────────────────┘                    └────────┬────────┘
                                                           │
                                                           ▼
                                                  ┌─────────────────┐
                                                  │ DealEventConsumer│
                                                  │ (DLQ Recoverer) │
                                                  └─────────────────┘
