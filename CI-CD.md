# CI/CD Pipeline Documentation

This project includes a comprehensive CI/CD pipeline implemented with GitHub Actions that automatically tests, builds, and deploys the application using **Spring Boot Buildpacks** (Cloud Native Buildpacks).

## Pipeline Overview

The CI/CD pipeline consists of the following stages:

1. **Test** - Runs all unit and integration tests
2. **Build** - Compiles and packages the application
3. **Build Image** - Creates a Docker container image using Spring Boot Buildpacks
4. **Push** - Pushes the image to GitHub Container Registry (ghcr.io)

## Spring Boot Buildpacks

This project uses **Cloud Native Buildpacks** via Spring Boot's built-in support (`./mvnw spring-boot:build-image`), which provides:

- 🚀 **No Dockerfile needed** - Automatic image creation
- 🔒 **Security** - Regularly updated base images from Paketo Buildpacks
- ⚡ **Performance** - Layer caching for faster builds
- 📦 **Optimized images** - Minimal size with JRE-only runtime
- 🔄 **Consistency** - Same image locally and in CI/CD
- 🛡️ **Best practices** - Security and performance optimizations built-in

## Workflow Triggers

The pipeline runs automatically on:

- **Push to main branch** - Full pipeline including image push
- **Push to develop branch** - Full pipeline including image push
- **Pull Requests to main/develop** - Test and build only (no image push)
- **Manual trigger** - Via GitHub Actions UI (workflow_dispatch)

## Pipeline Jobs

### 1. Test Job

Runs the application test suite using Maven.

**Steps:**
- Checkout code
- Set up JDK 21 (Eclipse Temurin)
- Execute `./mvnw clean test`
- Upload test results as artifacts

**Artifacts:** Test results available for 7 days

### 2. Build Job

Compiles and packages the Spring Boot application.

**Steps:**
- Checkout code
- Set up JDK 21 (Eclipse Temurin)
- Execute `./mvnw clean package -DskipTests`
- Upload JAR file as artifact

**Artifacts:** Application JAR available for 7 days

### 3. Build and Push Image Job

Creates a Docker image using Spring Boot Buildpacks and pushes it to GitHub Container Registry.

**Steps:**
- Checkout code
- Set up JDK 21 (Eclipse Temurin)
- Login to GitHub Container Registry
- Extract Docker metadata and tags
- **Build image using `./mvnw spring-boot:build-image`**
- Tag image with multiple tags
- Test Docker image (health check)
- Push all image tags to registry (if not a PR)
- Generate image summary

**Image Tags Generated:**
- `latest` - For main branch only
- `main` - For main branch commits
- `main-{sha}` - Specific commit reference
- `develop` - For develop branch commits
- `develop-{sha}` - Develop branch commit reference
- `pr-{number}` - For pull requests (not pushed)

### 4. Summary Job

Generates a deployment summary with job results and build information.

## Docker Image

The Docker image is built using **Spring Boot Buildpacks** (Paketo Buildpacks):

**Buildpack Features:**
- Base: Automatically selected optimal base image (Ubuntu Jammy Tiny)
- Builder: `paketobuildpacks/builder-jammy-base`
- **JRE-only runtime** - No JDK in final image for smaller size
- **Non-root user** - Runs as CNB user for security
- **Layer optimization** - Separate layers for dependencies, classes, and resources
- **Memory calculator** - Automatic JVM memory configuration
- **Health check ready** - Spring Boot Actuator endpoints available
- **Reproducible builds** - Same source = same image

**Image Layers:**
```
- Paketo BellSoft Liberica JRE
- Application dependencies (cached)
- Application classes
- Application resources
```

## Using the Docker Image

### Pull from GitHub Container Registry

```bash
# Pull latest image from main branch
docker pull ghcr.io/<username>/springboot-openapi-generator:latest

# Pull specific branch
docker pull ghcr.io/<username>/springboot-openapi-generator:main

# Pull specific commit
docker pull ghcr.io/<username>/springboot-openapi-generator:main-abc1234
```

### Run the Container

```bash
# Run directly with Docker
docker run -d -p 8080:8080 ghcr.io/<username>/springboot-openapi-generator:latest

# Or use docker-compose
docker-compose up -d
```

### Access the Application

```bash
# Check health
curl http://localhost:8080/actuator/health

# Test API endpoint
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","phone":"(11) 99999-9999"}'
```

## Local Docker Development

### Build locally with Spring Boot Buildpacks

```bash
# Build the image using Maven
./mvnw spring-boot:build-image

# Or specify custom image name
./mvnw spring-boot:build-image \
  -Dspring-boot.build-image.imageName=myapp:1.0

# Build with Gradle (alternative)
./gradlew bootBuildImage
```

### Run locally

```bash
# Run with Docker
docker run -d -p 8080:8080 springboot-openapi-generator:0.0.1-SNAPSHOT

# Run with custom image name
docker run -d -p 8080:8080 myapp:1.0

# View logs
docker logs -f <container-id>

# Stop container
docker stop <container-id>
```

### Customizing the Build

You can customize the buildpack image in `pom.xml`:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <configuration>
        <image>
          <name>ghcr.io/${project.groupId}/${project.artifactId}:${project.version}</name>
          <env>
            <BP_JVM_VERSION>21</BP_JVM_VERSION>
            <BPE_JAVA_TOOL_OPTIONS>-XX:MaxRAMPercentage=75.0</BPE_JAVA_TOOL_OPTIONS>
          </env>
          <buildpacks>
            <buildpack>paketo-buildpacks/java</buildpack>
          </buildpacks>
        </image>
      </configuration>
    </plugin>
  </plugins>
</build>
```

## GitHub Container Registry Permissions

To pull private images, you need to authenticate:

```bash
# Create a Personal Access Token (PAT) with read:packages scope
# Then login:
echo $GITHUB_TOKEN | docker login ghcr.io -u USERNAME --password-stdin
```

## Environment Variables

The container supports standard Spring Boot environment variables:

```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e SERVER_PORT=8080 \
  ghcr.io/<username>/springboot-openapi-generator:latest
```

## Monitoring and Health Checks

The Docker image includes built-in health checks:

```bash
# Check container health status
docker ps

# View health check logs
docker inspect --format='{{json .State.Health}}' <container-id>

# Access actuator endpoints
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/info
```

## Security Features

1. **Cloud Native Buildpacks** - Industry-standard, secure base images
2. **Non-root user** - Runs as CNB user (UID 1000)
3. **Minimal runtime** - JRE-only, no build tools in final image
4. **Automatic updates** - Paketo team maintains and updates base images
5. **Layer-based caching** - Faster builds and efficient storage
6. **Reproducible builds** - Same source code produces identical images
7. **SBOM generation** - Software Bill of Materials for security scanning

## Buildpack Environment Variables

You can configure the buildpack behavior with environment variables:

```bash
# During build
./mvnw spring-boot:build-image \
  -Dspring-boot.build-image.env.BP_JVM_VERSION=21 \
  -Dspring-boot.build-image.env.BP_JVM_JLINK_ENABLED=true

# Common buildpack environment variables:
# - BP_JVM_VERSION: Java version (default: auto-detected)
# - BP_JVM_JLINK_ENABLED: Enable JLink for smaller images
# - BPE_DELIM_JAVA_TOOL_OPTIONS: JVM options delimiter
# - BPE_JAVA_TOOL_OPTIONS: JVM runtime options
```

## Troubleshooting

### Pipeline fails on test job

```bash
# Run tests locally
./mvnw clean test

# Check test reports
cat target/surefire-reports/*.txt
```

### Docker build fails

```bash
# Check if Docker daemon is running
docker ps

# Build with verbose output
./mvnw spring-boot:build-image -X

# Clean and rebuild
./mvnw clean
./mvnw spring-boot:build-image
```

### Image push permission denied

Ensure the `GITHUB_TOKEN` has `packages: write` permission. This is automatically provided for GitHub Actions.

### Container fails health check

```bash
# Check logs
docker logs <container-id>

# Test health endpoint manually
docker exec <container-id> wget -O- http://localhost:8080/actuator/health
```

## Pipeline Configuration

The pipeline configuration is in `.github/workflows/ci-cd.yml`.

Key environment variables:
- `REGISTRY`: ghcr.io
- `IMAGE_NAME`: Automatically derived from repository name
- `JAVA_VERSION`: 21
- `JAVA_DISTRIBUTION`: temurin

## Customization

### Change Java Version

Edit `.github/workflows/ci-cd.yml`:

```yaml
env:
  JAVA_VERSION: '17'  # Change to desired version
```

### Add Environment-specific Deployments

Add a new job after `build-and-push-image`:

```yaml
deploy-staging:
  name: Deploy to Staging
  runs-on: ubuntu-latest
  needs: build-and-push-image
  if: github.ref == 'refs/heads/develop'
  steps:
    - name: Deploy
      run: |
        # Your deployment commands
```

### Change Docker Registry

To use Docker Hub instead of GHCR:

```yaml
env:
  REGISTRY: docker.io
  IMAGE_NAME: username/springboot-openapi-generator
```

Then add Docker Hub credentials to repository secrets:
- `DOCKER_USERNAME`
- `DOCKER_PASSWORD`

## Best Practices

1. **Always run tests locally** before pushing
2. **Use feature branches** for development
3. **Create Pull Requests** to trigger pipeline validation
4. **Review pipeline results** before merging
5. **Tag releases** with semantic versioning
6. **Monitor image size** and optimize when needed
7. **Scan images** for vulnerabilities regularly

## Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)
- [GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
