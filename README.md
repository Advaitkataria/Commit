# Commit — Daily Accountability App

A social accountability app where users make a daily commitment every morning and get matched with a stranger to stay accountable. Both must check in by end of day — if either fails, the streak is lost.

## Live Demo
🌐 [commit-frontend-l9bae2lew-advait-katarias-projects.vercel.app](https://commit-frontend-l9bae2lew-advait-katarias-projects.vercel.app)

## Tech Stack

**Backend**
- Java 17
- Spring Boot 3.3
- Maven
- Spring Security + JWT Authentication
- Spring Data JPA + Hibernate
- MySQL (Aiven cloud)
- Deployed on Railway


## Features

- JWT-based authentication (register, login, secure endpoints)
- Daily commitment creation with categories (FITNESS, STUDY, WORK, HABIT, PERSONAL, CUSTOM)
- Real-time matching system using a waiting pool with optimistic locking to prevent race conditions
- Daily check-in system — both users must complete for the match to succeed
- 7-day check-in history grid per user
- Partner status tracking
- Profile page with commit history
- Global exception handling with custom error responses
- CORS configured for cross-origin frontend-backend communication

## API Endpoints

### Auth (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/register | Register new user |
| POST | /auth/login | Login and get JWT token |

### Commits (Protected)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /commit | Create today's commitment |
| GET | /commit | Get today's commitment |
| GET | /commit/history | Get all past commitments |
| PUT | /commit/{id} | Update a commitment |
| DELETE | /commit/{id} | Delete a commitment |

### Matching (Protected)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /match/join | Join matching pool |
| GET | /match/current | Get current active match |

### Check-ins (Protected)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /checkins | Check in for today |
| GET | /checkins/today | Check if already checked in |
| GET | /checkins/match/{matchId} | Get check-in history for a match |

### Profile (Protected)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /profile | Get user profile and history |

## Project Structure
src/main/java/org/example/commit1/
├── controller/        # REST controllers
├── service/           # Business logic
├── repository/        # JPA repositories
├── model/             # JPA entities
├── dto/               # Data transfer objects
├── filter/            # JWT auth filter
├── config/            # Security configuration
└── exception/         # Global exception handling
## Security

- Passwords hashed with BCrypt
- JWT tokens with 24-hour expiration
- Stateless session management
- Protected routes require valid Bearer token
- Optimistic locking on waiting pool to prevent race conditions


### Prerequisites
- Java 17
- Maven
- MySQL



