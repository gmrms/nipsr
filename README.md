# Nipsr

This is a distributed Nostr relay implementation using Quarkus and Kotlin.

## NIPs

- [X] NIP-01 - Basic protocol flow description

## Architecture

### Nipsr

- **Relay** - Websocket server that handles the communication with clients
- **Processor** - Consumes the events from the relay and handles database persistence
- **Payload** - Contains the data model and the serialization/deserialization logic

### Additional technologies

- **RabbitMQ** - Message broker
- **MongoDB** - Primary database
- ~~**Redis** - Cache database~~ (to be implemented)

## Quickstart

You can always run a _non-containerized_ version of every software but the following guides assume you have Docker installed
to run the broker and database.

### Local

There are two ways to run the application locally:
1. Using the pre-built images with `docker-compose-local.yml` (only needs docker)
2. Building the containers from source with `docker-compose-dev.yml` (needs java installed)

The fastest way is to run with the pre-built images, but for development purposes you might want to build from source.

#### Building the containers from source
1. Package the application `./mvnw package`
2. Enter the `local` directory.
3. Start docker compose `docker compose -f docker-compose-dev.yml up` or `docker-compose -f docker-compose-dev.yml up`
depending on your docker version.

#### Using the pre-built images
1. Enter the `local` directory.
2. Start docker compose `docker compose -f docker-compose-local.yml up` or `docker-compose -f docker-compose-local.yml up`
depending on your docker version.

With either of those you application should be accessible at `ws://localhost`.

#### Running Nipsr without docker

1. Install, configure and start Nginx (optional), RabbitMQ and MongoDB. (not covered here)
2. Package the application `./mvnw install`.
3. You must configure the environment variables according to the files in `/local/env/`.
4. In both `relay` and `processor` directories: run `./mvnw quarkus:dev` to start the relay application in development 
mode or `java -jar target/quarkus-app/quarkus-run.jar` to run the app jar.

If the ports were not changed, the relay should be accessible at `ws://localhost:8080` and the processor at `http://localhost:8088`
although the processor currently has no endpoints.

### Production

_TODO_
