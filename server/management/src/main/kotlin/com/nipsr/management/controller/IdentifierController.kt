package com.nipsr.management.controller

import com.nipsr.management.config.NIP05Config
import com.nipsr.management.model.IdentifierRequest
import com.nipsr.management.model.Invoice
import com.nipsr.management.service.IdentifierService
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.UriInfo

@ApplicationScoped
@Path("/identifier")
class IdentifierController(
    private val identifierService: IdentifierService,
    private val nip05Config: NIP05Config
) {

    @GET
    @Path("/{domain}/{identifier}")
    suspend fun checkAvailability(
        @PathParam("identifier") identifier: String,
        @PathParam("domain") domain: String
    ) = identifierService.checkAvailability(identifier, domain)

    @GET
    @Path("/domains")
    suspend fun getAvailableDomains() = nip05Config.domains()

    @POST
    suspend fun requestAddress(request: IdentifierRequest): Invoice {
        return identifierService.requestAddress(request)
    }

}