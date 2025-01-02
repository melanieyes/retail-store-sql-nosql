# Retail Store Management System

## Overview
This project is a **Retail Store Management System** that integrates both SQL (MySQL) and NoSQL (MongoDB and Redis) databases. It demonstrates efficient data management for retail operations by utilizing different database technologies for structured, semi-structured, and unstructured data. The implementation follows the **3-layer architecture** and **MVC design pattern** for modularity and scalability.

---

## Features
- **SQL Integration**: MySQL is used for relational data storage and operations.
- **NoSQL Integration**:
  - MongoDB (Cloud) for managing semi-structured and document-based data.
  - Redis (Cloud) for caching and handling fast key-value store operations.
- **Retail Operations**:
  - Product inventory management.
  - Customer information tracking.
  - Order processing and data synchronization.
- **3-Layer Architecture**: Separates the application into Presentation, Business Logic, and Data Access layers.
- **MVC Design Pattern**: Ensures clean separation of concerns, enhancing code readability and maintainability.

---

## Technologies Used
### Backend:
- **Programming Language**: Java
- **Frameworks and Tools**: JDBC (MySQL), MongoDB Java Driver, Redis Java Client

### Databases:
- **SQL Database**: MySQL
- **NoSQL Databases**: MongoDB (Cloud), Redis (Cloud)

---

## Installation and Setup
### Prerequisites:
1. **Java Development Kit (JDK)** installed.
2. **MySQL Server** installed and running.
3. **MongoDB Cloud Account** set up and a cluster created.
4. **Redis Cloud Account** set up and an instance created.
5. **Maven** for dependency management.

### Steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/melanieyes/retail-store-sql-nosql.git
   cd retail-store-sql-nosql
   ```
2. Configure the databases:
   - **MySQL**:
     - Create a database and import the schema from `sql/schema.sql`.
     - Update MySQL credentials in the `config.properties` file.
   - **MongoDB (Cloud)**:
     - Connect to your MongoDB Cloud cluster and set up collections using the provided scripts in `nosql/mongodb/`.
     - Update MongoDB connection string in the `config.properties` file.
   - **Redis (Cloud)**:
     - Connect to your Redis Cloud instance and configure the connection string in the application.

3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   java -jar target/retail-store-sql-nosql.jar
   ```

---

## Usage
1. **Start the application**.
2. Use the graphical interface or terminal commands to:
   - Add, update, or delete product inventory.
   - Track customer data.
   - Process and manage orders.
   - View logs for database interactions.

---

## Directory Structure
```
.
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   |-- com.retail.controller/      # Controller layer
|   |   |   |-- com.retail.service/         # Business logic layer
|   |   |   |-- com.retail.dao/             # Data access layer
|   |   |-- resources/
|   |       |-- config.properties            # Configuration file
|-- sql/
|   |-- schema.sql                           # MySQL schema
|-- nosql/
|   |-- mongodb/                             # MongoDB setup scripts
|   |-- redis/                               # Redis setup scripts
|-- README.md                                # Project documentation
```


