# newrelic1


## Description

New Relic demo project.

## Requirements and Basic Configuration

* Java 21 or later
* Gradle 8 (via local wrapper)
* Database compatible with Spring
* Spring Boot (currently 3.3.1)
* Git

Currently the project is configured for MySQL (port 3306) but can be easily changed.
* Database: newrelic
* Username: newrelic
* Password: newrelic 

The web service is configured for port 3000.

## Setup

The project is hosted on GitHub and is built using Gradle. To checkout and build, including running unit tests:


```
git clone https://github.com/bobd-biz/newrelic1.git 
cd newrelic1 
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
To start serving the microservices (configured on port 3000):
```
./gradlew bootRun
```

## Design Considerations and Shortcomings

* Services accept and return JSON.
* No authentication and authorization are provided, so no HTTPS or OpenId/OAuth2.
* The two different services (companies and customers) are packaged together for convenience.
* Spring Webflux is used to implement the REST services in an attempt to provide asynchronous performance. However, no testing or analysis has been done to verify the services actually operate without blocking.
* Error handling is by no means complete. For instance, if a resource is not found the service returns a 404 status code, but no detailed error details.
* Lombok is used to create class scaffolding such as getters.
* Flyway is used to manage database schema changes.
* Some Spring libraries, such as validator and actuator, are included in the configuration but not yet exploited.
* Create services return a 201 status but do not provide a URI for the created object.
* The Customer object currently has a String Id. This is actually intended to be a UUID to make creation across services more robust.
* Although JPA is used, no work has been done with transactions (which aren't likely to be needed anyway for these services).
* Spring Routes are used for service dispatch. Some of the services are only differentiated using query parameters, making the routing predicates a little clumsy. There may be a better approach.
* The API is not versioned. In any event my preference is use headers instead of including version in the path.
* Used Copilot to generate initial unit tests

