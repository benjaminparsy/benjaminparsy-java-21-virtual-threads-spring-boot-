# Java 21's virtual threads with Spring Boot

This project is a sample application for learning and exploring the use of Java 21 virtual threads with Spring Boot. 
It provides a basic implementation and demonstrates how to configure, develop and test REST endpoints in virtual threads with Spring Boot.

## Prerequisites

- [Java 21+](https://adoptopenjdk.net/)
- [Maven 3.8+](https://maven.apache.org/)

## Installation

1. Clone the project:

   ```bash
   git clone https://github.com/benjaminparsy/java-21-virtual-threads-spring-boot
   ```

2. Compile and run the application:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

## Switch between virtual and platform thread

To change the thread type, change the value of the 'spring.threads.virtual.enabled' to true or false

## Gatling performance test

A performance test was carried out using Gatling to verify 
the performance gain of virtual threads.

To launch it, you need to run :

   ```bash
   mvn gatling:test
   ```

You also need to configure the environment variables below, which correspond to the test parameters:

- GATLING_DURATION_SECONDS: Test duration
- GATLING_REQUESTS_PER_SECOND: Number of requests per second
  
For IntelliJ users, runConfigurations are already programmed.

## Github actions

Github actions have been programmed for simplified performance testing.

## Reviews

The tests carried out on virtual threads are not yet conclusive, as the multiplication of threads overloads the database connection pool much more quickly than platform threads.
As a result, more requests end up in timeout because of the database.
To overcome this timeout problem, a non-blocking database is conceivable, but this implies switching to reactive programming.