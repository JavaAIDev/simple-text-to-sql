# Simple Text to SQL (No RAG)

[![build](https://github.com/JavaAIDev/simple-text-to-sql/actions/workflows/build.yaml/badge.svg)](https://github.com/JavaAIDev/simple-text-to-sql/actions/workflows/build.yaml)

> See JavaAIDev [article](https://javaaidev.com/docs/rag/samples/text-to-sql) for more details.

Test data: [Netflix data](https://github.com/neondatabase/postgres-sample-dbs?tab=readme-ov-file#netflix-data)

How to run:

1. Requires Java 21 to build and run.
2. Start Postgres database using Docker Compose.
3. Start the server and use [Swagger UI](http://localhost:8080/swagger-ui/index.html) to run query.

Sample query:

```text
how many movies are produced in the United States?
```

Output:

```text
There are 2,058 movies produced in the United States.
```

## Full Text-to-SQL Implementation

For a complete Text-to-SQL implementation, check out my [course](https://www.udemy.com/course/spring-ai-text-to-sql/?referralCode=6180D9A02FA8BA9D4F60).