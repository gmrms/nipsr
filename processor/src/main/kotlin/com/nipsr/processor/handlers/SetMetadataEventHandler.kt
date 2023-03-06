package com.nipsr.processor.handlers

import com.nipsr.payload.model.events.SetMetadataEvent
import com.nipsr.payload.nips.NIP_01
import com.nipsr.processor.handlers.spec.EventHandler
import javax.enterprise.context.ApplicationScoped

@NIP_01
@ApplicationScoped
class SetMetadataEventHandler : EventHandler<SetMetadataEvent>() {
    override suspend fun handleEvent(event: SetMetadataEvent) {}

    override fun handlesType() = SetMetadataEvent::class
}