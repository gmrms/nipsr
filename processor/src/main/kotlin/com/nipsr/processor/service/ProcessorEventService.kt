package com.nipsr.processor.service

import com.mongodb.client.result.InsertOneResult
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.service.EventService
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped

@NIP_01
@ApplicationScoped
class ProcessorEventService : EventService() {

    suspend fun persist(event: Event<*>): InsertOneResult =
        collection.insertOne(event).awaitSuspending()

    suspend fun deleteByIdsAndPubkey(ids: List<String>, pubkey: String): Long =
        collection.deleteMany(doc("_id", doc("\$in", ids)).append("pubkey", pubkey)).awaitSuspending().deletedCount

    suspend fun deleteOldersOfKindAndPubkey(createdAt: Long, kind: Int, pubkey: String): Long =
        collection.deleteMany(
            doc("kind", kind)
                .append("pubkey", pubkey)
                .append("created_at", doc("\$lt", createdAt))
        ).awaitSuspending().deletedCount

}