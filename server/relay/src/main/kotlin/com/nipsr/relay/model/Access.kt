package com.nipsr.relay.model

import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoCompanionBase
import io.quarkus.mongodb.panache.kotlin.reactive.ReactivePanacheMongoEntityBase
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.bson.codecs.pojo.annotations.BsonId
import kotlin.properties.Delegates

@MongoEntity(collection = "access")
class Access : ReactivePanacheMongoEntityBase() {
    @BsonId
    lateinit var pubkey: String
    var expiration by Delegates.notNull<Long>()

    companion object : ReactivePanacheMongoCompanionBase<Access, String> {
        suspend fun existsByPubkey(pubkey: String): Boolean {
            return count("_id", pubkey).awaitSuspending() > 0
        }
    }

}