package com.nipsr.relay.handlers


import com.nipsr.payload.Constants.SUBSCRIPTION_ID
import com.nipsr.payload.nips.NIP_01
import com.nipsr.relay.handlers.spec.MessageHandler
import com.nipsr.relay.handlers.spec.MessageParts
import com.nipsr.relay.handlers.spec.MessageSpec
import com.nipsr.relay.message.MessageType
import com.nipsr.relay.model.SessionsContext
import com.nipsr.relay.model.Subscription
import javax.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.metrics.MetricUnits
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.metrics.annotation.Timed

/**
 * Handles the "CLOSE" message type.
 */
@NIP_01
@ApplicationScoped
class CloseMessageHandler : MessageHandler {

    @Counted(name = "CLOSE-TotalMessages", unit = MetricUnits.HOURS)
    @Timed(name = "CLOSE-MessageProcessingDuration", unit = MetricUnits.MILLISECONDS)
    override suspend fun handleMessage(sessionsContext: SessionsContext, messageParts: MessageParts) {
        with(sessionsContext.currentSession.info) {
            this.subscriptions.remove(
                Subscription(messageParts[spec(SUBSCRIPTION_ID)].first() as String)
            )
        }
    }

    override fun handlesType() = MessageType.CLOSE

    override fun messageSpec() = MessageSpec().apply {
        put(SUBSCRIPTION_ID, 1)
    }

}