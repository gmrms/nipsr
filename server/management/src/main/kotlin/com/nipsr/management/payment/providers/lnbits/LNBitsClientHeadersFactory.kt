package com.nipsr.management.payment.providers.lnbits

import io.quarkus.arc.properties.IfBuildProperty
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.MultivaluedHashMap
import javax.ws.rs.core.MultivaluedMap
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory


@ApplicationScoped
@IfBuildProperty(name = "$LNBITS_CONFIG.enabled", stringValue = "true")
class LNBitsClientHeadersFactory(
    private val lnbItsConfig: LNBItsConfig
) : ClientHeadersFactory {
    override fun update(
        incomingHeaders: MultivaluedMap<String, String>?,
        clientOutgoingHeaders: MultivaluedMap<String, String>?
    ): MultivaluedMap<String, String> {
        val result: MultivaluedMap<String, String> = MultivaluedHashMap()
        result.add("X-API-KEY", lnbItsConfig.apiKey())
        return result
    }
}
