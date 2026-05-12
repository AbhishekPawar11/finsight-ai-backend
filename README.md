
# ⚙️ FinSight AI – Core API Engine

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-Integration-blue)](https://spring.io/projects/spring-ai)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue)](https://www.mysql.com/)

> **An intelligent, secure Spring Boot API designed for high-volume UPI transaction processing and AI-driven insights.**

This repository contains the backend engine for **FinSight AI**. It handles secure user authentication, complex relational data mapping, and integrates with Google Gemini to automate the financial tracking process for the high-volume UPI ecosystem.

## ✨ Key Backend Features
*   **🤖 Intelligent Ingestion & Categorization:** Leverages **Spring AI** and Gemini Pro to parse unstructured PDF statements and automatically classify transactions into categories like Food, Utilities, and Transfers.
*   **🔒 Enterprise-Grade Security:** Implements a stateless security architecture using Spring Security and JWT for robust authentication and authorization.
*   **✅ Human-in-the-Loop (HITL) Architecture:** Features a staging layer that ensures data integrity by allowing users to validate and edit AI classifications before final storage in the database.
*   **📈 Generative Analytics:** Aggregates user data to generate actionable financial strategies and personalized monthly spending summaries via LLM integration.

## 🛠️ Tech Stack
*   **Language:** Java 17
*   **Framework:** Spring Boot 3.x, Spring Data JPA
*   **AI Integration:** Spring AI (Google Gemini)
*   **Database:** MySQL (Optimized via MySQL Workbench)
*   **Build Tool:** Maven

## 🚀 Local Development Setup

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/FinSight-AI-Backend.git](https://github.com/yourusername/FinSight-AI-Backend.git)
   cd FinSight-AI-Backend
2.Database Setup:
Create a local MySQL database named finsight_db.

3.Environment Configuration:
Create src/main/resources/application-local.properties (this file is ignored by git for security):

spring.datasource.url=jdbc:mysql://localhost:3306/finsight_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

# Spring AI / Gemini Configuration
spring.ai.vertex.ai.gemini.api-key=YOUR_API_KEY

4.Run the Application:
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
