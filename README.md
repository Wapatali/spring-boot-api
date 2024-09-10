# About

This is a small API built with _Spring Boot_ (version 3.3.0) that lets you manage users using _Json Web Tokens_ as an authentication and authorisation system.
The application connects to a _Postgresql_ database (version 16.3) and automatically performs migrations with _Flyway_ to update the schema at launch.
It is a proposal for a small code base, respecting standards and best practices as far as possible.

## Environment variables

In order to connect to the database and configure JWT, the following environment variables must be defined when the application is launched:

- `DB_USER` Database username
- `DB_PASSWORD` Database password
- `DB_URL` URL to the database of type `jdbc:postgresql://host:port/database_name` 
- `JWT_SECRET` Secret used for JWT signature
- `JWT_LIFETIME` Token lifetime expressed in milliseconds