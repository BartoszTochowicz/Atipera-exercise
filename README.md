# Atipera-exercise

A Spring Boot application that consumes the GitHub API to list non-fork repositories for a given GitHub user, along with their branch information and last commit SHA.

## Features

- Fetches a list of **public, non-fork repositories** for a given GitHub username.
- For each repository, returns the repository name, owner login, and a list of branches.
- For each branch, returns the branch name and the latest commit SHA.
- Returns a well-defined JSON error response if the username does not exist.

## Technologies Used

- Java 21
- Spring Boot
- Maven
- Dotenv
- Swagger UI (for API documentation/testing)

## API

### Endpoint

```
GET /users/{username}/repos
```

- **username**: GitHub username to query.

#### Example Response

```json
[
  {
    "repoName": "sample-repo",
    "ownerLogin": "username",
    "branchName": "main",
    "lastCommitSha": "e1a2b3c4d5..."
  }
  // ...
]
```

#### Error Response (e.g. user not found)

```json
{
  "status": 404,
  "message": "User not found"
}
```

Other error responses may include:
- 403: API rate limit exceeded or token invalid.
- 500: Internal server error.

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.6+
- A GitHub personal access token (for accessing the GitHub API)

### Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/BartoszTochowicz/Atipera-exercise.git
   cd Atipera-exercise
   ```

2. **Configure your GitHub token:**

   Create a `.env` file in the root directory with the following content:

   ```
   GITHUB_TOKEN=your_github_personal_access_token
   ```
   To get your own GitHub token visit https://github.com/settings/tokens

3. **Build the project:**

   ```bash
   mvn clean install
   ```

4. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

5. **Access the API:**

   - Visit the Swagger UI at (http://localhost:8080/swagger-ui/index.html#) to explore and test the API endpoints.
   - Or call the endpoint directly, for example:
     ```
     http://localhost:8080/api/github/repos?username=asf
     ```

## Project Structure

- `src/main/java/com/atipera/`
  - `controller/` - REST controller(s)
  - `service/` - Business logic and GitHub API integration
  - `model/` - Data models
  - `exceptions/` - Custom exception classes

## Custom Error Handling

If a user does not exist on GitHub, the API returns a 404 response in the following format:

```json
{
  "status": 404,
  "message": "User not found"
}
```

Other errors (like invalid token or rate limiting) will return appropriate HTTP status codes and error messages.


## License

This project is licensed under the MIT License.

---

**Author:** Bartosz Tochowicz
