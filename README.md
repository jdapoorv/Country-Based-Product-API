# Country-Based Product API (Ktor)

A Kotlin (Ktor) service that manages products and applies country-specific VAT and idempotent discounts with database-level concurrency guarantees.

---

## Tech Stack

- Kotlin + Ktor (2.x)
- PostgreSQL
- Exposed ORM
- Gradle
- Docker (Postgres)
- JUnit + Ktor Test Host

---

## Features

- Store and query products by country
- Apply discounts idempotently (same discount cannot be applied twice)
- Calculate final price using:
finalPrice = basePrice × (1 - totalDiscount%) × (1 + VAT%)


- Concurrency-safe under simultaneous HTTP requests
- Fully covered with a concurrency test

---

## Prerequisites

- Java 17+
- Docker & Docker Compose
- Gradle (or Gradle Wrapper)

---

## Running Locally

### Start PostgreSQL


docker compose up -d

(Postgres runs on port 5434)

--- 

### Start PostgreSQL

./gradlew run


Server starts at:

http://localhost:8080


## Examples API calls:

Create product (via SQL for simplicity)

INSERT INTO products (id, name, base_price, country)
VALUES ('p1', 'MacBook', 1000, 'Sweden');


### Apply discount (idempotent)

curl -X PUT http://localhost:8080/products/p1/discount \
  -H "Content-Type: application/json" \
  -d '{
    "discountId": "DISC10",
    "percent": 10
  }'

### Get products by country

curl "http://localhost:8080/products?country=Sweden"

Response: 
[
  {
    "id": "p1",
    "name": "MacBook",
    "basePrice": 1000.0,
    "country": "Sweden",
    "discounts": [],
    "finalPrice": 1250.0
  }
]

### Running Tests:

./gradlew clean test
