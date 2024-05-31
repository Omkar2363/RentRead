
## RentRead - Online Book Rental System


### Introduction

RentRead is a RESTful API service developed using Spring Boot to manage an online book rental system with MySQL as the data persistence layer. This service provides functionality for user authentication and authorization, book management, and rental management. The system distinguishes between public endpoints (accessible to everyone) and private endpoints (restricted to authenticated users with specific roles).
 
## Features

### 1. Authentication and Authorization

- Basic Authentication
- Roles: USER and ADMIN

### 2. User Registration and Login

- Users can register with email, password, first name, last name, and role.
- Passwords are encrypted with BCrypt.
- Default role is USER if not specified.
- Registered users can log in using email and password.

### 3. Book Management

- Fields: Title, Author, Genre, Availability Status.
- Only administrators can create, update, and delete books.
- All users can browse available books.

### 4. Rental Management

- Users can rent books.
- A user can have a maximum of two active rentals.
- Users can return rented books.
## Requirements :

### 1. Logging
- Log information and errors using SLF4J.

### 2. Error Handling
- Graceful handling of common errors with appropriate HTTP codes.

### 3. Testing
- Basic unit tests with MockMvc and Mockito 

### 4. Deployment
- Generate a JAR file for the application.
Instructions on how to run the application.

### 5. Documentation
- Descriptive README.md.
- Public Postman Collection.

## Setup Instructions :

### Prerequisites

- Java 11 or higher
- Gradle 6.0.8 or higher
- MySQL 8.0 or higher
- Git
- Postman


To run the application follow the following steps :

### Local Environment Setup :
#### 1. Clone the repository :

    git clone https://github.com/Omkar2363/RentRead.git


### 2. Set up the MySQL database :

- Update the application.properties file with your MySQL database details:

       spring.datasource.url=jdbc:mysql://localhost:3306/rentread?createDatabaseIfNotExist=true
       spring.datasource.username=<your-username>
       spring.datasource.password=<your-password>
       spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
       spring.jpa.hibernate.ddl-auto=update
       spring.jpa.show-sql=true

#### 2. Navigate to the project directory :

    cd RentRead

#### 3. Build the project :

    gradle clean build 

#### 4. Run the application :

To run the application, use the following command

    java -jar build/libs/RentRead-0.0.1-SNAPSHOT.jar

#### 5. Accessing the APIs :

Once the application is running, you can access the API at 

    http://localhost:8080



## Running Tests :

To run the unit tests :

    gradle test

## Endpoints :

### 1. Public Endpoints
- User Registration: POST /users/register
- User Login: POST /users/login

### 2. Private Endpoints

#### Book Management (Admin only):
- Create Book: POST /books
- Update Book: PUT /books/{bookId}
- Delete Book: DELETE /books/{bookId}

#### Rental Management (Authenticated Users):
- Rent a Book: POST /books/{bookId}/rent
- Return a Book: POST /books/{bookId}/return
- Browse Books: GET /books (available to all authenticated users)

## Postman Collection

A Postman collection has been included to test the API endpoints. Import the collection into Postman and start testing the API.

[https://www.postman.com/omkar2363/workspace/rentread/collection/28208818-2cbcc214-0f16-41a8-883e-150b97480255?action=share&creator=28208818
](RentRead Postman Collection)

## Project Structure

The project follows a layered architecture approach:

- `entities` : Contains the entity classes.
- `controllers`: Handles incoming HTTP requests and responses.
- `services`: Contains the logic for managing users books and rentals.
- `repositories`: Interacts with the MySQL Database.
- `exceptions` : Contains the custom exceptions and exception handler classes. 



## Validation and Error Handing

- Basic validation is implemented to check student is registered for subject or not before registering for the exam of the subject.
- Common errors are handled, and appropriate HTTP status codes are returned (e.g., 404 for User not found).

## Licence

All the copy rights belongs to Omkar Yadav.