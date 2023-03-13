package com.nipsr.management.controller

import com.nipsr.management.model.Invoice
import com.nipsr.management.service.RelayIngressService
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@ApplicationScoped
@Path("/ingress")
class RelayIngressController(
    private val relayIngressService: RelayIngressService
) {

    @GET
    @Path("/{address}")
    suspend fun checkAvailability(@PathParam("address") address: String) =
        relayIngressService.checkAvailability(address)


    @POST
    @Path("/{address}/{pubkey}")
    suspend fun requestAddress(@PathParam("pubkey") pubkey: String, @PathParam("address") address: String): Invoice {
        return relayIngressService.requestAddress(pubkey, address)
    }

}