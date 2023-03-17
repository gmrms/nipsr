package com.nipsr.management.controller

import com.nipsr.management.model.Invoice
import com.nipsr.management.service.IdentifierService
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@ApplicationScoped
@Path("/identifier")
class IdentifierController(
    private val identifierService: IdentifierService
) {

    @GET
    @Path("/{identifier}")
    suspend fun checkAvailability(@PathParam("identifier") identifier: String) =
        identifierService.checkAvailability(identifier)

    @POST
    @Path("/{identifier}/{pubkey}")
    suspend fun requestAddress(@PathParam("pubkey") pubkey: String, @PathParam("identifier") identifier: String): Invoice {
        return identifierService.requestAddress(pubkey, identifier)
    }

}