package com.nipsr.payload.service

import com.nipsr.payload.model.events.Event
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.mongodb.reactive.ReactiveMongoCollection
import javax.annotation.PostConstruct
import javax.inject.Inject
import org.bson.Document
import org.eclipse.microprofile.config.inject.ConfigProperty

abstract class EventService {

    @Inject
    lateinit var mongoClient: ReactiveMongoClient

    @ConfigProperty(name = "quarkus.mongodb.database")
    lateinit var database: String

    lateinit var collection: ReactiveMongoCollection<Event<*>>

    companion object {
        const val COLLECTION_NAME = "events"
        const val DEFAULT_LIMIT = 50
        const val MAX_LIMIT = 200
    }

    @PostConstruct
    fun init() {
        collection =
        mongoClient.getDatabase(database)
            .getCollection(COLLECTION_NAME, Event::class.java)
    }

    fun doc(key: String, value: Any?) = Document(key, value)

}