package com.nipsr.management.controller

import com.nipsr.management.model.RelayIngress
import io.quarkus.arc.profile.IfBuildProfile
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@ApplicationScoped
@Path("/ingress")
@IfBuildProfile("dev")
class ManagementController {

    @POST
    @Path("/{pubkey}/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun insertIngress(@PathParam("pubkey") pubkey: String, @PathParam("name") name: String) =
        RelayIngress.persist(
            RelayIngress().apply {
                this.pubkey = pubkey
                this.identifier = name
                this.expiration = Long.MAX_VALUE
            }
        ).awaitSuspending()

}