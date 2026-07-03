Bare-Metal Java HTTP Engine 🚀

Building the magic behind @RestController. A zero-dependency Java server demonstrating low-level socket programming, HTTP packet parsing, and ThreadPool concurrency.

📖 Why this project exists

Modern enterprise development relies heavily on frameworks like Spring Boot and embedded servers like Tomcat. While these tools are powerful, they often hide the underlying mechanics of network I/O, thread management, and HTTP protocol parsing.

This project is a bare-metal implementation of an HTTP server built in pure Java (no Spring, no external web dependencies). It is designed to strip away the "magic" and demonstrate a deep, foundational understanding of concurrent systems architecture, raw socket communication, and dynamic routing dispatchers.

🏗️ Core Architecture & Features

1. Concurrency Engine (Thread Pooling)

A standard single-threaded ServerSocket completely blocks the OS thread during a heavy I/O workload (like a database query), freezing out all other users.

The Solution: Implemented a decoupled HttpServer manager that wraps the server.accept() lifecycle inside a custom ExecutorService (Fixed Thread Pool).

The Result: Memory is isolated per thread, allowing the server to handle concurrent user requests simultaneously without thread starvation.

2. The Dynamic OOP Dispatcher

Instead of relying on hardcoded string-matching or messy if/else blocks, the server uses a proper Object-Oriented routing registry.

Packet Parsing: Dynamically reads raw TCP streams via BufferedReader, extracting HTTP Methods and Paths using Java 8 Streams.

Polymorphism: Uses an abstract Controller class to enforce standard HTTP methods (doGet, doPost, etc.).

The Registry: A HashMap routes parsed URLs directly to instantiated controller objects in $O(1)$ time.

Fail-Safe Design: Automatically falls back to standard 404 Not Found or 400 Bad Request protocols if the routing fails or the packet is malformed.

3. I/O Template Rendering

Safely reads and streams external HTML templates back to the client using FileReader and try-with-resources to prevent memory leaks and dangling file pointers.

📊 Benchmarks (Proving the Architecture)

To validate the Thread Pool concurrency, an artificial 5-second processing delay was added to the server logic, and tested using Apache Benchmark
    
    ab -n 10 -c 10 http://localhost:8080/

Single-Threaded Server: ~50.0 seconds total processing time.

Multi-Threaded Server (Pool of 10): ~10.0 seconds total processing time.

🗺️ Project Roadmap (The 8-Week Sprint)

[x] Phase 1: Core Concurrency - Implemented the ThreadPoolExecutor and isolated memory handling.

[x] Phase 2: The Routing Dispatcher - Built the dynamic HTTP parser and OOP HashMap router.

[ ] Phase 3: The Database Bottleneck - Connecting the server to PostgreSQL and implementing a raw JDBC Connection Pool.

[ ] Phase 4: The NIO Refactor - Upgrading the bare-metal server from blocking java.net threads to java.nio event loops (Selector/SocketChannel).

💻 How to Run

Clone the repository:

    git clone https://github.com/AnasMAC/java-baremetal-server.git

Compile the Java files:

    mvn clean compile


Start the server:

    mvn exec:java -Dexec.mainClass="com.pfe.prep.App"


Open your browser or use curl:

    http://localhost:8080/user (User Template Route)

Developed as an advanced systems engineering exercise. Target: Enterprise Backend Infrastructure.
