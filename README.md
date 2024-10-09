# Simple Text to SQL (No RAG)

Test
data: [Netflix data](https://github.com/neondatabase/postgres-sample-dbs?tab=readme-ov-file#netflix-data)

How to run:

1. Requires Java 21 to build and run.
2. Download test data and import into a Postgres database.
3. Update database connection in `application.yaml`.
4. Start the server and
   use [Swagger UI](http://localhost:8080/swagger-ui/index.html) to run query.

Sample query:

```text
how many movies are produced in United States?
```

Output:

```text
There are 2,058 movies produced in the United States.
```