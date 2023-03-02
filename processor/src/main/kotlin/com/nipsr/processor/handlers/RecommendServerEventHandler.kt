package com.nipsr.processor.handlers

import com.nipsr.payload.events.RecommendServerEvent
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class RecommendServerEventHandler : EventHandler<RecommendServerEvent>() {

    override suspend fun handleEvent(event: RecommendServerEvent) {
        println("Received recommend_server event")
    }

    override fun handlesType() = RecommendServerEvent::class

}