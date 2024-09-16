
# CodeRunner Discord Bot

<center><img src="https://cdn.discordapp.com/avatars/1283371499197300738/498b8d6cd94b4324e780128667305b5d.png?size=1024" alt="CodeRunner Logo" width="200"/></center><!-- Optional: Add a logo or banner here -->

CodeRunner is a Discord bot built with Spring Boot and JDA (Java Discord API) that enables users to interact with AI models and execute code snippets within Discord. With a simple and intuitive command system, CodeRunner can handle AI-powered conversations and execute code snippets in real-time.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Running the Bot](#running-the-bot)
- [Commands](#commands)
- [Built With](#built-with)
- [Contributing](#contributing)
- [License](#license)

## Features

- **AI Conversations**: Chat with an AI model powered by OpenAI via the `!chat` command.
- **Code Execution**: Execute code snippets in multiple languages using the `!run` command.
- **Customizable**: Easily extendable with new commands and listeners.
- **Lightweight & Scalable**: Built with Spring Boot, the bot is designed to be fast and scalable, handling multiple requests simultaneously.

## Getting Started

Follow these instructions to set up and run the CodeRunner bot on your local machine.

### Prerequisites

Before setting up the bot, ensure you have the following installed:

- [Java 11+](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- A [Discord Bot Token](https://discord.com/developers/docs/intro)
- OpenAI API Key (for ChatGPT integration)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/CodeRunner.git
   cd CodeRunner
   ```

2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

### Configuration

1. Rename the `application-env.properties` file to `application.properties`.
2. Add your configuration details:
   - `BOT_TOKEN`: Your Discord bot token.
   - `AIBOT_TOKEN`: Your OpenAI API key.
   
   Example:
   ```properties
   BOT_TOKEN=your-discord-bot-token
   AIBOT_TOKEN=your-openai-api-key
   ```

### Running the Bot

To start the bot, run the following command:

```bash
mvn spring-boot:run
```

Alternatively, you can package the application and run the JAR file:

```bash
mvn package
java -jar target/coderunner-1.0.jar
```

## Commands

The bot responds to several commands to interact with AI models and execute code snippets.

### `!chat <message>`

Interact with an AI model (powered by OpenAI) and receive responses in real-time.

Example:
```
!chat What is the capital of France?
```

### `!run <code>`

Execute a code snippet in real-time and receive the output.

Example:
```
!run
```python
print("Hello, world!")
```

### Supported Code Languages
- Python
- JavaScript
- Java
- More to be added soon!

## Built With

- **[Spring Boot](https://spring.io/projects/spring-boot)** - Java framework for building production-ready applications.
- **[JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA)** - Library for interacting with the Discord API.
- **[OpenAI API](https://beta.openai.com/)** - Integration for AI-powered conversations.
- **[OkHttp](https://square.github.io/okhttp/)** - HTTP & HTTP/2 client for Java.

## Contributing

Contributions are welcome! If you'd like to improve CodeRunner, feel free to submit a pull request or open an issue.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
