**Wildlife Conservatory Donation Management Microservice**

This project is a microservice-based application developed using Spring Boot and MySQL to manage symbolic donations made to a wildlife conservatory. 
The system supports various features, including creating wildlife profiles, managing donor profiles, processing donations, and integrating with other essential microservices. 
The architecture includes service discovery, API gateway, security, resilience, event-driven communication, and monitoring.

***Features Overview***

- Wildlife Profile Management: Create and maintain wildlife profiles with information such as species, endangered status, and description.
- Donor Profile Management: Manage donor registration, profile updates, and preferences.
- Donation Management: Process symbolic donations to wildlife profiles and track donation histories.
- Notification: Send notifications to donors upon successful donations.
- Payment Processing: Integrate with payment gateways to handle donations.
- API Gateway: Central routing for service requests, with security and load balancing.
- Resilience: Circuit breaker and fault tolerance for reliable communication.
- Event-Driven Architecture: Asynchronous communication using Kafka for scalability.
- Monitoring: Use Prometheus and Grafana for monitoring and observability.


***Microservices***

`Wildlife Service`

Manages CRUD operations for wildlife profiles.
Maintains data on species, status (e.g., endangered), and other relevant information.

`Donor Service`

Handles donor registration, profile updates, and preferences.
Provides donor information for accessing donation history.

`Donation Service`

Processes symbolic donations and associates them with donor and wildlife profiles.
Maintains a record of donation histories and calculates total contributions.

`Notification Service`



`Payment Service`

Integrates with payment providers (e.g., Stripe, PayPal) to process donation payments.
Manages payment transactions, confirmations, and failures.

`API Gateway (Spring Cloud Gateway)`

Routes requests to appropriate microservices.
Implements security measures like authentication and authorization via Keycloak.
Supports rate limiting and load balancing.

`Service Discovery (Netflix Eureka)`

Registers and discovers microservices dynamically.
Provides load balancing for service communication.

`Resilience and Fault Tolerance (Resilience4j)`
Implements circuit breaker patterns to handle service failures .
Provides retry mechanisms for failed requests.

`Event-Driven Architecture (Kafka)`
Facilitates communication between microservices through message queues.
Asynchronous processing for events such as donation confirmations.
Monitoring (Prometheus and Grafana)
Tracks key metrics like request counts, latencies, and system health.
Visualizes data on Grafana dashboards for real-time monitoring.


**Getting Started**

1. Clone the Repository: Clone this project to your local machine.
2. Configure MySQL Database: Set up a MySQL database for the application.
3. Configure Services: Update configurations for each microservice (e.g., database connections, Kafka settings, Keycloak integration).
4. Run Services with Docker: Use Docker Compose to start all services.
5. Access the Application: Access the services via the API Gateway.

**Prerequisites**

- Java 17+
- Spring Boot 3.x
- Docker
- MySQL
- Kafka
- Prometheus and Grafana
- Keycloak for authentication

**Additional Information**

Service Discovery: Uses Eureka for discovering and registering microservices.
Security: Managed through Keycloak for user authentication and authorization.
Monitoring and Logging: Integrated with Prometheus for metrics and Grafana for visualization.
Containerization: Each microservice is Dockerized for consistent deployment.
