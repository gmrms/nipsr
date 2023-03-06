package com.nipsr.relay.controller

import com.nipsr.payload.nips.NIP_11
import com.nipsr.relay.config.NipsrRelayConfig
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@NIP_11
@Path("/")
@ApplicationScoped
class RestController(
    private val nipsrRelayConfig: NipsrRelayConfig
) {

    @GET
    @Produces("application/nostr+json")
    fun metadata() = nipsrRelayConfig.getRelayMetadata()

}