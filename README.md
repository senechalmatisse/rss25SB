# 📰 RSS25SB – RESTful Service for Custom RSS Feeds

[![Java 17](https://img.shields.io/badge/Java-17-blue?logo=java)](https://adoptopenjdk.net/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-orange?logo=apache-maven)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)](https://www.postgresql.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green)](LICENSE)

> **RSS25SB** is a modular, production-ready RESTful web service designed to handle custom RSS feed operations, based on the `rss25SB` XML format. It supports full CRUD capabilities, schema validation via XSD, HTML rendering with XSLT, and integrates conversion utilities for external RSS formats.

---

## 📚 Table of Contents

- [📘 Introduction](#-introduction)
- [✨ Features](#-features)
- [🏗️ Architecture](#-architecture)
  - [📂 Package Structure](#-package-structure)
- [🌐 REST API Endpoints](#-rest-api-endpoints)
- [🖥️ Client Applications](#%EF%B8%8F-client-applications)
- [🚀 Getting Started](#-getting-started)
  - [✅ Prerequisites](#-prerequisites)
  - [⚙️ Configuration](#%EF%B8%8F-configuration)
  - [▶️ Running Locally](#%EF%B8%8F-running-locally)
- [📫 Postman Collection](#-postman-collection)
- [👨‍💻 Author](#-author)

---

## 📘 Introduction

**RSS25SB** is a modular, production-ready RESTful web service designed for managing custom RSS feeds that conform to the `rss25SB` XML schema specification.

Crafted using **Spring Boot**, **JAXB**, **XSLT**, and **PostgreSQL**, this application delivers:

- 🔁 Full CRUD support for RSS articles via REST endpoints
- 🧪 Automatic XML validation against an XSD schema
- 🎨 Dual-format rendering (machine-readable XML & styled HTML)
- 🛡 Robust and structured error handling in both XML and HTML
- 🖥️ Client-side tools for uploading and converting external RSS feeds
- ☁️ Seamless deployment on CleverCloud for real-world use

---

## ✨ Features

### 🏠 Home & Help Pages
- `GET /` – Returns project metadata (name, version, author, university logo)
- `GET /help` – Displays documentation listing all available REST endpoints

### 📄 Article Listing
- `GET /rss25SB/resume/xml` – Returns a summarized list of articles in XML format
- `GET /rss25SB/resume/html` – Renders the same summary as a clickable HTML table

### 📌 Article Detail
- `GET /rss25SB/resume/xml/{id}` – Retrieves full article content by ID (XML)
- `GET /rss25SB/html/{id}` – Renders article content as an HTML page

### ➕ Insert & ❌ Delete
- `POST /rss25SB/insert` – Accepts a valid XML `rss25SB` feed and adds it to the database
- `DELETE /rss25SB/delete/{id}` – Removes an article by ID and cleans up the feed if empty

### 🛠️ Client Tools
- **Transfer App** – Uploads a local `rss25SB` XML file to the service via POST request
- **Conversion App** – Converts external RSS (e.g. LeMonde) into `rss25SB` XML and uploads it automatically

### 🧪 Validation & Error Handling
- All incoming XML is validated against the `rss25SB.xsd` schema
- Errors return structured responses in XML or HTML with:
  - `status`, `id`, and `description`
- Global error handler (`ErrorController`) provides consistent diagnostics for invalid HTTP routes or failures

---

## 🧱 Architecture

The `rss25SB` project follows a clean, modular structure inspired by best practices from Clear Architecture and Spring Boot organization principles. Each package fulfills a specific responsibility, ensuring maintainability, scalability, and separation of concerns.

### 📦 Package Structure

```
src/main/java/com/rss25sb/
├── client
│   └── config/           # 🔧 Centralized REST client configuration (e.g., Rss25SBClientProperties)
├── controllers/          # 🌐 REST controllers (Index, Help, Resume, Insert, Delete, Item)
├── converter/            # 🔁 Parsers and converters for external RSS formats (e.g., LeMonde)
├── dto/                  # 📤 JAXB-based Data Transfer Objects (InsertResponse, ErrorResponse…)
├── exception/            # 🚨 Global exception handler (e.g., CustomErrorController)
├── model/
│   ├── adapter/          # 🔧 JAXB adapters (e.g., OffsetDateTimeXmlAdapter)
│   ├── db/               # 🧩 JPA entities mapped to database tables (ItemEntity, AuthorEntity)
│   └── xml/              # 📰 JAXB models aligned with rss25SB XSD (Feed, Item, Link…)
├── repository/           # 🗃 Spring Data JPA interfaces for persistence (ItemRepository)
├── service/              # ⚙️ Business logic and use case services (ItemService, ProjectInfoService)
└── utils/
    ├── constants/        # 📌 Global constants (XSD paths, status codes…)
    ├── mapper/           # 🔄 Entity ↔ XML object mapping logic (ItemMapper)
    └── tools/            # 🧰 Utility functions (XmlUtil, DateTimeUtil, HtmlRenderer…)
```
---

## 🌐 REST API Endpoints

The `rss25SB` RESTful service provides a complete set of endpoints for managing custom RSS articles. Each operation is strictly REST-compliant, returns structured XML or HTML, and leverages XSD validation and XSLT rendering where applicable.

### 🏠 Home Page

| Method | Endpoint | Description                              | Response Format |
|--------|----------|------------------------------------------|------------------|
| GET    | `/`      | Displays homepage with project metadata  | HTML (XHTML)     |

---

### 📘 API Overview

| Method | Endpoint | Description                           | Response Format |
|--------|----------|---------------------------------------|------------------|
| GET    | `/help`  | Returns dynamic documentation for API | HTML (XHTML)     |

---

### 📄 Article Summary List

#### 🔸 XML Format

| Method | Endpoint                  | Description                        | Response Format |
|--------|---------------------------|------------------------------------|------------------|
| GET    | `/rss25SB/resume/xml`     | Summarized list of stored articles| XML              |

#### 🔸 HTML Format

| Method | Endpoint                   | Description                          | Response Format |
|--------|----------------------------|--------------------------------------|------------------|
| GET    | `/rss25SB/resume/html`     | Same list rendered in an HTML table | HTML             |

---

### 📌 Single Article Detail

#### 🔸 XML Format

| Method | Endpoint                            | Description                        | Response Format |
|--------|--------------------------------------|------------------------------------|------------------|
| GET    | `/rss25SB/resume/xml/{id}`          | Full article content by ID         | XML              |

#### 🔸 HTML Format

| Method | Endpoint                    | Description                         | Response Format |
|--------|-----------------------------|-------------------------------------|------------------|
| GET    | `/rss25SB/html/{id}`        | Full article rendered via XSLT      | HTML             |

---

### ➕ Insert a New Feed

| Method | Endpoint             | Description                              | Request Body | Response Format |
|--------|----------------------|------------------------------------------|--------------|------------------|
| POST   | `/rss25SB/insert`    | Adds a new `rss25SB` feed (XSD-validated)| XML (Feed)   | XML              |

> 💡 On failure, the service responds with `<status>ERROR</status>` and a meaningful `<description>`.

---

### ❌ Delete an Article

| Method | Endpoint                  | Description                           | Response Format |
|--------|---------------------------|---------------------------------------|------------------|
| DELETE | `/rss25SB/delete/{id}`    | Deletes an article by its numeric ID | XML              |

> ⚠️ If the article does not exist, a structured XML error message is returned.

---

### 🛡️ General Constraints

- All inbound XML is strictly validated using the `rss25SB.xsd` schema.
- HTML rendering is achieved via XSLT transformation.
- Errors are consistently returned in XML or HTML, matching the original request format.
- The `{id}` path parameter refers to a unique positive integer assigned per article.

---

## 🖥️ Client Applications

To facilitate interaction with the `rss25SB` REST API, the project includes two lightweight and ergonomic client-side tools: a **Transfer Application** and a **Conversion Application**. These apps simplify the process of submitting, validating, and transforming custom RSS feeds.

### 📤 Transfer Application

A graphical, user-friendly interface that allows users to:

- 📁 Select a local `rss25SB`-compliant XML file
- ⚙️ Configure the REST server's URL, port, and endpoint path
- 📬 Send the file via an HTTP `POST` request to `/rss25SB/insert`
- ✅ View the server’s response — either the inserted IDs or an error message
- 🛡️ *(Optional)* Enable local XML validation against the `rss25.xsd` schema before upload

### 🔄 Conversion Application

A complementary interface designed to:

- 🗂️ Let users choose an external RSS source (e.g., **Le Monde**)
- 🌍 Automatically retrieve a remote RSS feed:
  `https://www.lemonde.fr/rss/plus-lus.xml`
- 🔁 Convert it into the `rss25SB` XML format using a source-specific parser
- 📤 Forward the transformed XML directly to the REST service using the Transfer App

These two tools are built to work seamlessly together, improving the reliability and user experience of custom RSS feed integration.

---

## 🚀 Getting Started

This section explains how to configure and launch the `rss25SB` REST service locally.

### ✅ Prerequisites

Before running the project, make sure the following components are installed:

- **Java 17**
- **Maven 3.8+**
- **PostgreSQL 15**

### ⚙️ Configuration

1. At the root of the project, create a `.env` file with your database credentials:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<db>
SPRING_DATASOURCE_USERNAME=<username>
SPRING_DATASOURCE_PASSWORD=<password>
PORT=8080
```

2. Export the environment variables into your shell session:

```bash
export $(grep -v '^#' .env | xargs)
```

### ▶️ Running Locally

Start the Spring Boot application using Maven wrapper:

```bash
./mvnw spring-boot:run \
  -Dspring-boot.run.arguments="\
--server.port=$PORT \
--spring.datasource.url=$SPRING_DATASOURCE_URL \
--spring.datasource.username=$SPRING_DATASOURCE_USERNAME \
--spring.datasource.password=$SPRING_DATASOURCE_PASSWORD"
```

Once running, access the service at: 
```text
http://localhost:$PORT/
```

## 📫 Postman Collection

A ready-to-use Postman collection named jrss25SB_senechal.json is available inside the /resources/ directory.
  - Includes all documented REST endpoints with preconfigured HTTP methods, headers, and body payloads
  - Uses an environment variable {{base_url}} to easily switch between:
    - Local server → ```http://localhost:8080```
    - Deployed instance → ```https://rss25sb-senechal.cleverapps.io```

📎 This collection allows you to quickly test all key scenarios, including:
  - Listing and retrieving articles
  - Uploading new feeds
  - Deleting existing items
  - Validating responses and error messages

---

## 👨‍💻 Author
Senechal Matisse – Developer

Université de Rouen – Academic affiliation and project support
