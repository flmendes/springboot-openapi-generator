# Spring Boot OpenAPI Generator Project

Spring Boot 3.5.7 application demonstrating OpenAPI Generator usage for generating API interfaces and models from OpenAPI specification. The project includes a simple Student Registration API with in-memory storage and comprehensive API documentation.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Prerequisites
- Install OpenJDK 21 (REQUIRED - project will not build with older Java versions):
  ```bash
  sudo apt update && sudo apt install -y openjdk-21-jdk
  sudo update-alternatives --config java  # Select Java 21
  export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
  ```

### Bootstrap and Build
- **Maven Build System** (primary):
  ```bash
  ./mvnw clean compile  # ~4 seconds - OpenAPI code generation + compilation
  ./mvnw clean package  # ~46 seconds full build - NEVER CANCEL, set timeout to 90+ seconds
  ./mvnw test           # ~8 seconds tests - NEVER CANCEL, set timeout to 30+ seconds
  ```

- **Gradle Build System** (alternative with enhanced features):
  ```bash
  ./gradlew clean build # First run: ~3.5 minutes (daemon startup + dependencies) - NEVER CANCEL, set timeout to 300+ seconds
                        # Subsequent runs: ~30 seconds - NEVER CANCEL, set timeout to 60+ seconds
  ./gradlew test        # ~5 seconds when up-to-date
  ```

### Run the Application
- **With Maven**:
  ```bash
  export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
  ./mvnw spring-boot:run  # Application starts in ~2 seconds
  ```

- **With Gradle** (RECOMMENDED for full OpenAPI documentation):
  ```bash
  export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
  ./gradlew bootRun  # Application starts in ~2 seconds
  ```

- Application runs on: http://localhost:8080

## Validation

### Always manually validate functionality after making changes:
1. **API Health Check**:
   ```bash
   curl http://localhost:8080/actuator/health
   # Expected: {"status":"UP"}
   ```

2. **Student Creation Endpoint**:
   ```bash
   curl -X POST http://localhost:8080/students \
        -H "Content-Type: application/json" \
        -d '{"name":"John Doe","email":"john.doe@email.com","phone":"(11) 99999-9999"}'
   # Expected: Student object with generated UUID
   ```

3. **Email Validation (Business Logic)**:
   ```bash
   curl -X POST http://localhost:8080/students \
        -H "Content-Type: application/json" \
        -d '{"name":"Jane Doe","email":"john.doe@email.com","phone":"(22) 88888-8888"}'
   # Expected: {"error":"Invalid argument","message":"Email already exists"}
   ```

4. **OpenAPI Documentation** (Gradle build only):
   ```bash
   curl http://localhost:8080/v3/api-docs  # Should return OpenAPI JSON
   curl -I http://localhost:8080/swagger-ui/index.html  # Should return 200 OK
   ```

### Test Suite Validation
- Always run tests after making changes:
  ```bash
  ./mvnw test  # 4 tests: Spring Boot context + Student business logic
  ./gradlew test
  ```

## Build System Differences

### Maven vs Gradle Feature Matrix:
| Feature | Maven | Gradle |
|---------|-------|--------|
| Build Speed | Faster (~46s) | Slower first run (~3.5m), faster subsequent |
| OpenAPI Code Generation | ✅ Basic | ✅ Enhanced |
| Swagger UI | ❌ Limited | ✅ Full Support |
| springdoc-openapi | ❌ Not included | ✅ Included |
| API Documentation | ❌ Basic | ✅ Complete with UI |

### Recommendation:
- Use **Gradle** for full-featured development with complete OpenAPI documentation
- Use **Maven** for faster CI/CD builds when documentation UI is not needed

## Project Structure

### Key Directories and Files:
```
├── src/main/resources/openapi/student.yml  # OpenAPI specification (source of truth)
├── src/main/java/com/example/
│   ├── Application.java                    # Spring Boot main class
│   ├── controller/StudentController.java   # Implements generated StudentsApi interface
│   ├── service/StudentService.java         # Business logic with in-memory storage
│   ├── model/Student.java                  # Domain model (Java record)
│   └── exception/GlobalExceptionHandler.java # Error handling
├── src/test/java/com/example/
│   ├── SpringbootOpenapiGeneratorApplicationTests.java  # Spring Boot integration test
│   └── service/StudentServiceTest.java     # Unit tests for Student record
├── target/generated-sources/openapi/       # Maven: Generated API interfaces and models
└── build/generated/                        # Gradle: Generated API interfaces and models
```

### Generated Code Locations:
- **Maven**: `target/generated-sources/openapi/src/main/java/com/example/students/`
- **Gradle**: `build/generated/src/main/java/com/example/students/`

Generated files include:
- `StudentsApi.java` - API interface implemented by StudentController
- `StudentRequest.java` - Request model
- `StudentResponse.java` - Response model

## Common Tasks

### Add New API Endpoint:
1. Edit `src/main/resources/openapi/student.yml`
2. Run build to regenerate interfaces: `./mvnw compile` or `./gradlew compileJava`
3. Implement new methods in `StudentController.java`
4. Add business logic to `StudentService.java`
5. Add validation tests

### Debug OpenAPI Generation Issues:
- Check warnings during build about empty operationId
- Verify YAML syntax in `student.yml`
- Regenerate with: `./mvnw clean compile` or `./gradlew clean compileJava`

### Troubleshooting

#### Build Failures:
- **"release version 21 not supported"**: Install and configure Java 21
- **Maven build slow**: First builds download dependencies (~2 minutes)
- **Gradle daemon startup**: First run takes ~3.5 minutes, this is normal

#### Runtime Issues:
- **Application won't start**: Verify Java 21 is active with `java -version`
- **OpenAPI docs not working**: Use Gradle build instead of Maven
- **Port 8080 in use**: Stop running application or change port in `application.properties`

#### Common Validation Steps:
- Always check that API endpoints return expected JSON responses
- Verify business logic with duplicate email validation test
- Confirm OpenAPI documentation is accessible at `/v3/api-docs` (Gradle only)
- Test both success and error scenarios

## CI/CD Considerations
- Use Maven for faster CI builds: `./mvnw clean package -DskipTests` (~30 seconds)
- Include Java 21 setup in CI environment
- Set appropriate timeouts: 90+ seconds for Maven builds, 300+ seconds for Gradle first runs
- **NEVER CANCEL** long-running builds - they will complete successfully

## Development Workflow
1. Make code changes
2. Run quick compilation: `./mvnw compile` (~4 seconds)
3. Run tests: `./mvnw test` (~8 seconds)
4. Test API manually with curl commands above
5. For OpenAPI documentation changes, use Gradle: `./gradlew bootRun`
6. Validate complete user scenarios before committing