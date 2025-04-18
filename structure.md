# Banking Application Architecture Design

## Project Structure

```
com.interview.assessment
├── config                  // Configuration classes
│   ├── SecurityConfig.java // Security configuration
│   ├── SwaggerConfig.java  // API documentation configuration
│   ├── DataInitializer.java  // Used to initialize mock data
│   └── ApiVersionConfig.java  // API version control configuration
├── controller              // API controllers
│   ├── v1                  // v1 version API
│   │   ├── AccountController.java    // Account related APIs
│   │   ├── TransactionController.java // Transaction related APIs
│   │   └── StatementController.java  // Statement related APIs
│   └── BaseController.java  // Base controller
├── dto                     // Data Transfer Objects
│   ├── request             // Request DTOs
│   │   ├── TransactionRequest.java
│   │   └── StatementRequest.java
│   └── response            // Response DTOs
│       ├── AccountResponse.java
│       ├── TransactionResponse.java
│       └── StatementResponse.java
├── entity                  // Entity classes
│   ├── Account.java        // Account entity
│   ├── Transaction.java    // Transaction entity
│   └── User.java           // User entity
├── enums                   // Enumeration classes
│   ├── AccountType.java    // Account types
│   └── TransactionType.java // Transaction types
├── exception               // Exception handling
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
├── repository              // Data access layer
│   ├── AccountRepository.java
│   ├── TransactionRepository.java
│   └── UserRepository.java
├── security                // Security related
│   ├── CustomUserDetailsService.java
│   └── OAuth2ClientConfig.java
├── service                 // Business logic layer
│   ├── AccountService.java
│   ├── TransactionService.java
│   └── StatementService.java
├── util                    // Utility classes
│   ├── PDFGenerator.java   // PDF generation tool
│   └── DateUtil.java       // Date utilities
├── test                      // Testing related
│   ├── AccountControllerTest.java
│   ├── TransactionServiceTest.java
│   └── ...
├── aspect
│   └── LoggingAspect.java       // Logging aspect
├── filter
│   └── RequestTracingFilter.java // Request tracing filter
├── service
│   └── AuditLogService.java     // Audit log service
└── AssessmentApplication.java  // Application entry point
└── resources
    ├── schema.sql            // Database schema initialization
    ├── data.sql              // Mock data initialization
    ├── application.yml       // Detailed configuration file
    └── logback-spring.xml    // Logging configuration file
```

## Data Models

### User Entity
```
- id: Long (PK)
- username: String
- password: String (encrypted storage)
- roles: List<Role>
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### Account Entity
```
- id: Long (PK)
- accountNumber: String (unique)
- balance: BigDecimal
- accountType: AccountType (enum: SAVINGS, CHECKING, CREDIT)
- userId: Long (FK -> User)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### Transaction Entity
```
- id: Long (PK)
- transactionType: TransactionType (enum: CREDIT, DEBIT)
- amount: BigDecimal
- description: String
- transactionDate: LocalDateTime
- accountId: Long (FK -> Account)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

## API Endpoints

All API endpoints are version controlled, with default version as v1.

### Account API
- `GET /api/v1/accounts` - Get all accounts for the current user
- `GET /api/v1/accounts/{id}` - Get specific account details

### Transaction API
- `GET /api/v1/accounts/{accountId}/transactions` - Get all transactions for an account
- `POST /api/v1/accounts/{accountId}/transactions` - Create a new transaction
- `GET /api/v1/transactions/{id}` - Get specific transaction details

### Statement API
- `GET /api/v1/accounts/{accountId}/statements/{year}/{month}` - Download statement for a specific month

## API Version Control Strategy

The system adopts a URL path-based API version control strategy:

1. **Version Identifier**:
   - Include version number in URL path, such as `/api/v1/accounts`
   - Controller classes are organized in different packages by version (e.g., `controller.v1`)

2. **Version Upgrade Strategy**:
   - Create a new version (e.g., `v2`) when the API undergoes significant changes
   - Old versions will maintain backward compatibility and set reasonable deprecation timeframes

3. **Multiple Versions Coexistence**:
   - The system supports running multiple versions of APIs simultaneously
   - New versions are added to new package paths (e.g., `controller.v2`)
   - Route different version requests to corresponding controllers through routing configuration

4. **Version Information Retrieval**:
   - API responses include current API version information
   - Detailed descriptions of each version are provided through Swagger documentation

5. **Default Version**:
   - If a client does not specify a version, `v1` version is used by default
   - Default version can be modified through configuration

## Data Flow

### View Account List
1. Client sends an authorized request to `/api/v1/accounts`
2. Security filter verifies user identity and permissions
3. `AccountController` receives the request and calls `AccountService`
4. `AccountService` uses `AccountRepository` to get all accounts for the user
5. Convert account data to DTOs and return to the client

### Create Transaction
1. Client sends a POST request with transaction details to `/api/v1/accounts/{accountId}/transactions`
2. Security filter verifies user identity and permissions
3. `TransactionController` receives the request and validates input
4. Calls `TransactionService` to process the transaction
5. `TransactionService` verifies account balance and creates a transaction record
6. `TransactionService` updates the account balance
7. Returns transaction result to the client

### Download Monthly Statement
1. Client sends a request to `/api/v1/accounts/{accountId}/statements/{year}/{month}`
2. Security filter verifies user identity and permissions
3. `StatementController` receives the request and calls `StatementService`
4. `StatementService` uses `TransactionRepository` to get all transactions for that month
5. `StatementService` uses `PDFGenerator` to generate a PDF format statement
6. Returns the PDF statement to the client

## Security Architecture

### Authentication Process
1. User logs in through an external authentication service
2. Authentication service provides OAuth2 token
3. Client includes the token in subsequent requests
4. Application verifies the token and extracts user information and roles

### Authorization Strategy
1. All API endpoints require user authentication
2. Only users with the "new_app_role" role can access application features
3. Users can only access their own account and transaction data
4. All API requests are transmitted via HTTPS

## Non-Functional Features

### Security Measures
- OAuth 2.0 authentication and authorization
- HTTPS transmission encryption
- Password encrypted storage
- Input validation and SQL injection prevention

### Logging and Monitoring
- Request logging (request path, method, response status, execution time)
- Exception logging
- Audit logging for important operations (such as transaction creation)

### Data Protection
- Sensitive data encryption
- Secure exception handling, not exposing sensitive information
- Appropriate data access control 