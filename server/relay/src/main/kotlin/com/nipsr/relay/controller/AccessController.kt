package com.nipsr.relay.controller

import com.nipsr.relay.model.Access
import com.nipsr.relay.service.AccessService
import javax.annotation.security.RolesAllowed
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@Path("/access")
@RolesAllowed("admin")
@ApplicationScoped
class AccessController(
    private val accessService: AccessService
) {

    @GET
    suspend fun listAll() = accessService.findAll()

    @GET
    @Path("/{pubkey}")
    suspend fun hasAccess(@PathParam("pubkey") pubkey: String) = accessService.hasAccess(pubkey)

    @POST
    suspend fun add(access: Access) = accessService.grantAccess(access)

    @DELETE
    @Path("/{pubkey}")
    suspend fun revoke(@PathParam("pubkey") pubkey: String) = accessService.revoke(pubkey)

    @PUT
    suspend fun edit(access: Access) = accessService.grantAccess(access)

}