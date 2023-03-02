package com.nipsr.processor.listener

import com.nipsr.processor.handlers.EventHandler
import com.nipsr.payload.events.Event
import io.vertx.core.json.JsonObject
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance
import kotlinx.coroutines.runBlocking
import org.eclipse.microprofile.metrics.MetricUnits
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.metrics.annotation.Timed
import org.eclipse.microprofile.reactive.messaging.Incoming

@ApplicationScoped
class EventMessageListener(
    private val eventHandlers: Instance<EventHandler<*>>
) {

    @Incoming("events")
    @Counted(
        name = "totalEvents",
        description = "How many messages are received.",
        unit = MetricUnits.HOURS
    )
    @Timed(
        name = "eventProcessingDuration",
        description = "A measure of how long it takes to process a message.",
        unit = MetricUnits.MILLISECONDS
    )
    fun consume(eventJson: JsonObject) {
        val event = eventJson.mapTo(Event::class.java)
        for(handler in eventHandlers){
            if(handler.handlesType().java.isInstance(event)){
                handler as EventHandler<Event<*>>
                handler.handle(event)
            }
        }
        println("finished processing event: $event")
    }

}