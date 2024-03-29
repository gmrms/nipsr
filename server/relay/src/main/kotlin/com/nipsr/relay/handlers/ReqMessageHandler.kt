package com.nipsr.relay.handlers

import com.nipsr.payload.Constants.FILTERS
import com.nipsr.payload.Constants.SUBSCRIPTION_ID
import com.nipsr.payload.ObjectMapperUtils.mapTo
import com.nipsr.payload.model.Filter
import com.nipsr.payload.nips.NIP_01
import com.nipsr.payload.nips.NIP_15
import com.nipsr.relay.exeptions.RelayException
import com.nipsr.relay.handlers.spec.MessageHandler
import com.nipsr.relay.handlers.spec.MessageParts
import com.nipsr.relay.handlers.spec.MessageSpec
import com.nipsr.relay.message.Message
import com.nipsr.relay.message.MessageType
import com.nipsr.relay.message.SessionMessageExtension.send
import com.nipsr.relay.model.SessionsContext
import com.nipsr.relay.model.Subscription
import com.nipsr.relay.service.RelayEventService
import javax.enterprise.context.ApplicationScoped
import javax.websocket.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.eclipse.microprofile.metrics.MetricUnits
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.metrics.annotation.Timed

/**
 * Handles the "REQ" message type.
 */
@NIP_01
@ApplicationScoped
class ReqMessageHandler(
    private val eventService: RelayEventService
) : MessageHandler {

    companion object {
        const val TAG_PREFIX = "#"
    }

    @Counted(name = "REQ-TotalMessages", unit = MetricUnits.HOURS)
    @Timed(name = "REQ-MessageProcessingDuration", unit = MetricUnits.MILLISECONDS)
    override suspend fun handleMessage(sessionsContext: SessionsContext, messageParts: MessageParts): Unit =
        coroutineScope {
            val (subscriptionId, filters) = extractParts(messageParts)

            with(sessionsContext.currentSession.info) {
                val subscription = Subscription(subscriptionId, filters)
                // If the subscription already exists, remove it and add it again to update the filters
                if(!this.subscriptions.add(subscription)) {
                    this.subscriptions.remove(subscription)
                    this.subscriptions.add(subscription)
                }
                for(filter in filters){
                    launch(Dispatchers.IO) {
                        try {
                            broadcastEvents(filter, subscription, sessionsContext.currentSession.session)
                        } catch (e: Exception) {
                            throw RelayException("Error while fetching events for subscription: $subscriptionId", e)
                        }
                    }
                }
            }
        }

    private suspend fun broadcastEvents(filter: Filter, subscription: Subscription, currentSession: Session) {
        val events = eventService.findByFilters(filter)
        events.onCompletion {
            @NIP_15
            currentSession.send(
                Message(MessageType.EOSE, subscription.id)
            )
        }.collect { event ->
            currentSession.send(
                Message(MessageType.EVENT, subscription.id, event)
            )
        }
    }

    private fun extractParts(messageParts: MessageParts): Pair<String, List<Filter>> {
        val subscriptionId = messageParts[spec(SUBSCRIPTION_ID)].first() as String
        val filtersParts = messageParts[spec(FILTERS)]
        val filters = arrayListOf<Filter>()
        for(filter in filtersParts){
            val tags = extractTags(filter as HashMap<String, Any>)
            if(tags.isNotEmpty()){
                filter["tags"] = tags
            }
            filters += filter.mapTo(Filter::class.java)
        }
        return subscriptionId to filters
    }

    private fun extractTags(filterMap: HashMap<String, Any>): List<List<String>> {
        val tags = mutableListOf<List<String>>()
        for((key, value) in filterMap){
            if(key.startsWith(TAG_PREFIX)){
                tags.add(
                    listOf(
                        key.removePrefix(TAG_PREFIX),
                        *(value as List<String>).toTypedArray()
                    )
                )
            }
        }
        for(tag in tags){
            filterMap.remove("$TAG_PREFIX${tag[0]}")
        }
        return tags
    }

    override fun handlesType() = MessageType.REQ

    override fun messageSpec() = MessageSpec().apply {
        put(SUBSCRIPTION_ID, 1)
        put(FILTERS, 2..Int.MAX_VALUE)
    }

}