# spring-mysql-redis-cache
Spring Boot Mysql Redis  REST API Cache example

##
### Prerequisites
- JDK 1.8
- Maven
- Mysql
- Redis

## Quick Start

### Clone source
```
git clone https://github.com/jeonguk/spring-mysql-redis-cache.git
cd spring-mysql-redis-cache
```

### Build
```
mvn clean package
```

### Run
```
java -jar target/spring-mysql-redis-cache.jar
```

##
### Get information about system health, configurations, etc.
```
http://localhost:8091/env
http://localhost:8091/health
http://localhost:8091/info
http://localhost:8091/metrics
```

##
### Swagger-ui REST API Reference
- http://localhost:8080/swagger-ui.html


##
### Redis monitor
- https://redis.io/commands/monitor
