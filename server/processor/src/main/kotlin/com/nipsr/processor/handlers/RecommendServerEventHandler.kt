package com.nipsr.processor.handlers

import com.nipsr.payload.model.events.RecommendServerEvent
import com.nipsr.payload.nips.NIP_01
import com.nipsr.processor.handlers.spec.EventHandler
import javax.enterprise.context.ApplicationScoped

@NIP_01
@ApplicationScoped
class RecommendServerEventHandler : EventHandler<RecommendServerEvent>() {

    override suspend fun handleEvent(event: RecommendServerEvent) {
    }

    override fun handlesType() = RecommendServerEvent::class

}