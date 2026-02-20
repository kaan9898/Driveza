# Driveza
Spring boot application for car rentals.
## Features
The main features of Driveza are:
* User side featuring:
  * map showing available rental cars,
  * list of available cars, and the option to rent and return a car
* Admin panel, where admins manage users, cars, rentals
## Setup
### Env
To use this app you need to provide a Postgres database. Postgres database information has to be passed in using an `.env` file:
```
spring.datasource.url=jdbc:YOUR_DB_URL
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD
```
#### Postgres
Optionally you can use this Docker command to set up the database:
```
docker run --name driveza -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:18.2
```

In this case `DB_USER` is `postgres`, `DB_PASSWORD` is `postgres` and `DB_URL` is `postgresql://postgres:postgres@localhost:5432/postgres`
#### OAuth2
If you want Google OAuth2 support you need to add these to `.env`:
```
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
```
#### Schema
**For the first run you have to change `application.properties` with**
```
spring.jpa.hibernate.ddl-auto=create
```
So that hibernate can generate database schema, after the first run this can be changed back to `none` so that DB data isn't dropped.
### Run
To run the app you now only have to run, but if you have multiple Java versions ensure JDK25 is used by specifying `JAVA_HOME`
```
./mvnw spring-boot:run
```
or on Windows
```
mvnw.cmd spring-boot:run
```
