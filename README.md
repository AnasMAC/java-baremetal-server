Here is your text fully formatted and optimized for GitHub. I added language tags to the code blocks (Bash, SQL), utilized blockquotes for emphasis, and structured the features and roadmap with clean markdown syntax so it renders perfectly on your repository page.


# Bare-Metal Java HTTP Engine 🚀

> **Building the magic behind `@RestController`.** A zero-dependency Java server demonstrating low-level socket programming, Non-Blocking I/O (NIO), HTTP packet parsing, and Multi-Reactor concurrency.

## 📖 Why this project exists

Modern enterprise development relies heavily on frameworks like Spring Boot and embedded servers like Tomcat or Netty. While these tools are powerful, they often hide the underlying mechanics of network I/O, thread management, and HTTP protocol parsing.

This project is a bare-metal implementation of an HTTP server built in **pure Java** (no Spring, no external web dependencies). It is designed to strip away the "magic" and demonstrate a deep, foundational understanding of concurrent systems architecture, raw socket communication, dynamic routing dispatchers, and event-driven design.

---

## 🏗️ Core Architecture & Features

### 1. Non-Blocking I/O (NIO) & Multi-Reactor Architecture
Moved away from legacy thread-per-request blocking I/O to a highly scalable Event Loop architecture (similar to Node.js libuv or Netty).
* **The Boss/Worker Pattern:** Uses a Master Selector (The Boss) dedicated solely to listening for `OP_ACCEPT` events.
* **The Selectors Pool:** Upon accepting a connection, the Boss uses Round-Robin to hand off the `SocketChannel` to a pool of 10 persistent Worker Event Loops running in an `ExecutorService`.
* **Zero-Blocking:** Workers handle `OP_READ` and `OP_WRITE` via `ByteBuffer`s, ensuring no thread is ever blocked waiting for network packets. Solved advanced NIO edge cases including `BufferOverflowException` and infinite loop memory leaks.

### 2. The Dynamic OOP Dispatcher
Instead of relying on hardcoded string-matching or messy if/else blocks, the server uses a proper Object-Oriented routing registry.
* **Packet Parsing:** Dynamically reads raw TCP streams, extracting HTTP Methods and Paths using Java 8 Streams.
* **Polymorphism:** Uses an abstract `Controller` class to enforce standard HTTP methods (`doGet`, `doPost`, etc.).
* **Automatic Component Scanning (Reflection):** Built a custom directory scanner to auto-detect classes annotated with a custom `@RequestMapping` at runtime.
* **The Registry:** A `HashMap` routes parsed URLs directly to dynamically instantiated controller objects in $O(1)$ time.

### 3. Custom Database Connection Pool & IoC
Modern frameworks rely on tools like HikariCP and Inversion of Control (IoC). This engine implements its own thread-safe pool and injection mechanism.
* **Thread-Safe Pooling:** Utilizes `ArrayBlockingQueue` to pre-initialize and manage PostgreSQL connections. Native blocking mechanisms handle database connection waiting without CPU-intensive loops.
* **Dependency Injection (IoC):** The Dispatcher acts as an IoC container, instantiating the Connection Pool as a Singleton and injecting it into Controllers via Constructor Injection.

### 4. Server-Side Rendering (SSR)
Complete elimination of external HTML templates by dynamically generating pure HTML/Tailwind dashboards directly within the Java Controller layer, parsing SQL `ResultSet`s into UI tables in real-time.

---

## 📊 Benchmarks (Proving the Architecture)

To validate the Multi-Reactor Event Loop vs. the legacy blocking model, an artificial 5-second processing delay was added to the server logic to simulate heavy DB load. Tested using Apache Benchmark:

```bash
ab -n 10 -c 10 http://localhost:8080/

```

* **Legacy Single-Threaded Server:** ~50.0 seconds total processing time.
* **NIO Multi-Reactor Server (Pool of 10):** ~10.0 seconds total processing time (5x Speedup).

---

## 🗺️ Project Roadmap (The 8-Week Sprint)

* [x] **Phase 1: Core Concurrency** - Implemented the `ThreadPoolExecutor` and isolated memory handling.
* [x] **Phase 2: The Routing Dispatcher** - Built the dynamic HTTP parser, OOP `HashMap` router, and Reflection-based Component Scanner.
* [x] **Phase 3: The Database Bottleneck** - Connected the server to PostgreSQL, implemented a raw JDBC Connection Pool, and built an SSR dashboard.
* [x] **Phase 4: The NIO Refactor** - Upgraded the bare-metal server from blocking `java.net` threads to `java.nio` event loops (`Selector`/`SocketChannel`) using the Boss/Worker pattern.

---

## 💻 How to Run & Test

### 1. Database Setup (PostgreSQL)

Before running the server, create a local PostgreSQL database named `barmetal` and run the following script:

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO users (name, email) VALUES ('Mohamed Anas', 'anas@example.com');

```

> **Note:** Ensure you update the JDBC URL credentials in `CustomConnectionPool.java` to match your local setup.

### 2. Run the Server

Clone the repository:

```bash
git clone [https://github.com/AnasMAC/java-baremetal-server.git](https://github.com/AnasMAC/java-baremetal-server.git)

```

Compile the Java files:

```bash
mvn clean compile

```

Start the server:

```bash
mvn exec:java -Dexec.mainClass="com.pfe.prep.App"

```

Open your browser to test the interactive SSR Dashboard: [http://localhost:8080/user](https://www.google.com/search?q=http://localhost:8080/user)

---

*Developed as an advanced systems engineering exercise. Target: Enterprise Backend Infrastructure.*

```

```
