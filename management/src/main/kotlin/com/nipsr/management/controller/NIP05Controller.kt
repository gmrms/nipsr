package com.nipsr.management.controller

import com.nipsr.management.model.RelayIngress
import com.nipsr.payload.nips.NIP_05
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import kotlin.collections.HashMap

@NIP_05
@Path("/")
@ApplicationScoped
class NIP05Controller {

    @GET
    @Path("/.well-known/nostr.json")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun nip05AddressVerify(@QueryParam("name") name: String?) : HashMap<String, HashMap<String, Any>> {
        val accounts = RelayIngress.listAll().awaitSuspending()
        val relays = arrayListOf("wss://public.nipsr.com")
        return hashMapOf(
            "names" to accounts.associate {
                it.identifier to it.pubkey
            } as HashMap<String, Any>,
            "relays" to accounts.associate {
                it.pubkey to relays
            } as HashMap<String, Any>
        )
    }

}