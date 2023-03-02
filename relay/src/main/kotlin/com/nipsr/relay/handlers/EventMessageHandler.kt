package com.nipsr.relay.handlers

import com.nipsr.payload.Constants.EVENT
import com.nipsr.payload.ObjectMapperUtils.mapTo
import com.nipsr.payload.events.Event
import com.nipsr.payload.events.KnownKinds
import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.exeptions.RelayException
import com.nipsr.relay.filters.EventFilter
import com.nipsr.relay.filters.FilterType
import com.nipsr.relay.filters.user.AllowNoneEventFilter
import com.nipsr.relay.filters.user.UserEventFilter
import com.nipsr.relay.handlers.spec.MessageHandler
import com.nipsr.relay.handlers.spec.MessageParts
import com.nipsr.relay.handlers.spec.MessageSpec
import com.nipsr.relay.message.Message
import com.nipsr.relay.message.MessageType
import com.nipsr.relay.message.SessionMessageExtension.send
import com.nipsr.relay.model.SessionsContext
import com.nipsr.relay.model.Subscription
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Instance
import org.eclipse.microprofile.metrics.MetricUnits
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.metrics.annotation.Timed
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter

/**
 * Handles the "EVENT" message type.
 *
 * This handler applies the necessaries filters to the event and sends it to the subscribed sessions.
 */
@NIP_01
@ApplicationScoped
class EventMessageHandler(
    @Channel("events")
    private val eventEmitter: Emitter<Event<*>>,
    eventFilters: Instance<EventFilter>,
) : MessageHandler {

    private val filtersGroupedByType = eventFilters.groupBy { it.type() }

    @Counted(name = "EVENT-TotalMessages", unit = MetricUnits.HOURS)
    @Timed(name = "EVENT-MessageProcessingDuration", unit = MetricUnits.MILLISECONDS)
    override suspend fun handleMessage(sessionsContext: SessionsContext, messageParts: MessageParts) {

        val event = messageParts.getAndConvert(spec(EVENT), ::convertEvent)

        val globalFilters = filtersGroupedByType[FilterType.GLOBAL] ?: arrayListOf()

        for(globalFilter in globalFilters){
            continue
            if(!globalFilter.filter(event)){
                throw RelayException("Event denied: invalid signature")
            }
        }

        eventEmitter.send(event)

        for((session, info) in sessionsContext.sessions){
            if(info.subscriptions.isEmpty()){
                continue
            }
            for(subscription in info.subscriptions){
                val userFilter = getFilter(subscription)
                if(userFilter.filter(event)){
                    val message = Message(MessageType.EVENT, subscription.id, event)
                    session.send(message)
                }
            }
        }
    }

    private fun getFilter(subscription: Subscription) = if(subscription.filters != null){
        UserEventFilter(subscription.filters)
    } else {
        AllowNoneEventFilter()
    }

    private fun convertEvent(events: List<Any>): Event<*> {
        val event = events.first()
        if(event !is HashMap<*, *>){
            throw RelayException("Invalid event format")
        }

        val kind = KnownKinds.fromCode(event[Event<*>::kind.name] as Int)
            ?: throw RelayException("Event kind not supported")

        return event.mapTo(kind.type.java) as Event<*>
    }

    override fun handlesType() = MessageType.EVENT

    override fun messageSpec() = MessageSpec().apply {
        put(EVENT, 1)
    }

}