### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
  - credentials for login while calling the api
    -user: admin
    -password: admin
- H2 UI : http://localhost:8080/h2-console
  -user: sa
  -password:



> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.

> Note that the data.sql file is included in the git as it inserts the five datas into the database which would be easir for testing the application


### Tasks Done
#### Testing
- Implemented unit testing and integration testing for all the components i.e. services, controllers, additional classes etc.
#### Controller Changes
- Added the Partial Update feature using PATCH on route PATCH /api/v1/employees/{id}
- Implemented validation in the create, update(both PUT and PATCH), delete, list and retrieve- 
- Implemented searching, sorting  in the GET /api/v1/employees with default parameters
- Implemented searching, sorting and pagination in the GET /api/v1/paginated-employees with default parameters

#### Logging
- Implemented logging and logged the messages and exceptions
- File and console logging with the seperate file for log level ERROR
- Different loggin settings for the prod and dev environment

#### Comments and Cacheing
- Proper Comments throughout the code
- Database Cacheing logic implemented

#### Authentication
- Basic authentication implemented with inmemory authentication


#### Other Changes
- Implemented custom Exception handling
- Created global exception handler
- Custom annotation created to check if atleast one of the fields is provided while partially updating the employee
- Created EmployeeDTO

### Tasks that could be done if given more time
- SSO or JWT authentication could be implemented
- authentication implementation on the database level
- implement the session time out
- add the parameter on the /api/v1/employees and /api/v1/paginated-employees for the multiple fields filetring with or, and condition
  - filter Map<String,String> where Map<Field,FilterValue> 
  - filterCondition String where value can be "and" and "or"
- No SQL database for high speed performane while scaling the application


#### Restrictions
- use java 8


#### Your experience in Java
 - I have more than 3 years of web development experience in various programming languages i.e. Java, PHP, JavaScript (React, Angular, JQuery), Python (Django)
 - I have a little more than 8 months of experience in Java and Spring Boot  and I started development in Spring Boot from 2022/07 
