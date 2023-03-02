package com.nipsr.processor.service

import com.nipsr.payload.events.Event
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.service.EventService
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped

@NIP_01
@ApplicationScoped
class ProcessorEventService : EventService() {

    suspend fun persist(event: Event<*>) =
        collection.insertOne(event).awaitSuspending()

    suspend fun replaceLastOfKindOfPubkeyBy(event: Event<*>) =
        collection.findOneAndReplace(
            doc("kind", event.kind).append("pubkey", event.pubkey),
            event
        ).awaitSuspending()

}