package com.nipsr.relay.listener

import com.nipsr.payload.nips.NIP_05
import com.nipsr.relay.service.AccessService
import io.quarkus.arc.properties.IfBuildProperty
import javax.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.reactive.messaging.Incoming

@NIP_05
@ApplicationScoped
@IfBuildProperty(name = "nipsr.relay.settings.grant-access-nip05", stringValue = "true")
class IdentifierCreatedListener(
    private val accessService: AccessService
) {

    @Incoming("identifier-created")
    suspend fun created(message: IdentifierMessage) {
        accessService.grantAccess(message.pubkey, message.expiration)
    }

    @Incoming("identifier-deleted")
    suspend fun deleted(message: IdentifierMessage) {
        accessService.revoke(message.pubkey)
    }

    data class IdentifierMessage(
        val pubkey: String,
        val expiration: Long = 0
    )

}