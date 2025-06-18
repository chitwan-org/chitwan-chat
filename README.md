Okay, here's a much smaller, more concise version of the README for your real-time chat application, focusing on the essentials:

---

## README.md (Small Version)

# Real-time Chat App

A simple real-time chat application built with **Spring Boot** (backend) and **Redis Pub/Sub** for message broadcasting via **WebSockets (STOMP)**.

## Features

* Instant message delivery.
* User-to-user private chat.
* Uses Redis as a fast message broker.
* Handles `java.time.LocalDateTime` in messages.

## Technologies

* **Backend:** Spring Boot, Spring Data Redis, Spring WebSocket, Jackson.
* **Message Broker:** Redis.
* **Build:** Maven.

## Prerequisites

* Java JDK (17+)
* Apache Maven
* Redis Server (running on `localhost:6379`)

    * *Docker (easy setup):* `docker run --name my-redis -p 6379:6379 -d redis/redis-stack-server:latest`

## Quick Setup & Run

1.  **Backend:**
    * **Dependencies:** Ensure `pom.xml` has `spring-boot-starter-web`, `spring-boot-starter-websocket`, `spring-boot-starter-data-redis`, and `jackson-datatype-jsr310`.
    * **Config (`application.properties`):**
        ```properties
        spring.data.redis.host=localhost
        spring.data.redis.port=6379
        logging.level.in.chitwan.chat=DEBUG # Essential for troubleshooting
        ```
    * **Redis Config (`RedisConfig.java`):**
        * Configures `ObjectMapper` for `LocalDateTime` (add `JavaTimeModule`, disable `WRITE_DATES_AS_TIMESTAMPS`).
        * Sets `RedisTemplate` to use `GenericJackson2JsonRedisSerializer` with the `ObjectMapper`.
        * Sets up `RedisMessageListenerContainer` to `PSUBSCRIBE` to `user:*` pattern.
    * **Run:**
        ```bash
        mvn clean install
        mvn spring-boot:run
        ```
2.  **Frontend:**
    * A JavaScript client (e.g., using `stompjs`) connects to `/ws`.
    * Subscribes to `/topic/user:{userId}`.
    * Sends messages to `/app/chat.send`.

## Troubleshooting Tip

* **Messages not flowing?** Check your Spring Boot backend console logs (especially `DEBUG` level for `in.chitwan.chat` and `org.springframework.data.redis`). Look for `Delivered message to WebSocket topic...` in `RedisSubscriber` logs. If not, the issue is on the backend processing from Redis.

---
Okay, here's a more concise version of the README for your real-time chat application, focusing on the essentials:

---

## README.md (Condensed)

# Real-time Chat Application

A Spring Boot-based real-time chat application using WebSockets (STOMP) and Redis Pub/Sub. Messages are instantly delivered to specific users.

## Features

* **Instant Messaging:** Real-time chat functionality.
* **User-Specific Delivery:** Messages broadcast to individual user WebSocket topics.
* **Redis Pub/Sub:** Uses Redis as the message broker for scalable message handling.
* **`LocalDateTime` Support:** Correctly handles `java.time.LocalDateTime` in messages.

## Technologies

* **Backend:** Spring Boot, Spring Data Redis, Spring WebSocket, Jackson
* **Message Broker:** Redis
* **Frontend:** Any STOMP-compatible WebSocket client

## Prerequisites

* Java Development Kit (JDK 17+)
* Apache Maven
* Redis Server (running on `localhost:6379` by default)

## Setup & Running

1.  **Clone the project** (if from a repository).
2.  **Ensure Redis is running** (e.g., `docker run --name my-redis -p 6379:6379 -d redis/redis-stack-server:latest`).
3.  **Review `application.properties`** for Redis host/port.
4.  **Important Configurations:**
    * `RedisConfig.java`: Configures `RedisTemplate` with `GenericJackson2JsonRedisSerializer` and registers `ObjectMapper` to handle `LocalDateTime`. It also sets up `RedisMessageListenerContainer` to listen on `user:*` Redis channels.
    * `RedisSubscriber.java`: Contains the `handleMessage` method which receives messages from Redis, deserializes them, and **crucially, sends them to the correct user-specific WebSocket topic (`/topic/user:<receiverId>`)**.
5.  **Build and run the backend:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
6.  **Run your frontend client** that connects to the `/ws` WebSocket endpoint and subscribes to `/topic/user:<YOUR_USER_ID>`.

## Troubleshooting Tips

* **Messages not received by frontend:**
    * Check backend logs for `DEBUG` messages from `RedisSubscriber` (e.g., "Delivered message to WebSocket topic...").
    * Ensure your frontend subscribes to the *exact* topic `/topic/user:<YOUR_USER_ID>`.
    * Use `redis-cli monitor` to confirm `PUBLISH` and `PSUBSCRIBE` commands are happening.

---