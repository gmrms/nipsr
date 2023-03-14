package com.nipsr.management.model

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoCompanion
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoEntity
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlin.properties.Delegates

@MongoEntity(collection = "identifier")
class Identifier : ReactivePanacheMongoEntity() {
    lateinit var pubkey: String
    lateinit var identifier: String

    var expiration by Delegates.notNull<Long>()

    companion object : ReactivePanacheMongoCompanion<Identifier> {
        suspend fun existsByIdentifier(identifier: String) =
            count("identifier", identifier).awaitSuspending() > 0

        suspend fun findAllByIdentifier(identifier: String) =
            find("identifier", identifier).list().awaitSuspending()
    }

}