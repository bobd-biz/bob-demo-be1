# bob-demo-be1


## Description

Java backend demo project using Spring Boot, WebFlux, etc.

## Requirements and Basic Configuration

* Java 21 or later
* Gradle 8 (via local wrapper)
* Database compatible with Spring
* Spring Boot (currently 3.3.1)
* Git

Currently the project is configured for MySQL (port 3306) but can be easily changed.
* Database: bob-demo-1
* Username: bob-demo-1
* Password: bob-demo-1 

The web service is configured for port 4000 by default.

## Setup

The project is hosted on GitHub and is built using Gradle. To checkout and build, including running unit tests:


```
git clone https://github.com/bobd-biz/bob-demo-be1.git 
cd bob-demo-be1 
./gradlew build
```

Additional Gradle targets include ```clean```, ```test``` and ```bootRun```.

Configuration properties such as the database and service port are found in ```src/main/resources/application.properties```.
For more details see the Spring documentation.

## Testing and Running

To run the unit tests:
```
./gradlew test
```
To start serving the microservices (configured on port 4000):
```
./gradlew bootRun
```

## Design Considerations

* Services accept and return JSON.
* To keep things simpler at least initially, authentication and authorization are not configured, so no HTTPS or OpenId/OAuth2.
* The two different services (companies and customers) are packaged together for convenience.
* Spring Webflux is used to implement the REST services in an attempt to provide asynchronous performance. However, no testing or analysis has been done to verify the services actually operate without blocking.
* Lombok is used to create class scaffolding such as getters.
* Flyway is used to manage database schema changes.
* Some Spring libraries, such as validator and actuator, are included in the configuration but not yet exploited.
* The Customer object has a String Id that the database uses for a UUID. This is intended to make creation across service instances more robust.
* Spring Routes are used for service dispatch. Some of the services are only differentiated using query parameters, making the routing predicates a little clumsy. There may be a better approach.
* The APIs are not versioned. In any event my preference is use headers instead of including version in the path.
* Used Copilot to generate initial unit tests and suggest code patterns.
* The overall approach to class definition takes a functional approach. Classes are idempotent - there are no setters. This begs the question as to whether Java Records should have been used. There appeared to some potential issues with JPA, but that may be due to Records being unfamiliar.
* Given the simple data structures, DAO/DTO classes and Mapstruct were not used.
* A foreign key could be used for the many-one relationship from the customers table to the companies table. For now a NoSQL approach was used, just embedding the company name in the customers table.
* With Webflux the class relationships are slightly different than traditional Spring projects. 
    * *Repositories* deal directly with the database, and code is automatically generated from the repository interface. Unfortunately, Spring only supports reactive operations on NoSQL databases, so the SQL database operations are blocking.
    * *Services* isolate business operations from the database implementation. This should make those operations easier to test. In addition, service operations use the reactive Mono/Flux model, although in the current implementation the database operations block. This might allow long duration operations to be chunked so that individual chunks block for a shorter time.
    * *Handlers* both package operations that mirror the API as well as dealing with translation to and from the external representation. For instance, the handlers extract parameters from the server request and return status codes.
    * *Routes* map the external URL requests into handler requests. The intent is to isolate all the server request and response operations into this layer (although not fully accomplished so far).

## Current Shortcomings

* Error handling is by no means complete. For instance, if a resource is not found the service returns a 404 status code, but no detailed error details.
* Create services return a 201 status but do not return a URI for the created object.
* Although JPA is used, no work has been done with transactions (which aren't likely to be needed anyway for these services).
* Error handling is rudimentary at best. JSON format errors (converting a JSON string to an internal object like Customer), incorrect parameters and not found (404) errors return a simple JSON error message.
* CORS requests are allowed from http://localhost:3000 for GET, PUT, POST, DELETE, HEAD. The intent is to allow the frontend to be packaged on the same host. It's not currently configurable so any change requires a simple code modification.
* The tests are not comprehensive, nor has coverage or performance been checked. The initial intent was to create tests for the highest ROI.
