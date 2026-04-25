# Room Service
Standalone Spring Boot microservice extracted from the HotelVista backend for the room domain.
## Included
- `Room`
- `RoomType`
- `RoomChangeRequest`
- room-specific controllers, services, repositories, DTOs, enums, validators
- ID-based integration points for external services; no ORM relations across service boundaries
- H2 is enabled by default so the service can start without an external database
- Dockerfile for a simple container build/run
## Notes
- You can override datasource settings with environment variables if you want to point it to MariaDB.
- `RoomChangeRequest` is self-contained: it stores booking/customer IDs as plain strings instead of requiring the booking module.
- `RoomType` is the room-service boundary entity; pricing and promotion lifecycle belong to other services and should be accessed via IDs plus API calls.
## Run locally
```powershell
mvn spring-boot:run
```
## Build
```powershell
mvn clean package
```
## Docker
```powershell
docker build -t room-service .
docker run -p 8080:8080 room-service
```