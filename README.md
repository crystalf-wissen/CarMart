# Car Management Application - Java

## Overview

*   **Add new cars** to the inventory (Hatchback, Sedan, SUV).
*   **Search for cars** based on various criteria (All, Company, Type, Price Range).
*   **Update car prices.**
*   **Mark cars as sold.**

## Design Patterns Used

### 1. Abstract Factory Pattern

*   **Purpose:** To provide an interface for creating families of related objects without specifying their concrete classes.
*   **Implementation:** The application employs the Abstract Factory pattern to manage the creation of different car types (Hatchback, Sedan, SUV).
    *   `CarFactory` interface defines the contract for car creation.
    *   `HatchbackFactory`, `SedanFactory`, and `SUVFactory` are concrete factories responsible for creating specific car type instances.
    *   `CarFactoryProducer` acts as a factory of factories, choosing the appropriate concrete factory based on the desired car type.

### 2. Singleton Pattern

*   **Purpose:** To ensure a class has only one instance and provide a global point of access to it.
*   **Implementation:** The `DatabaseUtil` class is implemented as a Singleton to manage the database connection.
    *   A private constructor prevents external instantiation.
    *   A static instance variable (`con`) holds the single connection instance.
    *   A static `getConnection()` method provides the global access point, ensuring only one connection is created and reused.

## Good Coding Practices Highlighted
*   **Input Validation:**
*   **Error Handling:**
*   **Resource Management:**
