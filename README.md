# SariDB: A Lightweight Key-Value Database

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java%20Version-JDK%2021%2B-blue.svg)](https://www.oracle.com/java/technologies/javase-downloads.html)
![Built with Love](https://img.shields.io/badge/Built%20with%20♥️-fdb0c0)

## Overview

SariDB is a minimalist, file-reconstructible key-value database. It can be embedded directly within your application or deployed as a standalone solution, providing excellent flexibility and performance.

## Features

- **Embedded & Standalone:** Whether you're building compact applications or standalone services, SariDB fits seamlessly into your Java projects.

- **File-based Reconstruction:** Ensure data persistence and quick recovery with SariDB's file-based reconstruction feature built on Apache Parquet, making it robust against unexpected shutdowns.

- **Efficient Key-Value Storage:** 

- **Concurrency First:** SariDB provides built-in concurrency safety mechanisms, allowing multiple threads to operate on the database simultaneously without compromising data consistency.

## Usage Example
(Subject to change)

```java
// Embedded Deployment Example
import com.example.SariDB.db.SariDB;

// Initialize embedded database
SariDB inMemoryStore = SariDB.builder().embedded(true).build();

// Store key-value pair
inMemoryStore.put("key1", "value1");

// Retrieve value by key
String value = .get("key1");
System.out.println(value); // Output: value1

// Initialize standalone database
SariDB standaloneDB = SariDB.builder().embedded(false).filePath("db.parquet").build();

// Store key-value pair
standaloneDB.put("key2", "value2");

// Retrieve value by key
String value = standaloneDB.get("key2");
System.out.println(value); // Output: value2
```


## Add to your project (NOT YET IMPLEMENTED -- SOON!)
## Downloads

[Download latest version (Not yet implemented)](https://google.com) or add to `pom.xml`:

```XML
<dependency>
    <groupId>com.SariDB</groupId>
    <artifactId>SariDB</artifactId>
    <version>1</version>
</dependency>
```
