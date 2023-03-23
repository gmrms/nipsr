package com.nipsr.relay.listener

import com.nipsr.payload.nips.NIP_05
import com.nipsr.relay.service.AccessService
import io.quarkus.arc.properties.IfBuildProperty
import io.vertx.core.json.JsonObject
import javax.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming

@NIP_05
@ApplicationScoped
@IfBuildProperty(name = "nipsr.relay.settings.grant-access-nip05", stringValue = "true")
class IdentifierCreatedListener(
    private val accessService: AccessService
) {

    @Incoming("identifier-created")
    suspend fun created(message: JsonObject) {
        val identifierMessage = message.mapTo(IdentifierMessage::class.java)
        accessService.grantAccess(identifierMessage.pubkey, identifierMessage.expiration)
    }

    @Incoming("identifier-deleted")
    suspend fun deleted(message: JsonObject) {
        val identifierMessage = message.mapTo(IdentifierMessage::class.java)
        accessService.revoke(identifierMessage.pubkey)
    }

    class IdentifierMessage {
        lateinit var pubkey: String
        var expiration: Long = 0
    }

}