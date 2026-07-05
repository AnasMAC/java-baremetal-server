Bare-Metal Java HTTP Engine 🚀

Building the magic behind @RestController. A zero-dependency Java server demonstrating low-level socket programming, HTTP packet parsing, and ThreadPool concurrency.

📖 Why this project exists

Modern enterprise development relies heavily on frameworks like Spring Boot and embedded servers like Tomcat. While these tools are powerful, they often hide the underlying mechanics of network I/O, thread management, and HTTP protocol parsing.

This project is a bare-metal implementation of an HTTP server built in pure Java (no Spring, no external web dependencies). It is designed to strip away the "magic" and demonstrate a deep, foundational understanding of concurrent systems architecture, raw socket communication, dynamic routing dispatchers, and database connection pooling.

🏗️ Core Architecture & Features

1. Concurrency Engine (Thread Pooling)

A standard single-threaded ServerSocket completely blocks the OS thread during a heavy I/O workload (like a database query), freezing out all other users.

The Solution: Implemented a decoupled HttpServer manager that wraps the server.accept() lifecycle inside a custom ExecutorService (Fixed Thread Pool).

The Result: Memory is isolated per thread, allowing the server to handle concurrent user requests simultaneously without thread starvation.

2. The Dynamic OOP Dispatcher

Instead of relying on hardcoded string-matching or messy if/else blocks, the server uses a proper Object-Oriented routing registry.

Packet Parsing: Dynamically reads raw TCP streams via BufferedReader, extracting HTTP Methods and Paths using Java 8 Streams.

Polymorphism: Uses an abstract Controller class to enforce standard HTTP methods (doGet, doPost, etc.).

Automatic Component Scanning (Reflection): Built a custom directory scanner to auto-detect classes annotated with a custom @RequestMapping at runtime.

The Registry: A HashMap routes parsed URLs directly to dynamically instantiated controller objects in $O(1)$ time.

Engineering Challenge (ClassLoader Isolation): Overcame a critical JVM classpath isolation issue. Initially, a custom ClassLoader reading from src/main/java failed to recognize annotations at runtime due to the JVM treating identical classes loaded by different ClassLoaders as distinct, incompatible types. Resolved by aligning the scanner with Maven's build lifecycle (targeting the target/classes directory) and delegating to the System ClassLoader via Class.forName(), ensuring annotation retention and seamless type matching.

Fail-Safe Design: Automatically falls back to standard 404 Not Found or 400 Bad Request protocols if the routing fails or the packet is malformed.

3. Custom Database Connection Pool & IoC

Modern frameworks like Spring rely on tools like HikariCP and Inversion of Control (IoC). This engine implements its own thread-safe pool and injection mechanism.

Thread-Safe Pooling: Utilizes ArrayBlockingQueue to pre-initialize and manage PostgreSQL connections. Native blocking mechanisms handle thread waiting without CPU-intensive loops.

Dependency Injection (IoC): The Dispatcher acts as an IoC container, instantiating the Connection Pool as a Singleton and injecting it into Controllers via Reflection at runtime.

Server-Side Rendering (SSR): Complete elimination of external HTML templates by dynamically generating pure HTML/Tailwind dashboards directly within the Java Controller layer, parsing SQL ResultSets into UI tables in real-time.

📊 Benchmarks (Proving the Architecture)

To validate the Thread Pool concurrency, an artificial 5-second processing delay was added to the server logic, and tested using Apache Benchmark:

ab -n 10 -c 10 http://localhost:8080/


Single-Threaded Server: ~50.0 seconds total processing time.

Multi-Threaded Server (Pool of 10): ~10.0 seconds total processing time.

🗺️ Project Roadmap (The 8-Week Sprint)

[x] Phase 1: Core Concurrency - Implemented the ThreadPoolExecutor and isolated memory handling.

[x] Phase 2: The Routing Dispatcher - Built the dynamic HTTP parser, OOP HashMap router, and Reflection-based Component Scanner.

[x] Phase 3: The Database Bottleneck - Connected the server to PostgreSQL, implemented a raw JDBC Connection Pool, and built an SSR dashboard.

[ ] Phase 4: The NIO Refactor - Upgrading the bare-metal server from blocking java.net threads to java.nio event loops (Selector/SocketChannel).

💻 How to Run & Test

1. Database Setup (PostgreSQL)

Before running the server, create a local PostgreSQL database named barmetal and run the following script:

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO users (name, email) VALUES ('Mohamed Anas', 'anas@example.com');


Note: Ensure you update the JDBC URL credentials in CustomConnectionPool.java to match your local setup.

2. Run the Server

Clone the repository:

git clone [https://github.com/AnasMAC/java-baremetal-server.git](https://github.com/AnasMAC/java-baremetal-server.git)


Compile the Java files:

mvn clean compile


Start the server:

mvn exec:java -Dexec.mainClass="com.pfe.prep.App"


Open your browser to test the interactive SSR Dashboard:

http://localhost:8080/user

Developed as an advanced systems engineering exercise. Target: Enterprise Backend Infrastructure.
