package com.nipsr.processor.handlers

import com.nipsr.payload.events.SetMetadataEvent
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SetMetadataEventHandler : EventHandler<SetMetadataEvent>() {

    override suspend fun handleEvent(event: SetMetadataEvent) {
        println("Received set_metadata event")
    }

    override fun handlesType() = SetMetadataEvent::class

}