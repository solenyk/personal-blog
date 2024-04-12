# personal-blog

The personal blog project, built using Spring Boot 3, Spring Security 6, and Java 17, implements authentication and
authorization mechanisms using the OAuth2 protocol. It consists of two main components: the Authorization Server and the
Resource Server. The Authorization Server is responsible for user registration, authentication, token issuance, and
token management. The Resource Server provides secure access to the blog's resources by validating the validity and
permissions of tokens issued by the Auth Server.

## Table of Contents

- [Introduction](#introduction)
- [Technologies](#technologies)
- [Features](#features)
- [Application endpoints](#application-endpoints)
- [API Documentation](#api-documentation)
- [Installation and Usage](#installation-and-usage)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This personal blog web application is designed to allow users to view other people's posts and create, edit, delete 
their own posts. A user with the administrator role can view the activity statistics of web application users.

## Technologies

This application was built using the following technologies:

### Backend:

- Spring Boot 3
- Spring Security 6
- Java 17

### Database:

- Java ORM (JPA 3 + Hibernate 6)
- PostgreSQL 14

### Testing:

- JUnit 5: A Java framework for writing unit tests
- Mockito 5: A mocking framework for Java that allows developers to create mock objects in tests
- TestContainers: An open source framework for providing lightweight instances of databases, web browsers, or just about
  anything that can run in a Docker container
- WebTestClient: An HTTP client designed for testing server applications

### Documentation:

- Swagger 3: A tool for writing API documentation

## Features

### Authorization server features:

- Login
- Logout
- Retrieve authorization code
- Retrieve jwt access and opaque refresh tokens
- Refresh access token
- User registration

## Application endpoints

### Authorization server endpoints:
- __POST /login__ - Form-based user login
- __GET /logout__ - User logout
- __GET /oauth2/authorize__ - Retrieve authorization code

    _Query Parameters:_
    - response_type: In this case it should be "code". (Required parameter)
    - client_id: OAuth2 registered client id. (Required parameter)
    - redirect_uri: OAuth2 registered client redirect uri. (Required parameter)

- __POST /oauth2/token__ - Retrieve jwt access and opaque refresh user tokens

    _Query Parameters:_
    - code: Authorization code. (Required parameter)
    - grant_type: Grant type. In this case it should be "authorization_code". (Required parameter)
    - redirect_uri: OAuth2 registered client redirect uri. (Required parameter)
    
    _Request Headers:_
    - Authorization: Basic (client_name:client_secret)

- __POST /oauth2/token__ - Refresh user access token

  _Query Parameters:_
  - grant_type: Grant type. In this case it should be "refresh_token". (Required parameter)
  - refresh_token: User opaque refresh token. (Required parameter)

  _Request Headers:_
  - Authorization: Basic (client_name:client_secret)

#### RegistrationController:

- __POST /api/v1/auth/register__ - User registration
  
## API Documentation

To view the API documentation, you can use Swagger. Swagger provides a user-friendly interface for exploring and testing
the API endpoints.

[Swagger Authorization Server Documentation](http://localhost:8081/swagger-ui/index.html#)

## Installation and Usage

To run this application, please follow the steps below:

1. Clone the repository to your local machine
2. Import the project into your IDE
3. Update the application.yml files in the authserver and resourceserver modules with your data and settings.
4. Set up a Postgres databases and update the application.yml files in the authserver and resourceserver modules with your database details
5. Run the authserver application module using the command `mvn spring-boot:run` or by running the main method in
   the `AuthServerApplication` class
6. Run the resourceserver application module using the command `mvn spring-boot:run` or by running the main method in
   the `ResourceServerApplication` class
7. Use a tool such as Postman or Swagger to make requests to the API endpoints.

> **_NOTE:_** The application is independent of a specific relational database, so if you want to use a different
> database, you only need to remove the Postgres dependency in the pom.xml file and add the dependency for your database.

## Contributing

If you would like to contribute to this project, feel free to fork the repository and submit a pull request.

## License

This project is not licensed and is not intended for use or distribution.