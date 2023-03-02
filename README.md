# Nipsr

This is a distributed Nostr relay implementation using Quarkus and Kotlin.

## NIPs

- [X] NIP-01 - Basic protocol flow description

## Architecture

- **Relay** - Websocket server that handles the communication with clients
- **Processor** - Consumes the events from the relay and handles database persistence
- **Payload** - Contains the data model and the serialization/deserialization logic

### Additional technologies

- **RabbitMQ** - Message broker
- **MongoDB** - NoSQL database
