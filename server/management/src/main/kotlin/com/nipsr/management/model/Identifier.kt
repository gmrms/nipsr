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
    lateinit var domain: String

    var expiration by Delegates.notNull<Long>()

    companion object : ReactivePanacheMongoCompanion<Identifier> {
        suspend fun existsByIdentifierDomain(identifier: String, domain: String) =
            count("identifier = ?1 and domain = ?2", identifier, domain).awaitSuspending() > 0

        suspend fun findAllByIdentifierAndDomain(identifier: String, domain: String) =
            find("identifier = ?1 and domain = ?2", identifier, domain).list().awaitSuspending()
    }

}