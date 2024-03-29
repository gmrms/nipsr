package com.nipsr.relay.handlers

import com.nipsr.payload.Constants.EVENT
import com.nipsr.payload.ObjectMapperUtils.mapTo
import com.nipsr.payload.factory.toEvent
import com.nipsr.payload.model.events.Event
import com.nipsr.payload.model.inputs.EventInput
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.nips.NIP_20
import com.nipsr.relay.exeptions.EventErrorException
import com.nipsr.relay.exeptions.RelayException
import com.nipsr.relay.filters.events.EventFilter
import com.nipsr.relay.filters.FilterType
import com.nipsr.relay.filters.events.user.AllowNoneEventFilter
import com.nipsr.relay.filters.events.user.UserEventFilter
import com.nipsr.relay.handlers.spec.MessageHandler
import com.nipsr.relay.handlers.spec.MessageParts
import com.nipsr.relay.handlers.spec.MessageSpec
import com.nipsr.relay.message.Message
import com.nipsr.relay.message.MessageType
import com.nipsr.relay.message.SessionMessageExtension.send
import com.nipsr.relay.message.SessionMessageExtension.sendResult
import com.nipsr.relay.model.SessionsContext
import com.nipsr.relay.model.Subscription
import com.nipsr.relay.validation.EventValidator
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
    private val eventValidators: Instance<EventValidator>,
    eventFilters: Instance<EventFilter>,
) : MessageHandler {

    private val filtersGroupedByType = eventFilters.groupBy { it.type() }

    @Counted(name = "EVENT-TotalMessages", unit = MetricUnits.HOURS)
    @Timed(name = "EVENT-MessageProcessingDuration", unit = MetricUnits.MILLISECONDS)
    override suspend fun handleMessage(sessionsContext: SessionsContext, messageParts: MessageParts) {
        val event = messageParts.getAndConvert(spec(EVENT), ::convertEvent)

        applyGlobalFilters(event)
        validate(event)
        sendEvent(event)

        @NIP_20
        sessionsContext.currentSession.session.sendResult(event)

        broadcast(sessionsContext, event)
    }

    private fun sendEvent(event: Event<*>) {
        try {
            eventEmitter.send(event)
        } catch (e: Exception) {
            throw EventErrorException(event, "error: error sending event to the event bus.")
        }
    }

    private fun validate(event: Event<*>) {
        val validators = eventValidators.stream()
            .filter { it.kindsToValidate().contains(event.kind) }
            .toList()

        for(validator in validators){
            if(!validator.validate(event)){
                throw EventErrorException(event, "invalid: this event does not match the required format for its kind.")
            }
        }
    }

    private suspend fun applyGlobalFilters(event: Event<*>) {
        val globalFilters = filtersGroupedByType[FilterType.GLOBAL] ?: arrayListOf()
        for(globalFilter in globalFilters){
            val (result, message) = globalFilter.filter(event)
            if(!result){
                throw EventErrorException(event, message)
            }
        }
    }

    private suspend fun broadcast(sessionsContext: SessionsContext, event: Event<*>) {
        for((session, info) in sessionsContext.sessions){
            if(info.subscriptions.isEmpty()){
                continue
            }
            for(subscription in info.subscriptions){
                val userFilter = getFilter(subscription)
                val (result, _) = userFilter.filter(event)
                if(result){
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

        return event.mapTo(EventInput::class.java).toEvent()
    }

    override fun handlesType() = MessageType.EVENT

    override fun messageSpec() = MessageSpec().apply {
        put(EVENT, 1)
    }

}