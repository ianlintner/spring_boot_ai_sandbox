# Contributing to Conversation Scoring Service

Thank you for your interest in contributing to this project! This document provides guidelines and instructions for contributing.

## Getting Started

1. Fork the repository
2. Clone your fork: `git clone https://github.com/YOUR_USERNAME/spring_boot_ai_sandbox.git`
3. Create a feature branch: `git checkout -b feature/your-feature-name`
4. Make your changes
5. Commit your changes: `git commit -m "Description of changes"`
6. Push to your fork: `git push origin feature/your-feature-name`
7. Open a Pull Request

## Development Setup

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

### Building the Project

```bash
mvn clean install
```

### Running Tests

```bash
mvn test
```

### Running the Application

```bash
mvn spring-boot:run
```

## Code Style

This project uses Google Java Style for code formatting.

### Format Your Code

```bash
mvn formatter:format
```

### Check Code Style

```bash
mvn checkstyle:check
```

## Code Quality

### Run All Quality Checks

```bash
mvn verify
```

This runs:
- Unit tests
- Checkstyle
- PMD
- SpotBugs

### Security Scanning

```bash
mvn dependency-check:check
```

## Pull Request Guidelines

1. **Keep changes focused**: Each PR should address a single concern
2. **Write tests**: Add tests for new functionality
3. **Update documentation**: Update README if needed
4. **Follow code style**: Run formatter before committing
5. **Pass all checks**: Ensure CI passes
6. **Write clear commit messages**: Use descriptive commit messages

### Commit Message Format

```
<type>: <subject>

<body>

<footer>
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Build/tool changes

Example:
```
feat: Add support for custom scoring algorithms

Implement a plugin system that allows users to define
custom scoring algorithms via configuration.

Closes #123
```

## Testing

- Write unit tests for all new functionality
- Aim for high test coverage
- Use meaningful test names
- Include both positive and negative test cases

## Reporting Issues

When reporting issues, please include:
- Clear description of the problem
- Steps to reproduce
- Expected vs actual behavior
- Java version and OS
- Relevant logs or error messages

## Questions?

Feel free to open an issue for questions or discussions about contributing.

## License

By contributing, you agree that your contributions will be licensed under the same license as the project.
