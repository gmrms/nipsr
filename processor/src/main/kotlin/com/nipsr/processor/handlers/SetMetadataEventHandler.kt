package com.nipsr.processor.handlers

import com.nipsr.payload.model.events.SetMetadataEvent
import com.nipsr.payload.nips.NIP_01
import com.nipsr.processor.handlers.spec.UniquePerKindEventHandler
import javax.enterprise.context.ApplicationScoped

@NIP_01
@ApplicationScoped
class SetMetadataEventHandler : UniquePerKindEventHandler<SetMetadataEvent>() {
    override fun handlesType() = SetMetadataEvent::class
}