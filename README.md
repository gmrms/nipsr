<p align="center">
  <img src="https://github.com/gmrms/nipsr/blob/main/client/public/favicon.svg" width="225"/>
</p>

# Nipsr

Nipsr is a distributed Nostr relay implementation using [Quarkus](https://quarkus.io/) and Kotlin for the server and [Astro](https://astro.build) for the client.

It aims to be eficient, scalable and simple to maintain and deploy (with docker) while also simplifying the process of running, managing and configuring an either free or paid relay.

_The project is in it's early stages, so expect a lot of changes and a few bugs._

## Use Nipsr

### Running now

- Public relay `wss://public.nipsr.com`
- ~Private relay `wss://private.nipsr.com`~ (pending)
- Registration page `https://nipsr.com`

### NIP-05

Get an identifier ~[here](https://nipsr.com)~! (coming soon)

Available domains:
- nipsr.io
- nipsr.com
- nipsr.com.br


## Features

- [X] NIP-01 - Basic protocol flow description
- [X] NIP-02 - Contact List and Petnames
- [X] NIP-05 - Mapping Nostr keys to DNS-based internet identifiers
- [X] NIP-09 - Event Deletion
- [X] NIP-11 - Relay Information Document
- [X] NIP-12 - Generic Tag Queries
- [X] NIP-13 - Proof of Work
- [X] NIP-15 - End of Stored Events Notice
- [X] NIP-16 - Event Treatment
- [X] NIP-20 - Command Results
- [X] NIP-22 - Event created_at Limits

## Architecture

### Nipsr

#### Server
- **Relay** - Websocket server that handles the communication with Nostr clients
- **Processor** - Consumes the events from the relay and handles database persistence
- **Management** - Manages users data like NIP-05 identifiers and relay access
- **Payload** - Contains the data model and the serialization/deserialization logic

#### Client
- **Client** - Frontend client for NIP-05 identifier registration

#### Additional technologies

- **Nginx** - Reverse proxy server
- **RabbitMQ** - Message broker
- **MongoDB** - Primary database
- ~~**Redis** - Cache database~~ (to be implemented)

## Quickstart

You can always run a _non-containerized_ version of every software but the following guides assume you have Docker installed to run the broker and database.

1. Package applications on `server` with `./mvnw package`
2. Enter the `local` directory.
3. Start docker compose `docker compose up` or `docker-compose up` depending on your docker version.

Now the application should be accessible at `ws://localhost` for the relay websocker endpoint and `http://localhost:8080` for the client and management api.

## Production Setup

_TODO_
