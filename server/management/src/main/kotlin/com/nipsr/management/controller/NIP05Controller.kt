package com.nipsr.management.controller

import com.nipsr.management.service.IdentifierService
import com.nipsr.payload.nips.NIP_05
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType

@NIP_05
@Path("/")
@ApplicationScoped
class NIP05Controller(
    private val identifierService: IdentifierService
) {

    @GET
    @Path("/.well-known/nostr.json")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun nip05AddressVerify(
        @QueryParam("name") name: String,
        @Context headers: HttpHeaders
    ) : Map<String, Map<String, String>> {
        val accounts = identifierService.findAllIngressByIdentifierAndDomain(name, headers.getHeaderString("Host"))
        return mapOf(
            "names" to accounts.associate {
                it.identifier to it.pubkey
            }
        )
    }

}