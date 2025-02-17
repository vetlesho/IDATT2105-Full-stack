# Calculator backend


## Folder structure
calculator-backend/ \
│── src/\
│   ├── main/java/com/example/calculator/\
│   │   ├── controller/        <-- Handles API and HTTP requests\
│   │   │   ├── CalculatorController.java\
│   │   ├── service/           <-- Contains business logic\
│   │   │   ├── CalculatorService.java\
│   │   ├── model/             <-- Defines request/response objects\
│   │   │   ├── CalculationRequest.java\
│   │   │   ├── CalculationResult.java\
│   │   ├──  CalculatorBackendApplication  <-- Backend application\
│── pom.xml\
│── application.properties\

# Swagger
Use swagger to see endpoints directly, and have interactive API documentation: 
http://localhost:8080/swagger-ui.html