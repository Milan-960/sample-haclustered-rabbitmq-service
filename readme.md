# HA Clustered RabbitMQ Service with Spring Boot

This project demonstrates the setup of a High Availability (HA) RabbitMQ cluster using Docker and its integration with Spring Boot applications for message publishing and consumption. It showcases a robust messaging solution resilient to node failures.

---

## Table of Contents

1. [Project Objective](#1-project-objective)
2. [Prerequisites](#2-prerequisites)
3. [Project Scope & Implementation Details](#3-project-scope--implementation-details)
   - [Dockerized RabbitMQ Cluster Setup](#dockerized-rabbitmq-cluster-setup)
   - [RabbitMQ High Availability (HA) Configuration](#rabbitmq-high-availability-ha-configuration)
   - [Spring Boot Application Development & Configuration](#spring-boot-application-development--configuration)
   - [Key Implementation Challenges & Solutions](#key-implementation-challenges--solutions)
4. [How to Run the Project](#4-how-to-run-the-project)
5. [How to Test High Availability (HA)](#5-how-to-test-high-availability-ha)
6. [Conclusion & Key Learnings](#6-conclusion--key-learnings)

---

## 1. Project Objective

The primary objective of this project is to gain practical experience in:

- Setting up and managing a High Availability (HA) RabbitMQ cluster using Docker.
- Integrating a Spring Boot application with a clustered RabbitMQ setup.
- Verifying the resilience and fault tolerance of a mirrored queue in a RabbitMQ cluster.

---

## 2. Prerequisites

Before running this project, ensure you have the following installed:

- **Docker Desktop** (includes Docker Engine and Docker Compose)  
  [Download Docker Desktop](https://www.docker.com/products/docker-desktop)

- **Java Development Kit (JDK):** Version 21 or higher  
  Tested with **Zulu OpenJDK 24**

- **Apache Maven:** Version 3.8+  
  [Install Maven](https://maven.apache.org/install.html)

- **IntelliJ IDEA** (Recommended IDE)  
  [Download IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

---

## 3. Project Scope & Implementation Details

### Dockerized RabbitMQ Cluster Setup

- A 3-node RabbitMQ cluster (`rabbitmq1`, `rabbitmq2`, `rabbitmq3`) is defined in `docker-compose.yml`.
- Uses the `rabbitmq:3-management` Docker image with clustering enabled via a shared Erlang cookie.
- Only `rabbitmq1` exposes AMQP (`5672`) and Management UI (`15672`) ports to the host.
- Communication between containers occurs over a dedicated Docker network.

### RabbitMQ High Availability (HA) Configuration

A mirrored queue ensures fault tolerance:

- **Queue:** `q.example`
- **Policy Pattern:** `q\.example`
- **HA Policy:**
  - `ha-mode`: `exactly`
  - `ha-params`: `3`
  - `ha-sync-mode`: `automatic`
- Messages are routed via a direct exchange (e.g., `amq.direct`).

### Spring Boot Application Development & Configuration

Two Spring Boot modules:

- **listener**: Listens for messages from `q.example`
- **sender**: Publishes messages to `q.example`
- **Connection Config (in `application.properties`)**:
  ```properties
  spring.rabbitmq.addresses=localhost:5672
  spring.rabbitmq.username=guest
  spring.rabbitmq.password=guest
  ```

### Key Implementation Challenges & Solutions

### 🐳 Docker Not Found

**Problem:**  
Docker was not found on the system or not accessible from the terminal.

**Solution:**

- Ensure Docker Desktop is installed on your machine.
- Verify that Docker binaries are added to your system `PATH`.
- Restart your terminal or IDE after installation.

---

### 🔐 Credential Errors

**Problem:**  
Encountered the following error: error getting credentials - err: docker-credential-desktop

**Solution:**

- Restart **Docker Desktop**.
- Restart your terminal or development environment.

---

### ☕ Java Compatibility Issues

**Problem:**  
Spring Boot 1.5.1 is not compatible with JDK 21+.

**Solution:**

- Upgraded Spring Boot version to **3.3.1**.
- Updated `pom.xml` with the following configuration:

  ```xml
  <java.version>21</java.version>

  # Key Implementation Challenges & Solutions

  ```

---

### 🌐 Hostname Resolution Failure

**Problem:**
Docker-internal hostnames (`rabbitmq2`, `rabbitmq3`) were not resolvable from outside the Docker network.

**Solution:**

- Point Spring Boot clients to `localhost:5672`, which maps to `rabbitmq1` via port forwarding in `docker-compose`.

---

### 📤 HA Policy Not Applied in RabbitMQ

**Problem:**
Messages were routed to non-mirrored queues instead of high-availability queues.

**Solution:**

- Manually define the HA policy in the RabbitMQ Management UI:

  - Navigate to **Admin > Policies**
  - Add a policy for mirrored queues (e.g., using regex and `ha-mode: all` or `exactly` with node count)

---

# 4. How to Run the Project

## 📁 Clone the Repository

```bash
git clone <your-repo-url>
cd sample-haclustered-rabbitmq-service
```

## ⚒️ Build with Maven

```bash
mvn clean install
```

## 🛠️ Start RabbitMQ Cluster

```bash
docker compose up -d
docker ps  # Check containers are running
```

## 🔧 Configure HA Policy in RabbitMQ

- Visit: [http://localhost:15672](http://localhost:15672)
- Login with: `guest` / `guest`
- Go to: **Admin > Policies**
- Add a new policy:

**Name:** `ha-q-example-policy`
**Pattern:** `q\.example`
**Apply to:** Queues
**Definition:**

```json
{
  "ha-mode": "exactly",
  "ha-params": 3,
  "ha-sync-mode": "automatic"
}
```

## 🚀 Run Applications

In **IntelliJ IDEA**:

1. Open `listener` module → `Listener.java` → Run main method
2. Open `sender` module → `Sender.java` → Run main method

---

# 5. How to Test High Availability (HA)

### 🔍 Observe Normal Flow

- Monitor both **Sender** and **Listener** logs in IntelliJ.
- In RabbitMQ UI ([http://localhost:15672](http://localhost:15672)), go to **Queues > q.example**.
- Verify messages are being published and consumed.
- Confirm that the queue is mirrored on all 3 nodes.

### ❌ Simulate Node Failure

- Stop the master node (`rabbitmq1`):

```bash
docker stop <container_id_of_rabbitmq1>
```

- Observe in RabbitMQ UI: `rabbitmq1` will appear as **down** under the **Nodes** tab.
- Observe in Applications: Message flow should **continue** after a brief reconnection.
- Observe in Queue: Queue master will shift to `rabbitmq2` or `rabbitmq3`.

## 🔁 Restart Node

- Bring back the stopped node:

```bash
docker start <container_id_of_rabbitmq1>
```

- Observe in RabbitMQ UI: `rabbitmq1` reappears as **up**.
- Observe in Queue: Queue mirror will **resynchronize** automatically.

---

# 6. Conclusion & Key Learnings

This project demonstrates:

- ✅ Effective use of **Docker Compose** for clustered RabbitMQ setup.
- ✅ Practical RabbitMQ **HA queue configuration** using policies.
- ✅ Seamless **Spring Boot integration** via auto-configuration.
- ✅ Solving **Java versioning** and build issues when upgrading Spring Boot.
- ✅ Real-world **HA validation** via node failure simulations.

This setup offers a solid foundation for **resilient, fault tolerant, message driven systems**.
