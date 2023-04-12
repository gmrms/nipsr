package com.nipsr.management.controller

import com.nipsr.management.model.Identifier
import com.nipsr.management.model.IdentifierRequest
import com.nipsr.management.service.IdentifierService
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@Path("/admin/identifier")
@RolesAllowed("admin")
@ApplicationScoped
class ManagementController(
    private val identifierService: IdentifierService
) {

    @GET
    fun listAll() = Identifier.streamAll()

    @POST
    suspend fun add(identifierRequest: IdentifierRequest) =
        identifierService.create(identifierRequest)

    @DELETE
    @Path("/{identifier}")
    suspend fun delete(@PathParam("identifier") identifier: String) =
        identifierService.delete(identifier)


}