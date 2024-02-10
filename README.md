# SariDB: A Lightweight Key-Value Database

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java%20Version-JDK%2021%2B-blue.svg)](https://www.oracle.com/java/technologies/javase-downloads.html)
![Built with Love](https://img.shields.io/badge/Built%20with%20♥️-fdb0c0)

## Overview

SariDB is a minimalist, file-reconstructible key-value database. It can be embedded directly within your application or deployed as a standalone solution, providing excellent flexibility and performance.

## Features

- **Embedded & Standalone:** Whether you're building compact applications or standalone services, SariDB fits seamlessly into your Java projects.

- **File-based Reconstruction:** Ensure data persistence and quick recovery with SariDB's file-based reconstruction feature built on Apache Parquet, making it robust against unexpected shutdowns.

- **Efficient Key-Value Storage:** Uses efficient hashing to guarantee constant-time reads and is thread-safe out of the box, no configuration needed.

- **Concurrency First:** SariDB provides built-in concurrency safety mechanisms, allowing multiple threads to operate on the database simultaneously without compromising data consistency.

## Usage Example

### Embedded Example

```java
SariDB sariDB = SariDB
                .builder()
                .isEmbedded(true) // Embedded or standalone?
                .filePath("path/to/db.parquet") // Save your .parquet wherever!
                .reconstruct(false) // Reconstruct or start anew?
                .build(); // That's all folks.

sariDB.set("key", "someValue")
sariDB.get("key") // returns "someValue"
```

### Standalone Example
```java
SariDB sariDB = SariDB
                .builder()
                .isEmbedded(true) // Embedded or standalone?
                .filePath("path/to/db.parquet") // Save your .parquet wherever!
                .reconstruct(false) // Reconstruct or start anew?
                .portNumber(1337) // Set the port number..
                .build(); // ..And that's all folks!
sariDB.start(); // Listening on port 1337 🚀!

```


## Add to your project (NOT YET IMPLEMENTED)
### Downloads

[Download latest version (Not yet implemented)](https://google.com) or add to `pom.xml`:

```XML
<dependency>
    <groupId>com.SariDB</groupId>
    <artifactId>SariDB</artifactId>
    <version>1</version>
</dependency>
```
