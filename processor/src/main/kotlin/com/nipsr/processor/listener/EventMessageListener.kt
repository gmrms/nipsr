package com.nipsr.processor.listener

import com.nipsr.payload.model.events.Event
import com.nipsr.processor.handlers.EventHandler
import io.vertx.core.json.JsonObject
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance
import org.eclipse.microprofile.metrics.MetricUnits
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.metrics.annotation.Timed
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.slf4j.LoggerFactory

@ApplicationScoped
class EventMessageListener(
    private val eventHandlers: Instance<EventHandler<*>>
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Incoming("events")
    @Counted(name = "totalEvents", unit = MetricUnits.HOURS)
    @Timed(name = "eventProcessingDuration", unit = MetricUnits.MILLISECONDS)
    suspend fun consume(eventJson: JsonObject) {
        val event = eventJson.mapTo(Event::class.java)
        logger.trace("Received event '${event.id}'")
        for(handler in eventHandlers){
            if(handler.handlesType().java.isInstance(event)){
                handler as EventHandler<Event<*>>
                handler.handle(event)
            }
        }
        logger.trace("Finished processing event '${event.id}'")
    }

}