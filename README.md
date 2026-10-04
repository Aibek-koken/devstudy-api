# DevStudy API

## Overview

A small Spring Boot API created for the INF 345 Fundamentals of DevOps course.

## Requirements

- Java 17

## Run

Run the application:

```bash
./scripts/run.sh
```

By default, the application runs on port `8080`.

To use another port:

```bash
PORT=9000 ./scripts/run.sh
```

## Test

Run automated tests:

```bash
./scripts/test.sh
```

Expected output at the end:

```text
TESTS: 4/4
```

## Endpoints

### GET /

Returns:

```text
DevStudy API
```

### GET /healthz

Health check endpoint.

Returns:

```text
ok
```

### GET /topics

Returns:

```json
["Git","Linux","Docker"]
```