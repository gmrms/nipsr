package com.nipsr.relay.filters.messages.global

import com.nipsr.relay.config.NipsrRelaySettings
import com.nipsr.relay.exeptions.RelayAccessDeniedException
import com.nipsr.relay.filters.FilterType
import com.nipsr.relay.filters.messages.MessageFilter
import com.nipsr.relay.message.Message
import com.nipsr.relay.message.MessageType
import com.nipsr.relay.model.SessionsContext
import com.nipsr.relay.service.AccessService
import io.quarkus.arc.properties.IfBuildProperty
import javax.enterprise.context.ApplicationScoped

fun main() {
    println(System.currentTimeMillis())
}

/**
 * An message filter that checks if a user is allowed to send messages.
 */
@ApplicationScoped
@IfBuildProperty(name = "nipsr.relay.settings.private.enabled", stringValue = "true")
class AllowAccessMessageFilter(
    private val nipsrRelaySettings: NipsrRelaySettings,
    private val accessService: AccessService
) : MessageFilter {

    override suspend fun filter(message: Message, sessionContext: SessionsContext) {
        val authenticated = sessionContext.currentSession.info.auth.authenticated
        val messageType = MessageType.valueOf(message.first() as String)
        when(messageType){
            MessageType.EVENT -> {
                if(!nipsrRelaySettings.requireAuthentication() || authenticated){
                    if(!accessService.hasAccess(sessionContext.currentSession.info.auth.pubkey!!)){
                        throw RelayAccessDeniedException("Access denied")
                    }
                }
            }
            else -> {
                if(!nipsrRelaySettings.private().allowPublicReads() && !authenticated) {
                    throw RelayAccessDeniedException("Unauthenticated users cannot send events")
                }
            }
        }
    }

    override fun type() = FilterType.GLOBAL

    override fun handlesType(messageType: MessageType) = true

}
