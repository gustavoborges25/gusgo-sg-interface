# Gusgo - SG Interface

Microservice that acts as the interface for the ERP SG, translating information for the Gusgo.

## Description

This project must expose endpoints that receive xls file, translate it to send to Gusgo registration.

## Technologies Used

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- Lombok
- Gradle
- JUnit 5 (for testing)

## Features

- Upload xls list people and send to Gusgo

## Prerequisites

Before you begin, make sure you have the following installed on your machine:

- JDK 21
- Gradle
- docker-compose

## Installation on dev

1. Clone the repository:
   ```bash
   git clone https://github.com/gustavoborges25/gusgo-sg-interface
   cd gusgo-sg-interface

2. Set up the Kafka:
   ```bash
   docker-compose up -d 
   ## docker-compose will spin up an instance of the PostgreSQL database. If you have any questions, look at the docker-compose.yml file in the project root.
3. Compile the project:
   ```bash
   ./gradlew build

## Contribution
Contributions are welcome! Feel free to open an issue or submit a pull request.

## Contact
For any questions, feel free to reach out:

- Email: gustavoborges25@gmail.com
- GitHub: gustavoborges25